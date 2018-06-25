package nl.aronmandos.connect4.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Player {

    @Id
    @GeneratedValue
    private Long id = null;

    private String username = null;

    private String email = null;

    @JsonIgnore
    private String password = null;

	@ManyToMany
	@JoinTable(name="tbl_friends",
			joinColumns=@JoinColumn(name="playerId"),
			inverseJoinColumns=@JoinColumn(name="friendId")
	)
	private Set<Player> friends = new HashSet<>();
	
	@ManyToMany
	@JoinTable(name="tbl_friends",
			joinColumns=@JoinColumn(name="friendId"),
			inverseJoinColumns=@JoinColumn(name="playerId")
	)
	private Set<Player> friendOf = new HashSet<>();
    
    @OneToMany(mappedBy = "challenger")
    private Set<Game> startedGames = new HashSet<>();
	
	@OneToMany(mappedBy = "opponent")
	private Set<Game> acceptedGames = new HashSet<>();
	
	@OneToMany(mappedBy = "winner")
	private Set<Game> wonGames = new HashSet<>();

    private Player() {

    }

    public Player(final String username, final String email, final String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }
	
	public Long getId() {
    	return id;
    }
    public void setId(Long id) {
    	this.id = id;
    }
    
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
	
	public Collection<Player> getFriends() {
    	return friends;
	}
	public void addFriend(Player friend) {
		this.friends.add(friend);
	}
}
