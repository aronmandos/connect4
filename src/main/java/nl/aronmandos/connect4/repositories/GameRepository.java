package nl.aronmandos.connect4.repositories;

import nl.aronmandos.connect4.models.Game;
import nl.aronmandos.connect4.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
	
	Collection<Game> findAllByEndDateNull();
    Collection<Game> findAllByEndDateNotNull();
	@Query("select g from Game g where (g.challenger = :player or g.opponent = :player)")
	Collection<Game> findByPlayer(@Param("player")Player player);
	@Query("select g from Game g where g.endDate is null and (g.challenger = :player or g.opponent = :player)")
	Collection<Game> findActiveByPlayer(@Param("player")Player player);
}
