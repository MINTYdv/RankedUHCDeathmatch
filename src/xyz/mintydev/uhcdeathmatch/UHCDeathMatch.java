package xyz.mintydev.uhcdeathmatch;

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.plugin.java.JavaPlugin;

import xyz.mintydev.uhcdeathmatch.cmd.EloCommand;
import xyz.mintydev.uhcdeathmatch.cmd.GamesStopCommand;
import xyz.mintydev.uhcdeathmatch.cmd.LeaveCommand;
import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;
import xyz.mintydev.uhcdeathmatch.core.modes.UHCMode;
import xyz.mintydev.uhcdeathmatch.data.EloPlayersManager;
import xyz.mintydev.uhcdeathmatch.deathchest.DeathChestManager;
import xyz.mintydev.uhcdeathmatch.duels.DuelManager;
import xyz.mintydev.uhcdeathmatch.leaderboard.LeaderboardManager;
import xyz.mintydev.uhcdeathmatch.listeners.CoreListener;
import xyz.mintydev.uhcdeathmatch.listeners.GameListener;
import xyz.mintydev.uhcdeathmatch.listeners.LobbyListener;
import xyz.mintydev.uhcdeathmatch.managers.ArenaManager;
import xyz.mintydev.uhcdeathmatch.managers.GameManager;
import xyz.mintydev.uhcdeathmatch.managers.PlayersManager;
import xyz.mintydev.uhcdeathmatch.managers.ScoreboardManager;
import xyz.mintydev.uhcdeathmatch.managers.border.BorderManager;
import xyz.mintydev.uhcdeathmatch.runnables.ScoreboardRunnable;
import xyz.mintydev.uhcdeathmatch.util.gui.UHCGuiManager;

public class UHCDeathMatch extends JavaPlugin {

	private static UHCDeathMatch instance;

	private PlayersManager playersManager;
	private EloPlayersManager eloPlayersManager;
	private GameManager gameManager;
	private ArenaManager arenaManager;
	private ScoreboardManager scoreboardManager;
	private DeathChestManager deathChestManager;
	private BorderManager borderManager;
	private UHCGuiManager guiManager;
	private LeaderboardManager leaderboardManager;
	private DuelManager duelManager;
	
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
		this.borderManager = new BorderManager(this);
		this.gameManager = new GameManager(this);
		this.scoreboardManager = new ScoreboardManager(this);
		this.deathChestManager = new DeathChestManager(this);
		this.leaderboardManager = new LeaderboardManager(this);
		this.duelManager = new DuelManager(this);
		
		this.guiManager = new UHCGuiManager(this);
		
		// listeners
		this.getServer().getPluginManager().registerEvents(new LobbyListener(this), this);
		this.getServer().getPluginManager().registerEvents(new GameListener(this), this);
		this.getServer().getPluginManager().registerEvents(new CoreListener(this), this);
		this.getServer().getPluginManager().registerEvents(this.guiManager, this);
		this.getServer().getPluginManager().registerEvents(this.borderManager, this);
		this.getServer().getPluginManager().registerEvents(this.deathChestManager, this);
		
		// commands
		registerCommands();
		
		// runnables
		new ScoreboardRunnable().runTaskTimer(this, 0, 10);
	}
	
	private void registerCommands() {
		getCommand("leave").setExecutor(new LeaveCommand(this));
		getCommand("gamestop").setExecutor(new GamesStopCommand(this));
		getCommand("elo").setExecutor(new EloCommand(this));
	}

	@Override
	public void onDisable() {
		this.eloPlayersManager.saveAll();
		// end all games
		if(this.getGameManager().getGames() != null && this.getGameManager().getGames().entrySet().size() > 0) {
			for(Entry<UHCMode, List<UHCGame>> entry : this.getGameManager().getGames().entrySet()) {
				for(UHCGame game : entry.getValue())
					gameManager.endGame(game);
			}
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
	
	public BorderManager getBorderManager() {
		return borderManager;
	}
	
	public EloPlayersManager getEloPlayersManager() {
		return eloPlayersManager;
	}
	
	public LeaderboardManager getLeaderboardManager() {
		return leaderboardManager;
	}
	
	public GameManager getGameManager() {
		return gameManager;
	}
	
	public DuelManager getDuelManager() {
		return duelManager;
	}
	
	public static UHCDeathMatch get() {
		return instance;
	}
	
}
