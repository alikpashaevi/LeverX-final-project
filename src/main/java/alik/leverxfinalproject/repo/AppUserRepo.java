package alik.leverxfinalproject.repo;

import alik.leverxfinalproject.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    AppUser getAppUserByEmail(String email);
    boolean existsByEmail(String email);
    AppUser findByEmail(String email);


    @Query("SELECT u FROM AppUser u WHERE u.isVerified = false")
    Page<AppUser> findUnverifiedUsers(Pageable pageable);
}
