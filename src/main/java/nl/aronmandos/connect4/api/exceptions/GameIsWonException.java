package nl.aronmandos.connect4.api.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class GameIsWonException extends RuntimeException {
	public GameIsWonException(Long gameId, int playerNumber) {
		super("Game '" + gameId + "' already won by player '" + playerNumber + "'.");
	}
}
