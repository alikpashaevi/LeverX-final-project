package alik.leverxfinalproject.service;

import alik.leverxfinalproject.entity.AppUser;
import alik.leverxfinalproject.repo.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class UserService {

    private final AppUserRepo appUserRepo;

    public UserService(AppUserRepo appUserRepo) {
        this.appUserRepo = appUserRepo;
    }

    public AppUser getUserByEmail(String email) {
        return appUserRepo.getAppUserByEmail(email);
    }

}
