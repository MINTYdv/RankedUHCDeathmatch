package xyz.mintydev.uhcdeathmatch.runnables;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;

public class ScoreboardRunnable extends BukkitRunnable {

	@Override
	public void run() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			UHCDeathMatch.get().getScoreboardManager().updateScoreboard(player);
		}
	}

}
