package xyz.mintydev.uhcdeathmatch.data;

import java.util.UUID;

import org.bukkit.entity.Player;

public class EloPlayer {

	private Player player;
	private UUID uuid;
	private String username;
	private int elo;
	
	public EloPlayer() {}
	
	public EloPlayer(Player player, UUID uuid, String username, int elo) {
		this.player = player;
		this.uuid = uuid;
		this.username = username;
		this.elo = elo;
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}
	
	public void setElo(int elo) {
		this.elo = elo;
	}
	
	public void addElo(int toAdd) {
		this.elo += toAdd;
	}
	
	public void removeElo(int toRemove) {
		this.elo -= toRemove;
	}
	
	public int getElo() {
		return elo;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	
}
