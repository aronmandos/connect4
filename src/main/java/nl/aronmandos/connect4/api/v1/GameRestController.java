package nl.aronmandos.connect4.api.v1;

import nl.aronmandos.connect4.api.exceptions.*;
import nl.aronmandos.connect4.models.Game;
import nl.aronmandos.connect4.models.Player;
import nl.aronmandos.connect4.models.PlayingField;
import nl.aronmandos.connect4.repositories.GameRepository;
import nl.aronmandos.connect4.repositories.PlayerRepository;
import nl.aronmandos.connect4.repositories.PlayingFieldRespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/games")
public class GameRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final GameRepository gameRepository;
	private final PlayerRepository playerRepository;
	private final PlayingFieldRespository fieldRespository;
	@Autowired
	GameRestController(GameRepository gameRepository, PlayerRepository playerRepository, PlayingFieldRespository fieldRepository){
		this.gameRepository = gameRepository;
		this.playerRepository = playerRepository;
		this.fieldRespository= fieldRepository;
	}
	
	@GetMapping
	Collection<Game> readAll() {
		return this.gameRepository.findAll();
	}
	
	@GetMapping("/active")
	Collection<Game> readActive() {
		return this.gameRepository.findAllByEndDateNull();
	}
	
	@GetMapping("/history")
	Collection<Game> readHistory() {
		return this.gameRepository.findAllByEndDateNotNull();
	}
	
	
	@GetMapping("/single/{gameId}")
	Game readGame(@PathVariable Long gameId) {
		return this.gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
	}
	
	@PostMapping("/move/{gameId}/{column}")
	Game doMove(@PathVariable Long gameId, @PathVariable int column) {
		Game game = this.gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
		PlayingField field = game.getPlayingField();
		

		int playerNumber = game.getPlayerOnTurn();
		//this needs more graceful code when >2 player support is in
		int opponentNumber = (playerNumber==1)?2:1;

		
		if (game.getPlayerOnTurn() != playerNumber) {
			throw new PlayerNotOnTurnException(gameId, playerNumber);
		}
		if (!field.isMoveValid(column, playerNumber)) {
			throw new MoveInvalidException(gameId, column, playerNumber);
		}
		if (game.getWinner()!= null && game.getWinner().getClass() == Player.class) {
			throw new GameIsWonException(gameId, playerNumber);
		}
		field.doMove(column, playerNumber);

		game.setPlayerOnTurn(opponentNumber);
		
		
		int winnerNumber = field.checkForVictory();
		Player winner = null;
		if (winnerNumber == 1 ) {
			winner = game.getChallenger();
		} else if (winnerNumber == 2) {
			winner = game.getOpponent();
		}
		game.setWinner(winner);
		
		if (winner != null) {
			game.setPlayerOnTurn(0);
		} else if (playerNumber == 1) {
			game.setPlayerOnTurn(2);
		} else if (playerNumber == 2) {
			game.setPlayerOnTurn(1);
		}
		
		gameRepository.save(game);
		return game;
	}
	
	@PostMapping("/surrender/{gameId}")
	Game surrender(@PathVariable Long gameId) {
		
		Game game = this.gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
		
		Player current = this.playerRepository.findById((long)game.getPlayerOnTurn()).orElseThrow(() -> new GameNotFoundException((long) gameId));
		Player opponent;
		if (game.getChallenger() == current) {
			opponent = game.getOpponent();
		} else {
			opponent = current;
		}
		
		game.setPlayerOnTurn(0);
		game.setWinner(opponent);
		game.setEndDate(new Date());
		
		gameRepository.save(game);
		return game;
	}
	
	@PostMapping("/newgame/{challengerId}/{opponentId}")
	Game startNewGame(@PathVariable Long challengerId, @PathVariable Long opponentId) {
		Player challenger = this.playerRepository.findById(challengerId).orElseThrow(() -> new PlayerNotFoundException(challengerId));
		Player opponent= this.playerRepository.findById(opponentId).orElseThrow(() -> new PlayerNotFoundException(opponentId));
		
		PlayingField field = new PlayingField();
		int firstPlayer = new Random().nextInt(field.getPlayerCount());
		fieldRespository.save(field);
		Game game = new Game(challenger, opponent, field, firstPlayer, new Date(), new Date(), null, null);
		gameRepository.save(game);
		return game;
	}
}
