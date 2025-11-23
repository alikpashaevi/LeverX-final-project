package alik.leverxfinalproject.controller;

import alik.leverxfinalproject.model.dto.GameObjectDTO;
import alik.leverxfinalproject.model.request.GameObjectRequest;
import alik.leverxfinalproject.service.GameObjectService;
import jakarta.validation.Valid;
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
    public Page<GameObjectDTO> getGameObjects(@RequestParam (defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        return gameObjectService.getAllGameObjects(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameObjectDTO> getGameObjectById(@PathVariable Long id) {
        GameObjectDTO gameObjectDTO = gameObjectService.getGameObjectById(id);
        return ResponseEntity.ok(gameObjectDTO);
    }

    @PostMapping
    public ResponseEntity<Void> CreateGameObject(@RequestBody @Valid GameObjectRequest request) {
        gameObjectService.addGameObject(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> editGameObject(@PathVariable long id, @RequestBody @Valid GameObjectRequest request) {
        gameObjectService.editGameObject(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGameObject(@PathVariable Long id) {
        gameObjectService.deleteGameObjectById(id);
        return ResponseEntity.ok().build();
    }

}
