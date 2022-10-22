package xyz.mintydev.uhcdeathmatch.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class UHCGame {

	private GameState state;
	
	private List<Player> players;
	private List<Player> alivePlayers;
	
	private Arena arena;
	
	public UHCGame() {
		this.state = GameState.WAITING;
		this.players = new ArrayList<>();
		this.alivePlayers = new ArrayList<>();
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
