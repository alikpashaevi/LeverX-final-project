package alik.leverxfinalproject.service;

import alik.leverxfinalproject.entity.AppUser;

import alik.leverxfinalproject.entity.Comment;
import alik.leverxfinalproject.model.CommentDTO;
import alik.leverxfinalproject.model.CommentRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

import static alik.leverxfinalproject.constants.AuthorizationConstants.ADMIN;
import static alik.leverxfinalproject.constants.AuthorizationConstants.SELLER_OR_ADMIN;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize(ADMIN)
    @GetMapping("/unverified")
    public Page<AppUser> getUnverifiedUsers(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        return userService.getUnverifiedUsers(page, size);
    }

    @PreAuthorize(ADMIN)
    @PutMapping("/verify")
    public ResponseEntity<Void> verifyUser(@RequestParam String email) {
        AppUser user = userService.getUserByEmail(email);
        userService.verifyUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Void> addComment(@PathVariable Long id, @RequestBody CommentRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        userService.addComment(id, request, httpRequest, httpResponse);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/comments")
    public Page<CommentDTO> getUserComments(@PathVariable Long id,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return userService.getUserComments(id, page, size);
    }

    @GetMapping("/{userId}/comments/{commentId}")
    public CommentDTO getComment(@PathVariable Long userId, @PathVariable Long commentId) {
        return userService.getComment(commentId, userId);
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long userId, @PathVariable Long commentId, HttpServletRequest request) {
        userService.deleteComment(commentId, userId, request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize(ADMIN)
    @GetMapping("/unapproved_comments")
    public Page<CommentDTO> getUnapprovedComments(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return userService.getUnapprovedComments(page, size);
    }

    @PreAuthorize(ADMIN)
    @PutMapping("/unapproved_comments/approve/{userId}/comment/{commentId}")
    public ResponseEntity<Void> approveComment(@PathVariable Long userId, @PathVariable Long commentId) {
        userService.approveComment(commentId, userId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize(ADMIN)
    @DeleteMapping("/unapproved_comments/reject/{userId}/comment/{commentId}")
    public ResponseEntity<Void> rejectComment(@PathVariable Long userId, @PathVariable Long commentId) {
        userService.rejectComment(commentId, userId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize(ADMIN)
    @GetMapping("/unapproved_comments/{commentId}")
    public CommentDTO getUnapprovedCommentById(@PathVariable Long commentId) {
        return userService.getUnapprovedComment(commentId);
    }



}
