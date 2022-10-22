package xyz.mintydev.uhcdeathmatch.core;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Arena {

	private boolean used = false;
	
	private final String worldName;
	private final List<Location> playersPositions;
	
	public Arena(String worldName, List<Location> playersPositions) {
		this.worldName = worldName;
		this.playersPositions = playersPositions;
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public List<Location> getPlayersPositions() {
		return playersPositions;
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
