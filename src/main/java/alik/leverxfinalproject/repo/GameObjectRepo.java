package alik.leverxfinalproject.repo;

import alik.leverxfinalproject.entity.GameObject;
import alik.leverxfinalproject.model.GameObjectDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GameObjectRepo extends JpaRepository<GameObject, Long> {
    @Query("SELECT NEW alik.leverxfinalproject.model.GameObjectDTO" +
                "(go.id, go.title, go.text, go.game.id, go.appUser.id) " +
                "FROM GameObject go")
    Page<GameObjectDTO> findGameObjects(Pageable pageable);
}
