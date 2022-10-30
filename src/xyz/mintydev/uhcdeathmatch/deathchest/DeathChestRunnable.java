package xyz.mintydev.uhcdeathmatch.deathchest;

import java.util.Date;

import org.bukkit.scheduler.BukkitRunnable;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;
import xyz.mintydev.uhcdeathmatch.core.modes.UHCMode;

public class DeathChestRunnable extends BukkitRunnable {

	private final UHCDeathMatch main;
	
	public DeathChestRunnable(UHCDeathMatch main) {
		this.main = main;
	}
	
	@Override
	public void run() {
		
		for(UHCMode mode : main.getGameManager().getModes()) {
			for(UHCGame game : main.getGameManager().getGames(mode)) {
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

}
