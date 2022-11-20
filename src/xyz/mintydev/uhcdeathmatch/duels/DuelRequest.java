package xyz.mintydev.uhcdeathmatch.duels;

import java.util.Date;

import org.bukkit.entity.Player;

import xyz.mintydev.uhcdeathmatch.core.modes.UHCMode;

public class DuelRequest {

	private Player askingPlayer;
	private Player opponent;
	private UHCMode mode;
	private Date creationDate;
	
	public DuelRequest(Player askingPlayer, Player opponent, UHCMode mode, Date creationDate) {
		this.askingPlayer = askingPlayer;
		this.opponent = opponent;
		this.mode = mode;
		this.creationDate = creationDate;
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public UHCMode getMode() {
		return mode;
	}

	public Player getAskingPlayer() {
		return askingPlayer;
	}
	
	public Player getOpponent() {
		return opponent;
	}
	
}
