package alik.leverxfinalproject.service;

import alik.leverxfinalproject.components.GetUserIdFromToken;
import alik.leverxfinalproject.constants.MapObjects;
import alik.leverxfinalproject.entity.Game;
import alik.leverxfinalproject.entity.GameObject;
import alik.leverxfinalproject.model.GameObjectDTO;
import alik.leverxfinalproject.model.GameObjectRequest;
import alik.leverxfinalproject.repo.GameObjectRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class GameObjectService {

    private final GameObjectRepo gameObjectRepo;
    private final GameService gameService;
    private final UserService userService;

    public GameObjectService(GameObjectRepo gameObjectRepo, GameService gameService, UserService userService) {
        this.gameObjectRepo = gameObjectRepo;
        this.gameService = gameService;
        this.userService = userService;
    }

    public GameObjectDTO getGameObjectById(long id) {
        GameObject gameObject = gameObjectRepo.findById(id).orElseThrow(() -> new RuntimeException("GameObject not found"));
        return MapObjects.mapToDTO(gameObject);
    }

    public void addGameObject(GameObjectRequest request) {
        GameObject gameObject = new GameObject();

        gameObject.setTitle(request.getName());
        gameObject.setText(request.getText());
        Game game = gameService.getGame(request.getGameId());
        if (game == null) {
            throw new RuntimeException("Game not found, please create the game first");
        }
        gameObject.setGame(game);
        System.out.println("User ID from token: " + GetUserIdFromToken.getUserIdFromToken());
        gameObject.setAppUser(userService.getUser(GetUserIdFromToken.getUserIdFromToken()));

        gameObjectRepo.save(gameObject);
    }

    public Page<GameObject> getAllGameObjects(int page, int size) {
        return gameObjectRepo.findAll(PageRequest.of(page, size));
    }

    public void editGameObject(long id, GameObjectRequest request) {
        GameObject gameObject = gameObjectRepo.findById(id).orElseThrow(() -> new RuntimeException("GameObject not found"));

        long userId = GetUserIdFromToken.getUserIdFromToken();

        if (gameObject.getAppUser().getId() != userId) {
            throw new RuntimeException("You are not the creator of this game object");
        }

        gameObject.setTitle(request.getName());
        gameObject.setText(request.getText());

        gameObjectRepo.save(gameObject);
    }

    public void deleteGameObjectById(long id) {
        GameObject gameObject = gameObjectRepo.findById(id).orElseThrow(() -> new RuntimeException("GameObject not found"));

        long userId = GetUserIdFromToken.getUserIdFromToken();

        if (gameObject.getAppUser().getId() != userId) {
            throw new RuntimeException("You are not the creator of this game object");
        }

        gameObjectRepo.deleteById(id);
    }

}
