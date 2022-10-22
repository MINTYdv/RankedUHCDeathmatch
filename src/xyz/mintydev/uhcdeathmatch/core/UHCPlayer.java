package xyz.mintydev.uhcdeathmatch.core;

import org.bukkit.entity.Player;

public class UHCPlayer {

	private Player player;
	private PlayerState state;
	
	public UHCPlayer(Player player) {
		this.player = player;
		this.state = PlayerState.LOBBY;
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public Player getPlayer() {
		return player;
	}
	
	public PlayerState getState() {
		return state;
	}
	
}
