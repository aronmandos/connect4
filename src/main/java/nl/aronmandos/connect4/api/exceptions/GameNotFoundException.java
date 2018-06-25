package nl.aronmandos.connect4.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GameNotFoundException extends RuntimeException {
	public GameNotFoundException(Long gameId) {
		super("Could not find game '" + gameId + "'.");
	}
}
