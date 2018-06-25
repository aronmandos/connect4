package nl.aronmandos.connect4.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class PlayerNotOnTurnException extends RuntimeException {
	public PlayerNotOnTurnException(Long gameId, int playerNumber) {
		super("Player '" + playerNumber + "' not on turn in game '" + gameId + "'.");
	}
}
