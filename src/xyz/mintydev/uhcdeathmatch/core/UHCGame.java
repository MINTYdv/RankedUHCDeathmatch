package xyz.mintydev.uhcdeathmatch.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import xyz.mintydev.uhcdeathmatch.core.modes.UHCMode;

public class UHCGame {

	private final UHCMode mode;
	private GameState state;
	
	private List<Player> players = new ArrayList<>();
	private List<Player> alivePlayers = new ArrayList<>();
	private String winnerName;
	
	private Arena arena;

	private int startTimer;
	
	public UHCGame(UHCMode mode) {
		this.mode = mode;
		this.state = GameState.WAITING;
	}
	
	public void broadcastMessage(String string) {
		for(Player player : players) {
			player.sendMessage(string);
		}
	}
	
	public String getWinnerName() {
		return winnerName;
	}
	
	public void setWinnerName(String winnerName) {
		this.winnerName = winnerName;
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
	
	public int getStartTimer() {
		return startTimer;
	}
	
	public void setStartTimer(int seconds) {
		this.startTimer = seconds;
	}
	
	public UHCMode getMode() {
		return mode;
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
