package xyz.mintydev.uhcdeathmatch.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;

public class Arena {

	private boolean used = false;
	
	private final String name;
	private final String worldName;
	private final Location center;
	private final List<Location> playersPositions;

	private List<Player> teleported = new ArrayList<>();
	
	public Arena(String name, String worldName, Location center, List<Location> playersPositions) {
		this.name = name;
		this.worldName = worldName;
		this.center = center;
		this.playersPositions = playersPositions;
	}
	
	public void teleportPlayer(Player player) {
		if(teleported.contains(player)) return;
		
		final int index = teleported.size();
		
		teleported.add(player);
		final Location loc = playersPositions.get(index);
		player.teleport(loc);
		final UHCPlayer uPlayer = UHCDeathMatch.get().getPlayersManager().getPlayer(player);
		uPlayer.setSpawnLocation(loc);
	}
	
	public void removePlayer(Player player) {
		if(!(teleported.contains(player))) return;
		
		teleported.remove(player);
	}
	
	public void resetTeleportations() {
		teleported.clear();
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public String getName() {
		return name;
	}
	
	public List<Location> getPlayersPositions() {
		return playersPositions;
	}
	
	public Location getCenter() {
		return center;
	}
	
	public boolean isUsed() {
		return used;
	}
	
	public void setUsed(boolean used) {
		this.used = used;
	}
	
	public World getWorld() {
		return Bukkit.getWorld(worldName);
	}
	
}
