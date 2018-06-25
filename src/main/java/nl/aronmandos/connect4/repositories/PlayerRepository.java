package nl.aronmandos.connect4.repositories;

import nl.aronmandos.connect4.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findById(Long id);
    Optional<Player> findByUsername(String username);
}
