package alik.leverxfinalproject.service;

import alik.leverxfinalproject.components.GetUserIdFromToken;
import alik.leverxfinalproject.entity.AppUser;
import alik.leverxfinalproject.entity.Comment;
import alik.leverxfinalproject.error.CommentNotFoundException;
import alik.leverxfinalproject.error.UnauthorizedActionException;
import alik.leverxfinalproject.error.UserNotFoundException;
import alik.leverxfinalproject.model.CommentDTO;
import alik.leverxfinalproject.model.CommentRequest;
import alik.leverxfinalproject.model.UserDTO;
import alik.leverxfinalproject.repo.AppUserRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
        return appUserRepo.findUserWithRating(id);
    }

    public Page<UserDTO> getAllUsers(int page, int size) {
        return appUserRepo.findAllUsersWithRatings(PageRequest.of(page, size));
    }

    public AppUser getUserByEmail(String email) {
        return appUserRepo.getAppUserByEmail(email);
    }

    public Page<AppUser> getUnverifiedUsers(int page, int size) {

        return appUserRepo.findUnverifiedUsers(PageRequest.of(page, size));
    }

    public void verifyUser(AppUser user) {
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
            System.out.println("Authenticated user: " + authorId);
        } else {
            authorId = anonymousIdService.getOrCreateAnonymousId(httpRequest, httpResponse);
        }

        if (appUserRepo.authorHasCommented(userId, authorId)) {
            comment = appUserRepo.findCommentByAuthorIdAndAppUserId(userId, authorId);
        } else {
            comment = new Comment();
        }

        comment.setText(request.getText());
        comment.setRating(request.getRating());
        comment.setAppUser(user);
        comment.setAuthorId(anonymousIdService.getOrCreateAnonymousId(httpRequest, httpResponse));

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

    public void deleteComment(Long commentId, Long userId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        AppUser user = getUser(userId);
        Comment comment = appUserRepo.findCommentEntityById(commentId, userId);

        String authorId = resolveAuthorId(httpRequest, httpResponse);

        if (!comment.getAuthorId().equals(authorId)) {
            throw new UnauthorizedActionException("You are not the author of this comment");
        }

        user.getComments().remove(comment);
        appUserRepo.save(user);
    }

    public Page<CommentDTO> getUnapprovedComments(int page, int size) {
        return appUserRepo.findUnapprovedComments(PageRequest.of(page, size));
    }

    public void approveComment(Long commentId, Long userId) {
        Comment comment = appUserRepo.findCommentEntityById(commentId, userId);
        comment.setIsApproved(true);
        appUserRepo.save(getUser(userId));
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
        Comment comment = appUserRepo.findCommentEntityById(commentId, userId);

        user.getComments().remove(comment);
        appUserRepo.save(user);
    }

    private String resolveAuthorId(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&
                authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal())) {
            String name = authentication.getName();
            System.out.println("Authenticated user: " + name);
            return name;
        } else {
            String anon = anonymousIdService.getOrCreateAnonymousId(request, response);
            System.out.println("Anonymous user: " + anon);
            return anon;
        }
    }


}
