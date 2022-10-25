package xyz.mintydev.uhcdeathmatch.core;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class UHCPlayer {

	private Player player;
	private PlayerState state;
	
	private Location previousLocation;
	private Location spawnLocation;
	
	private int kills = 0;
	
	public UHCPlayer(Player player) {
		this.player = player;
		this.state = PlayerState.LOBBY;
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public int getKills() {
		return kills;
	}
	
	public void addKill() {
		kills++;
	}
	
	public void setKills(int kills) {
		this.kills = kills;
	}
	
	public Location getSpawnLocation() {
		return spawnLocation;
	}
	
	public void setSpawnLocation(Location spawnLocation) {
		this.spawnLocation = spawnLocation;
	}
	
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
