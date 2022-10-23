package xyz.mintydev.uhcdeathmatch.core;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class UHCPlayer {

	private Player player;
	private PlayerState state;
	
	private Location previousLocation;
	
	public UHCPlayer(Player player) {
		this.player = player;
		this.state = PlayerState.LOBBY;
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public Location getPreviousLocation() {
		return previousLocation;
	}
	
	public void setPreviousLocation(Location previousLocation) {
		this.previousLocation = previousLocation;
	}
	
	public void setState(PlayerState state) {
		this.state = state;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public PlayerState getState() {
		return state;
	}
	
}
