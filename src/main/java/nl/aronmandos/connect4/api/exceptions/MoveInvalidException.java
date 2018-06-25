package nl.aronmandos.connect4.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MoveInvalidException extends RuntimeException {
	public MoveInvalidException(Long gameId, int move, int playerNumber) {
		super("Invalid move '" + move + "' in game '" + gameId + "' by player '" + playerNumber + "'.");
	}
}
