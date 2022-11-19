package xyz.mintydev.uhcdeathmatch.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.util.Cuboid;

public class Arena {

	private boolean used = false;
	
	private final boolean nodebuff;
	private final String name;
	private final String worldName;
	private final Location pos1;
	private final Location pos2;
	private final Location center;
	private final List<Location> playersPositions;

	private List<Player> teleported = new ArrayList<>();
	
	private Map<Location, BlockState> savedBlocks = new HashMap<>();
	
	public Arena(boolean nodebuff, String name, String worldName, Location pos1, Location pos2, Location center, List<Location> playersPositions) {
		this.name = name;
		this.nodebuff = nodebuff;
		this.worldName = worldName;
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.center = center;
		this.playersPositions = playersPositions;
	}
	
	public String getType() {
		return ChatColor.stripColor(this.name);
	}
	
	public Cuboid getCuboid() {
		return new Cuboid(this.getPos1(), this.getPos2());
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
	
	public Location getPos1() {
		return pos1;
	}
	
	public Location getPos2() {
		return pos2;
	}
	
	public Map<Location, BlockState> getSavedBlocks() {
		return savedBlocks;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isNodebuff() {
		return nodebuff;
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
