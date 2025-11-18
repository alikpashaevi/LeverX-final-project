package alik.leverxfinalproject.repo;

import alik.leverxfinalproject.entity.GameObject;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GameObjectRepo extends JpaRepository<GameObject, Long> {

}
