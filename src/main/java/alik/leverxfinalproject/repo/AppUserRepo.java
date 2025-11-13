package alik.leverxfinalproject.repo;

import alik.leverxfinalproject.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    AppUser getAppUserByEmail(String email);
}
