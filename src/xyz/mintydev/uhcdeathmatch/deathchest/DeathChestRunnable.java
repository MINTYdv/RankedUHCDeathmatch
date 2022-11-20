package xyz.mintydev.uhcdeathmatch.deathchest;

import java.util.Date;

import org.bukkit.scheduler.BukkitRunnable;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;

public class DeathChestRunnable extends BukkitRunnable {

	private final UHCDeathMatch main;
	
	public DeathChestRunnable(UHCDeathMatch main) {
		this.main = main;
	}
	
	@Override
	public void run() {
		if(main.getGameManager().getAllGames() == null || main.getGameManager().getAllGames().size() == 0) return;
		
		for(UHCGame game : main.getGameManager().getAllGames()) {
			if(main.getDeathChestManager().getChests(game) == null) continue;
			
			final Date now = new Date();
			for(DeathChest chest : main.getDeathChestManager().getChests(game)) {
				
				final long difference = now.getTime() - chest.getSpawnedDate().getTime();
				if(difference >= 30*1000) {
					// remove chest
					main.getDeathChestManager().removeChest(chest);
					continue;
				}
				
			}
		}
	}

}
