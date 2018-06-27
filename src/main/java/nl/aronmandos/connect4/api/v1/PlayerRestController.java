package nl.aronmandos.connect4.api.v1;

import nl.aronmandos.connect4.api.exceptions.GameNotFoundException;
import nl.aronmandos.connect4.api.exceptions.PlayerNotFoundException;
import nl.aronmandos.connect4.models.Game;
import nl.aronmandos.connect4.models.Player;
import nl.aronmandos.connect4.models.PlayingField;
import nl.aronmandos.connect4.repositories.GameRepository;
import nl.aronmandos.connect4.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

@CrossOrigin
@RestController
@RequestMapping("/players")
public class PlayerRestController {
	private final PlayerRepository playerRepository;
	private final GameRepository gameRepository;
	
	@Autowired
	PlayerRestController(PlayerRepository playerRepository, GameRepository gameRepository){
		this.playerRepository = playerRepository;
		this.gameRepository = gameRepository;
	}
	
	
	@GetMapping
	List<Player> readAll() {
		return this.playerRepository.findAll();
	}
	
	@PostMapping
	ResponseEntity<?> add(@RequestBody Player player) {
		//TODO maybe a safer way to transfer personal data?
		Player result = playerRepository.save(player);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(result.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping("/{playerId}")
	Player readPlayer(@PathVariable Long playerId) {
		return this.playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
	}
	
	@PostMapping("/{playerId}/invite")
	void invitePlayerForGame(@PathVariable Long playerId) { //TODO return
		//TODO get current user
		//TODO set current user as challenger
		Player challenger = this.playerRepository.findById((long) 1).orElseThrow(() -> new PlayerNotFoundException(playerId));
		Player opponent = this.playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
		
		PlayingField field = new PlayingField(6, 6);
		int firstPlayer = new Random().nextInt(2)+1;
		
		Game game = new Game(challenger, opponent, field, firstPlayer, new Date(), null, null, null);
		gameRepository.save(game);
	}
	
	@GetMapping("/{playerId}/friends")
	Collection<Player> readFriends(@PathVariable Long playerId) {
		Player current = this.playerRepository.findById((long) 1).orElseThrow(() -> new PlayerNotFoundException(playerId));
		return current.getFriends();
	}
	
	@PostMapping("/{playerId}/friends/{friendId}")
	void addFriend(@PathVariable Long playerId, @PathVariable Long friendId) { //TODO return
		//TODO get current user
		//TODO set current user as challenger
		Player current = this.playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
		Player friend = this.playerRepository.findById(friendId).orElseThrow(() -> new PlayerNotFoundException(playerId));
		current.addFriend(friend);
		this.playerRepository.save(current);
		
	}
	
	
	@GetMapping("/{playerId}/games")
	Collection<Game> readPlayerActiveGames(@PathVariable Long playerId) {
		Player player = this.playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
		return this.gameRepository.findActiveByPlayer(player);//TODO test whether object ref works
		
	}
}
