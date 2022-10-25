package xyz.mintydev.uhcdeathmatch.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import xyz.mintydev.uhcdeathmatch.core.modes.UHCMode;

public class UHCGame {

	private final UHCMode mode;
	private GameState state;
	
	private List<Block> placedBlocks = new ArrayList<>();
	private List<Player> players = new ArrayList<>();
	private List<Player> alivePlayers = new ArrayList<>();
	
	private Arena arena;
	
	public UHCGame(UHCMode mode) {
		this.mode = mode;
		this.state = GameState.WAITING;
	}
	
	public Arena getArena() {
		return arena;
	}
	
	public void setArena(Arena arena) {
		this.arena = arena;
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public UHCMode getMode() {
		return mode;
	}
	
	public List<Block> getPlacedBlocks() {
		return placedBlocks;
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	public List<Player> getAlivePlayers() {
		return alivePlayers;
	}
	
	public GameState getState() {
		return state;
	}
	
	public void setState(GameState state) {
		this.state = state;
	}
	
}
