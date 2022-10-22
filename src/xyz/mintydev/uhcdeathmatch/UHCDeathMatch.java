package xyz.mintydev.uhcdeathmatch;

import org.bukkit.plugin.java.JavaPlugin;

import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.managers.ArenaManager;
import xyz.mintydev.uhcdeathmatch.managers.GameManager;
import xyz.mintydev.uhcdeathmatch.managers.PlayersManager;

public class UHCDeathMatch extends JavaPlugin {

	private static UHCDeathMatch instance;

	private PlayersManager playersManager;
	private GameManager gameManager;
	private ArenaManager arenaManager;
	
	@Override
	public void onEnable() {
		this.getLogger().info("Plugin is now enabled.");
		
		instance = this;
		new Lang(this);
		this.playersManager = new PlayersManager();
		this.gameManager = new GameManager(this);
		this.arenaManager = new ArenaManager(this);
	}
	
	@Override
	public void onDisable() {
		this.getLogger().info("Plugin is now disabled.");
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public ArenaManager getArenaManager() {
		return arenaManager;
	}
	
	public PlayersManager getPlayersManager() {
		return playersManager;
	}
	
	public GameManager getGameManager() {
		return gameManager;
	}
	
	public static UHCDeathMatch get() {
		return instance;
	}
	
}
