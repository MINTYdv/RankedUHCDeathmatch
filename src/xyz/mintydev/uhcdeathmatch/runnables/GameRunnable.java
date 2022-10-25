package xyz.mintydev.uhcdeathmatch.runnables;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.GameState;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;
import xyz.mintydev.uhcdeathmatch.core.modes.UHCMode;

public class GameRunnable extends BukkitRunnable {

	private final UHCDeathMatch main;
	
	public GameRunnable(UHCDeathMatch main) {
		this.main = main;
	}
	
	private void handleGame(UHCGame game) {
		
		if(game.getState() == GameState.WAITING) {
			for(Player player : game.getPlayers()) {
				// send actionbar
				
			}
		}
		
	}
	
	@Override
	public void run() {
		for(UHCMode mode : main.getGameManager().getModes()) {
			for(UHCGame game : main.getGameManager().getGames(mode)) {
				handleGame(game);
			}
		}

	}

}
