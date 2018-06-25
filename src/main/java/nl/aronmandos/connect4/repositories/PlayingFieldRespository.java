package nl.aronmandos.connect4.repositories;

import nl.aronmandos.connect4.models.Game;
import nl.aronmandos.connect4.models.Player;
import nl.aronmandos.connect4.models.PlayingField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;
	
	public interface PlayingFieldRespository extends JpaRepository<PlayingField, Long> {
	
	
	}
	

