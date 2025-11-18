package alik.leverxfinalproject.controller;

import alik.leverxfinalproject.entity.GameObject;
import alik.leverxfinalproject.model.GameObjectRequest;
import alik.leverxfinalproject.service.GameObjectService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static alik.leverxfinalproject.constants.AuthorizationConstants.SELLER_OR_ADMIN;

@RestController
@RequestMapping("/game_object")
@PreAuthorize(SELLER_OR_ADMIN)
public class GameObjectController {

    private final GameObjectService gameObjectService;

    public GameObjectController(GameObjectService gameObjectService) {
        this.gameObjectService = gameObjectService;
    }

    @GetMapping
    public Page<GameObject> getGameObjects(@RequestParam (defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return gameObjectService.getAllGameObjects(page, size);
    }

    @PostMapping
    public ResponseEntity<Void> CreateGameObject(@RequestBody GameObjectRequest request) {
        gameObjectService.addGameObject(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> editGameObject(@PathVariable long id, @RequestBody GameObjectRequest request) {
        gameObjectService.editGameObject(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGameObject(@PathVariable Long id) {
        gameObjectService.deleteGameObjectById(id);
        return ResponseEntity.ok().build();
    }

}
