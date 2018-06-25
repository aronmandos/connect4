package nl.aronmandos.connect4;

import nl.aronmandos.connect4.api.v1.GameRestController;
import nl.aronmandos.connect4.api.v1.PlayerRestController;
import nl.aronmandos.connect4.models.Game;
import nl.aronmandos.connect4.models.Player;
import nl.aronmandos.connect4.models.PlayingField;
import nl.aronmandos.connect4.repositories.GameRepository;
import nl.aronmandos.connect4.repositories.PlayerRepository;
import nl.aronmandos.connect4.repositories.PlayingFieldRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@EnableSwagger2
public class Connect4Application {

	public static void main(String[] args) {
		SpringApplication.run(Connect4Application.class, args);
	}
	
	/**
	 * Selecting what to expose with swagger 2 api docs.
	 *
	 * @return The interface for the Springfox plugin.
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build()
				.pathMapping("/")
				.apiInfo(metadata());
	}
	
	private ApiInfo metadata() {
		
		return new ApiInfoBuilder()
				.title("Welcome to the REST API documentation of Connect4.")
				.description("Connect4 API docs")
				.version("0.1")
				.license("MIT Licence (MIT)")
				.build();
	}
	
	/**
	 * Testing values
	 *
	 * @param gameRepository games repo
	 * @param playerRepository players repo
	 * @return a runner
	 */
	@Autowired
	@Bean
	CommandLineRunner init(GameRepository gameRepository, PlayerRepository playerRepository, PlayingFieldRespository pfRepos) {
		List<String> players = Arrays.asList("jhoeller,dsyer,pwebb,ogierke,rwinch,mfisher,mpollack,jlong".split(","));
		
		return (args) -> {
			players.forEach(
					pl -> {
						Player player = playerRepository.save(new Player(pl,
								pl + "@example.c",
								"password"));
					});
			Player testChallenger = playerRepository.findById((long)1).get();
			Player testOpponent = playerRepository.findById((long)2).get();
			PlayingField field = pfRepos.save(new PlayingField(6, 6));
			field.doMove(1, 1);
			Game testGame = new Game(testChallenger, testOpponent, field , 1, new Date(), new Date(), null, null);
			gameRepository.save(testGame);
		};
	}
}
