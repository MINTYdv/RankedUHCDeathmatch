package xyz.mintydev.uhcdeathmatch.runnables;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.GameState;
import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;
import xyz.mintydev.uhcdeathmatch.core.modes.UHCMode;
import xyz.mintydev.uhcdeathmatch.util.UHCUtil;

public class GameRunnable extends BukkitRunnable {

	private final UHCDeathMatch main;
	
	public GameRunnable(UHCDeathMatch main) {
		this.main = main;
	}
	
	private void handleGame(UHCGame game) {
		// WAITING
		if(game.getState() == GameState.WAITING) {
			for(Player player : game.getPlayers()) {
				// send actionbar
				UHCUtil.sendActionText(player, Lang.get("misc.leave-actionbar"));
			}

			final boolean canStart = game.getPlayers().size() >= 2;
			if(!canStart && game.getStartTimer() > 0) {
				game.setStartTimer(-1);
				
				game.broadcastMessage(Lang.get("game.errors.not-enough-players"));
			}
			
			// start the timer
			if(canStart && game.getStartTimer() < 0) game.setStartTimer(30);

			if(canStart && game.getStartTimer() > 0) {
				// remove one second from timer
				game.setStartTimer(game.getStartTimer()-1);
			}
			
			if(canStart && game.getStartTimer() == 0) {
				// start game
				Bukkit.broadcastMessage("starting the game");
				main.getGameManager().startGame(game);
				return;
			}
		}
		
		// RUNNING
		
		// FINISHED
	}
	
	@Override
	public void run() {
		for(Player player : Bukkit.getOnlinePlayers()) main.getScoreboardManager().updateScoreboard(player);
		
		for(UHCMode mode : main.getGameManager().getModes()) {
			for(UHCGame game : main.getGameManager().getGames(mode)) {
				handleGame(game);
			}
		}

	}

}
