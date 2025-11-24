package alik.leverxfinalproject.service;

import alik.leverxfinalproject.entity.Game;
import alik.leverxfinalproject.error.UnauthorizedActionException;
import alik.leverxfinalproject.model.request.GameRequest;
import alik.leverxfinalproject.repo.GameRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final GameRepo gameRepo;

    public GameService(GameRepo gameRepo) {
        this.gameRepo = gameRepo;
    }

    public void addGame(GameRequest request) {
        if (gameRepo.existsByTitle(request.getTitle())) {
            throw new UnauthorizedActionException("Game with this title already exists");
        }
        Game game = new Game();
        game.setTitle(request.getTitle());

        gameRepo.save(game);
    }

    public Page<Game> getAllGames(int page, int size) {
        return gameRepo.findAll(PageRequest.of(page, size));
    }

    public Game getGame(long id) {
        return gameRepo.findById(id).orElse(null);
    }



}
