package nl.aronmandos.connect4.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Game {
	
	@Id
	@GeneratedValue
	private Long id = null;
	
	@ManyToOne
    private Player challenger = null;
	
	@ManyToOne
    private Player opponent = null;
	
	@ManyToOne
	private Player winner = null;

	@OneToOne
    private PlayingField playingField = null;
	
	// 0 = none, 1 = challenger, 2 = opponent
	private int playerOnTurn = 0;
	
	@Temporal(TemporalType.TIMESTAMP)
    private Date  inviteDate = null;
	
	@Temporal(TemporalType.TIMESTAMP)
    private Date  startDate = null;
	
	@Temporal(TemporalType.TIMESTAMP)
    private Date endDate = null;

    private Game() { }

    public Game(final Player challenger, final Player opponent, final PlayingField playingField, final int playerOnTurn, final Date inviteDate, final Date startDate, final Date endDate, final Player winner) {
        this.challenger = challenger;
        this.opponent = opponent;
        this.playingField = playingField;
        this.playerOnTurn = playerOnTurn;
        this.inviteDate = inviteDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.winner = winner;
    }
	
	
	public Long getId() {
		return id;
	}
	
	public Player getChallenger() {
		return challenger;
	}
	
	public Player getOpponent() {
		return opponent;
	}
	
	public PlayingField getPlayingField() {
		return playingField;
	}
	
	public int getPlayerOnTurn() {
		return playerOnTurn;
	}
	
	public void setPlayerOnTurn(int playerNumber) {
		this.playerOnTurn = playerNumber;
	}
	
	public Date getInviteDate() {
		return inviteDate;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Player getWinner() {
		return winner;
	}
	
	public void setWinner(Player winner) {
    	this.winner = winner;
	}
}
