package alik.leverxfinalproject.service;

import alik.leverxfinalproject.entity.AppUser;
import alik.leverxfinalproject.entity.Comment;
import alik.leverxfinalproject.error.CommentNotFoundException;
import alik.leverxfinalproject.error.EmailAlreadyInUseException;
import alik.leverxfinalproject.error.UnauthorizedActionException;
import alik.leverxfinalproject.error.UserNotFoundException;
import alik.leverxfinalproject.model.dto.CommentDTO;
import alik.leverxfinalproject.model.request.CommentRequest;
import alik.leverxfinalproject.model.request.UserCommentRequest;
import alik.leverxfinalproject.model.dto.UserDTO;
import alik.leverxfinalproject.repo.AppUserRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
//@RequiredArgsConstructor
public class UserService {

    private final AppUserRepo appUserRepo;
    private final AnonymousIdService anonymousIdService;

    public UserService(AppUserRepo appUserRepo, AnonymousIdService anonymousIdService) {
        this.appUserRepo = appUserRepo;
        this.anonymousIdService = anonymousIdService;

    }

    public AppUser getUser(long id) {
        return appUserRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public UserDTO getUserDTO(long id) {
        return appUserRepo.findUserDTO(id);
    }

    public Page<UserDTO> getAllUsers(int page, int size) {
        return appUserRepo.findAllUsersWithRatings(PageRequest.of(page, size));
    }

    public AppUser getUserByEmail(String email) {
        return appUserRepo.getAppUserByEmail(email);
    }

    public Page<UserDTO> getUnverifiedUsers(int page, int size) {
        return appUserRepo.getUnverifiedUsers(PageRequest.of(page, size));
    }

    public AppUser getUnverifiedUserEntity(long id) {
        AppUser userToReturn = appUserRepo.findUnverifiedUserEntityById(id);
        if (userToReturn == null) {
            throw new UserNotFoundException("User not found or already verified");
        }

        return userToReturn;
    }

    public UserDTO getUnverifiedUser(long id) {
        UserDTO userToReturn = appUserRepo.getUnverifiedUserById(id);
        if (userToReturn == null) {
            throw new UserNotFoundException("User not found or already verified");
        }

        return userToReturn;
    }

    public void verifyUser(long id) {
        AppUser user = getUnverifiedUserEntity(id);
        user.setVerified(true);
        appUserRepo.save(user);
    }

    public void addComment(Long userId, CommentRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        AppUser user = getUser(userId);
        String authorId;
        Comment comment;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&
                authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal())) {
            authorId = authentication.getName();
        } else {
            authorId = anonymousIdService.getOrCreateAnonymousId(httpRequest, httpResponse);
        }

        if (authorId.equals(userId.toString())) {
            throw new UnauthorizedActionException("You cannot comment on your own profile");
        }

        if (appUserRepo.authorHasCommented(userId, authorId) && appUserRepo.findCommentByAuthorIdAndAppUserId(userId, authorId) != null) {
            throw new UnauthorizedActionException("You have already commented on this user. Consider updating your existing comment. If you can't find it, it might be pending approval.");
        } else {
            comment = new Comment();
        }

        comment.setText(request.getText());
        comment.setRating(request.getRating());
        comment.setAppUser(user);
        comment.setAuthorId(authorId);

        user.getComments().add(comment);
        appUserRepo.save(user);
    }

    public Page<CommentDTO> getUserComments(Long userId, int page, int size) {
        return appUserRepo.findCommentsByUserId(userId, PageRequest.of(page, size));
    }

    public CommentDTO getComment(Long commentId, Long userId) {
        CommentDTO commentToReturn = appUserRepo.findCommentById(commentId, userId);
        if (commentToReturn == null) {
            throw new CommentNotFoundException("Comment not found");
        }

        return commentToReturn;
    }

    public Comment getCommentEntity(Long commentId, Long userId) {
        Comment commentToReturn = appUserRepo.findApprovedCommentEntityById(commentId, userId);
        if (commentToReturn == null) {
            throw new CommentNotFoundException("Comment not found");
        }

        return commentToReturn;
    }

    public void deleteComment(Long commentId, Long userId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        AppUser user = getUser(userId);
        Comment comment = getCommentEntity(commentId, userId);

        String authorId = resolveAuthorId(httpRequest, httpResponse);

        if (!comment.getAuthorId().equals(authorId)) {
            throw new UnauthorizedActionException("You are not the author of this comment");
        }

        user.getComments().remove(comment);
        appUserRepo.save(user);
    }

    public void updateComment(Long commentId, Long userId, CommentRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        AppUser user = getUser(userId);
        Comment comment = getCommentEntity(commentId, userId);
        String authorId = resolveAuthorId(httpRequest, httpResponse);

        if (!comment.getAuthorId().equals(authorId)) {
            throw new UnauthorizedActionException("You are not the author of this comment");
        }

        comment.setText(request.getText());
        comment.setRating(request.getRating());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setIsApproved(false);
        appUserRepo.save(user);

    }

    public Page<CommentDTO> getUnapprovedComments(int page, int size) {
        return appUserRepo.findUnapprovedComments(PageRequest.of(page, size));
    }

    public void approveComment(Long commentId, Long userId) {
        Comment comment = getUnapprovedCommentEntity(commentId, userId);
        comment.setIsApproved(true);
        appUserRepo.save(getUser(userId));
    }

    public Comment getUnapprovedCommentEntity(Long commentId, Long userId) {
        Comment commentToReturn = appUserRepo.findUnapprovedCommentEntityById(commentId, userId);
        if (commentToReturn == null) {
            throw new CommentNotFoundException("Comment not found or already approved");
        }

        return commentToReturn;
    }

    public CommentDTO getUnapprovedComment(Long commentId) {
        CommentDTO commentToReturn = appUserRepo.findUnapprovedComment(commentId);
        if (commentToReturn == null) {
            throw new CommentNotFoundException("Comment not found");
        }

        return commentToReturn;
    }

    public void rejectComment(Long commentId, Long userId) {
        AppUser user = getUser(userId);
        Comment comment = getUnapprovedCommentEntity(commentId, userId);

        user.getComments().remove(comment);
        appUserRepo.save(user);
    }

    private String resolveAuthorId(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&
                authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal())) {
            String name = authentication.getName();
            return name;
        } else {
            String anon = anonymousIdService.getOrCreateAnonymousId(request, response);
            return anon;
        }
    }

    public void createUserByComment(UserCommentRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        if (appUserRepo.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyInUseException("User with this email already exists");
        }

        AppUser user = new AppUser();
        String authorId = resolveAuthorId(httpRequest, httpResponse);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setEmailVerified(true);
        Comment comment = new Comment();
        comment.setAppUser(user);
        comment.setText(request.getComment());
        comment.setRating(request.getRating());
        comment.setAuthorId(authorId);
        user.getComments().add(comment);

        appUserRepo.save(user);
    }

    public Page<UserDTO> getTopSellers(int page, int size) {
        return appUserRepo.findTopSellers(PageRequest.of(page, size));
    }

    public Page<UserDTO> getUsersByGamesAndRatings(long gameId, double minRating, double maxRating, int page, int size) {
        return appUserRepo.findUsersByGameAndRatingRange(gameId, minRating, maxRating, PageRequest.of(page, size));
    }

}
