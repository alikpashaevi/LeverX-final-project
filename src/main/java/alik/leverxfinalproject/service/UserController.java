package alik.leverxfinalproject.service;

import alik.leverxfinalproject.entity.AppUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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



}
