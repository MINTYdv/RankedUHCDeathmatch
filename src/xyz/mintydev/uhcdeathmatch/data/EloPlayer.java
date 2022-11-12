package xyz.mintydev.uhcdeathmatch.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.modes.UHCModeType;

public class EloPlayer {

	private Player player;
	private UUID uuid;
	private String username;
	private Map<UHCModeType, Integer> elos = new HashMap<>();
	
	public EloPlayer() {}
	
	public EloPlayer(Player player, UUID uuid, String username, Map<UHCModeType, Integer> elos) {
		this.player = player;
		this.uuid = uuid;
		this.username = username;
		this.elos = elos;
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
	
	public void setElo(UHCModeType type, int elo) {
		elos.remove(type);
		elos.put(type, elo);
	}
	
	public void addElo(UHCModeType type, int toAdd) {
		getElo(type);
		
		int base = getElo(type);
		elos.remove(type);
		base += toAdd;
		
		elos.put(type, base);
	}
	
	public void removeElo(UHCModeType type, int toRemove) {
		getElo(type);
		
		int base = getElo(type);
		elos.remove(type);
		base -= toRemove;
		if(base < 500) base = 500;
		
		elos.put(type, base);
	}
	
	public int getElo(UHCModeType type) {
		if(!(this.elos.containsKey(type))){
			elos.put(type, UHCDeathMatch.get().getConfig().getInt("elo.default"));
		}
		
		return elos.get(type);
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	
}
