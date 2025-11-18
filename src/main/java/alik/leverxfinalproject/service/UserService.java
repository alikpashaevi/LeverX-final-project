package alik.leverxfinalproject.service;

import alik.leverxfinalproject.entity.AppUser;
import alik.leverxfinalproject.repo.AppUserRepo;
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

    public UserService(AppUserRepo appUserRepo) {
        this.appUserRepo = appUserRepo;

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

}
