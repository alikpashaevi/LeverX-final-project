package alik.leverxfinalproject.controller;

import alik.leverxfinalproject.entity.AppUser;

import alik.leverxfinalproject.model.dto.CommentDTO;
import alik.leverxfinalproject.model.request.CommentRequest;
import alik.leverxfinalproject.model.request.UserCommentRequest;
import alik.leverxfinalproject.model.dto.UserDTO;
import alik.leverxfinalproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static alik.leverxfinalproject.constants.AuthorizationConstants.ADMIN;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public Page<UserDTO> getUsers(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        return userService.getAllUsers(page, size);
    }

    @GetMapping("/profile/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserDTO(id);
    }

    @GetMapping("/profile/top_sellers")
    public Page<UserDTO> getTopSellers(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return userService.getTopSellers(page, size);
    }

    @GetMapping("/profile/get_seller_by_game_and_rating")
    public Page<UserDTO> getSellersByGameAndRating(@RequestParam long gameId,
                                                   @RequestParam double minRating,
                                                   @RequestParam double maxRating,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        return userService.getUsersByGamesAndRatings(gameId, minRating, maxRating, page, size);
    }

    @PreAuthorize(ADMIN)
    @GetMapping("/unverified")
    public Page<UserDTO> getUnverifiedUsers(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        return userService.getUnverifiedUsers(page, size);
    }

    @PreAuthorize(ADMIN)
    @GetMapping("/unverified/{id}")
    public UserDTO getUnverifiedUserById(@PathVariable long id) {
        return userService.getUnverifiedUser(id);
    }

    @PreAuthorize(ADMIN)
    @PostMapping("/verify")
    public ResponseEntity<Void> verifyUser(@RequestParam long id) {
        userService.verifyUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Void> addComment(@PathVariable Long id, @RequestBody @Valid CommentRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        userService.addComment(id, request, httpRequest, httpResponse);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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
    public ResponseEntity<Void> deleteComment(@PathVariable Long userId, @PathVariable Long commentId, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        userService.deleteComment(commentId, userId, httpRequest, httpResponse);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/comments/{commentId}")
    public ResponseEntity<Void> editComment(@PathVariable Long userId, @PathVariable Long commentId, @RequestBody @Valid CommentRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        userService.updateComment(commentId, userId, request, httpRequest, httpResponse);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize(ADMIN)
    @GetMapping("/unapproved_comments")
    public Page<CommentDTO> getUnapprovedComments(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return userService.getUnapprovedComments(page, size);
    }

    @PreAuthorize(ADMIN)
    @PostMapping("/unapproved_comments/approve/{userId}/comment/{commentId}")
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

    @PostMapping("/profile/create_seller")
    public ResponseEntity<Void> createSellerAccount(@RequestBody @Valid UserCommentRequest userCommentRequest, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        userService.createUserByComment(userCommentRequest, httpRequest, httpResponse);
        return ResponseEntity.ok().build();
    }


}
