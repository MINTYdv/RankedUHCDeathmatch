package xyz.mintydev.uhcdeathmatch;

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.plugin.java.JavaPlugin;

import xyz.mintydev.uhcdeathmatch.cmd.LeaveCommand;
import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;
import xyz.mintydev.uhcdeathmatch.core.modes.UHCMode;
import xyz.mintydev.uhcdeathmatch.data.EloPlayersManager;
import xyz.mintydev.uhcdeathmatch.deathchest.DeathChestManager;
import xyz.mintydev.uhcdeathmatch.listeners.CoreListener;
import xyz.mintydev.uhcdeathmatch.listeners.GameListener;
import xyz.mintydev.uhcdeathmatch.listeners.LobbyListener;
import xyz.mintydev.uhcdeathmatch.managers.ArenaManager;
import xyz.mintydev.uhcdeathmatch.managers.GameManager;
import xyz.mintydev.uhcdeathmatch.managers.PlayersManager;
import xyz.mintydev.uhcdeathmatch.managers.ScoreboardManager;
import xyz.mintydev.uhcdeathmatch.util.gui.UHCGuiManager;

public class UHCDeathMatch extends JavaPlugin {

	private static UHCDeathMatch instance;

	private PlayersManager playersManager;
	private EloPlayersManager eloPlayersManager;
	private GameManager gameManager;
	private ArenaManager arenaManager;
	private ScoreboardManager scoreboardManager;
	private DeathChestManager deathChestManager;
	
	private UHCGuiManager guiManager;
	
	@Override
	public void onEnable() {
		this.getLogger().info("Plugin is now enabled.");
		
		saveDefaultConfig();
		
		// managers
		instance = this;
		new Lang(this);
		this.playersManager = new PlayersManager();
		this.eloPlayersManager = new EloPlayersManager(this);
		this.arenaManager = new ArenaManager(this);
		this.gameManager = new GameManager(this);
		this.scoreboardManager = new ScoreboardManager(this);
		this.deathChestManager = new DeathChestManager(this);
		
		this.guiManager = new UHCGuiManager(this);
		
		// listeners
		this.getServer().getPluginManager().registerEvents(new LobbyListener(this), this);
		this.getServer().getPluginManager().registerEvents(new GameListener(this), this);
		this.getServer().getPluginManager().registerEvents(new CoreListener(this), this);
		this.getServer().getPluginManager().registerEvents(this.guiManager, this);
		this.getServer().getPluginManager().registerEvents(this.deathChestManager, this);
		
		// commands
		registerCommands();
	}
	
	private void registerCommands() {
		getCommand("leave").setExecutor(new LeaveCommand(this));
	}

	@Override
	public void onDisable() {
		// end all games
		for(Entry<UHCMode, List<UHCGame>> entry : this.getGameManager().getGames().entrySet()) {
			for(UHCGame game : entry.getValue())
				gameManager.endGame(game);
		}
		
		this.getLogger().info("Plugin is now disabled.");
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public UHCGuiManager getGuiManager() {
		return guiManager;
	}
	
	public ArenaManager getArenaManager() {
		return arenaManager;
	}
	
	public DeathChestManager getDeathChestManager() {
		return deathChestManager;
	}
	
	public PlayersManager getPlayersManager() {
		return playersManager;
	}
	
	public ScoreboardManager getScoreboardManager() {
		return scoreboardManager;
	}
	
	public EloPlayersManager getEloPlayersManager() {
		return eloPlayersManager;
	}
	
	public GameManager getGameManager() {
		return gameManager;
	}
	
	public static UHCDeathMatch get() {
		return instance;
	}
	
}
