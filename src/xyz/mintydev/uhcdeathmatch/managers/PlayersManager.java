package xyz.mintydev.uhcdeathmatch.managers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import xyz.mintydev.uhcdeathmatch.core.UHCPlayer;

public class PlayersManager {

	private Map<Player, UHCPlayer> players = new HashMap<>();
	
	public UHCPlayer getPlayer(Player player) {
		if(!(this.players.containsKey(player))) {
			players.put(player, new UHCPlayer(player));
		}
		
		return players.get(player);
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public Map<Player, UHCPlayer> getPlayers() {
		return players;
	}
	
}
