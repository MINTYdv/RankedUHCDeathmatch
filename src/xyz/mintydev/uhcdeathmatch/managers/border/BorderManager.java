package xyz.mintydev.uhcdeathmatch.managers.border;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.WorldBorder;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.Arena;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;

public class BorderManager implements Listener {

	private final UHCDeathMatch main;
	
	private final List<BorderStage> stages = new ArrayList<>();
	private Map<UHCGame, BorderStage> gamesStages = new HashMap<>();
	private Map<UHCGame, BukkitTask> runnables = new HashMap<>();
	private Map<UHCGame, BorderRunnable> borderRunnables = new HashMap<>();
	
	public BorderManager(UHCDeathMatch main) {
		this.main = main;
		
		// register stages
		stages.add(new BorderStage(0, 0, main.getConfig().getInt("settings.border.0.size")));
		stages.add(new BorderStage(1, main.getConfig().getInt("settings.border.1.timer"), main.getConfig().getInt("settings.border.1.size")));
		stages.add(new BorderStage(2, main.getConfig().getInt("settings.border.2.timer"), main.getConfig().getInt("settings.border.2.size")));
	}
	
	public void startGame(UHCGame game) {
		gamesStages.put(game, stages.get(0)); // first border stage
		final BorderRunnable rn = new BorderRunnable(main, game);
		borderRunnables.put(game, rn);
		runnables.put(game, rn.runTaskTimer(main, 0, 20));
		
		updateBorder(game);
	}
	
	public void updateBorder(UHCGame game) {
		final BorderStage stage = gamesStages.get(game);
		
		final Arena arena = game.getArena();
		WorldBorder border = arena.getWorld().getWorldBorder();
		border.setCenter(arena.getCenter());
		border.setSize(stage.getSize());
	}
	
	public void endGame(UHCGame game) {
		// cancel runnable
		if(runnables.containsKey(game)) {
			runnables.get(game).cancel();
			runnables.remove(game);
		}
		if(borderRunnables.containsKey(game)) {
			borderRunnables.remove(game);
		}
		if(gamesStages.containsKey(game)) {
			gamesStages.remove(game);
		}
		if(game.getArena() != null) {
			final Arena arena = game.getArena();
			WorldBorder border = arena.getWorld().getWorldBorder();
			border.reset();
		}
	}
	
	/*
	 * Getters & Setters
	 * */
	
	public Map<UHCGame, BorderRunnable> getBorderRunnables() {
		return borderRunnables;
	}
	
	public List<BorderStage> getStages() {
		return stages;
	}
	
	public Map<UHCGame, BorderStage> getGamesStages() {
		return gamesStages;
	}
	
	public Map<UHCGame, BukkitTask> getRunnables() {
		return runnables;
	}
	
}
