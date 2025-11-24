package alik.leverxfinalproject.repo;

import alik.leverxfinalproject.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepo extends JpaRepository<Game, Long> {
    boolean existsByTitle(String title);
}
