package alik.leverxfinalproject.auth;

import alik.leverxfinalproject.entity.AppUser;
import alik.leverxfinalproject.repo.AppUserRepo;
import alik.leverxfinalproject.repo.RoleRepo;
import alik.leverxfinalproject.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class RegisterService {

    private final AppUserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    // TODO: replace it with role service later
    private final RoleRepo roleRepo;

    public RegisterService(AppUserRepo userRepo, PasswordEncoder passwordEncoder, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
    }

    public void register(RegisterRequest registerRequest) {
        AppUser appUser = new AppUser();
        if (userRepo.existsByEmail(registerRequest.getEmail())) {
            // TODO: replace with custom exception
            throw new RuntimeException("Email already in use");
        }
        appUser.setFirstName(registerRequest.getFirstName());
        appUser.setLastName(registerRequest.getLastName());
        appUser.setEmail(registerRequest.getEmail());
        appUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        appUser.setRoles(roleRepo.findById(2L).stream().collect(Collectors.toSet()));

        userRepo.save(appUser);

    }

}
