package alik.leverxfinalproject.service;

import alik.leverxfinalproject.entity.AppUser;
import alik.leverxfinalproject.entity.Comment;
import alik.leverxfinalproject.model.CommentDTO;
import alik.leverxfinalproject.model.CommentRequest;
import alik.leverxfinalproject.repo.AppUserRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return appUserRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
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
        Comment comment = new Comment();
        comment.setText(request.getText());
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
            // TODO: Create custom exception
            throw new RuntimeException("Comment not found");
        }

        return commentToReturn;
    }

    public void deleteComment(Long commentId, Long userId, HttpServletRequest request) {
        AppUser user = getUser(userId);
        Comment comment = appUserRepo.findCommentEntityById(commentId, userId);

        String currentAnonymousId = anonymousIdService.getCurrentAnonymousId(request);
        if (!comment.getAuthorId().equals(currentAnonymousId)) {
            // TODO: Create custom exception
            throw new RuntimeException("You are not the author of this comment");
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
            throw new RuntimeException("Comment not found");
        }

        return commentToReturn;
    }

    public void rejectComment(Long commentId, Long userId) {
        AppUser user = getUser(userId);
        Comment comment = appUserRepo.findCommentEntityById(commentId, userId);

        user.getComments().remove(comment);
        appUserRepo.save(user);
    }

}
