package alik.leverxfinalproject.controller;

import alik.leverxfinalproject.entity.Game;
import alik.leverxfinalproject.model.GameRequest;
import alik.leverxfinalproject.service.GameService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static alik.leverxfinalproject.constants.AuthorizationConstants.SELLER_OR_ADMIN;

@RestController
@RequestMapping("/game")
@PreAuthorize(SELLER_OR_ADMIN)
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public Page<Game> getGames(@RequestParam (defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        return gameService.getAllGames(page, size);
    }


    @PostMapping
    public void addGame(@RequestBody @Valid GameRequest request) {
        gameService.addGame(request);
    }

}
