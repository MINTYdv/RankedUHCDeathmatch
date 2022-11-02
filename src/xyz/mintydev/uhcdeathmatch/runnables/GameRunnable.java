package xyz.mintydev.uhcdeathmatch.runnables;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.GameState;
import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;
import xyz.mintydev.uhcdeathmatch.core.modes.UHCMode;
import xyz.mintydev.uhcdeathmatch.util.TitleUtil;
import xyz.mintydev.uhcdeathmatch.util.UHCUtil;

public class GameRunnable extends BukkitRunnable {

	private final UHCDeathMatch main;
	
	public GameRunnable(UHCDeathMatch main) {
		this.main = main;
	}
	
	private void handleGame(UHCGame game) {
		// WAITING
		if(game.getState() == GameState.WAITING) {

			final boolean canStart = game.getPlayers().size() >= 2;
			if(!canStart && game.getStartTimer() > 0) {
				game.setStartTimer(-1);
				
				game.broadcastMessage(Lang.get("game.errors.not-enough-players"));
			}

			if(!canStart) {
				for(Player player : game.getPlayers()) {
					// send actionbar
					UHCUtil.sendActionText(player, Lang.get("misc.leave-actionbar"));
				}
			}
			
			// start the timer
			if(canStart && game.getStartTimer() < 0) game.setStartTimer(main.getConfig().getInt("settings.timer"));

			if(canStart && game.getStartTimer() > 0) {
				// remove one second from timer
				game.setStartTimer(game.getStartTimer()-1);
				
				for(Player player : game.getPlayers()) {
					// send actionbar
					UHCUtil.sendActionText(player, Lang.get("misc.timer-actionbar").replace("%timer%", game.getStartTimer()+""));
				}
				
				if(game.getStartTimer() > 0 && game.getStartTimer() <= 5) {
					for(Player player : game.getPlayers()) {
						// send title
						TitleUtil.sendTitle(player, 0, 20, 0, "", Lang.get("misc.timer-actionbar").replace("%timer%", game.getStartTimer()+""));
						player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1, 1);
					}
				}
			}
			
			if(canStart && game.getStartTimer() == 0) {
				// start game
				main.getGameManager().startGame(game);
				
				for(Player player : game.getPlayers()) {
					// send title
					TitleUtil.sendTitle(player, 0, 20, 0, Lang.get("misc.title-fight"), "");
				}
				return;
			}
		}
		
		// RUNNING
		if(game.getAlivePlayers().size() == 1) {
			final Player winner = game.getAlivePlayers().get(0);
			main.getGameManager().winGame(game, winner);
			return;
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
