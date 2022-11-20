package xyz.mintydev.uhcdeathmatch.duels;

import java.util.Date;

import org.bukkit.scheduler.BukkitRunnable;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;

public class DuelRequestExpireRunnable extends BukkitRunnable {

	private final UHCDeathMatch main;
	
	public DuelRequestExpireRunnable(UHCDeathMatch main) {
		this.main = main;
	}
	
	@Override
	public void run() {
		final Date now = new Date();
		
		// check expiration
		
		for(DuelRequest request : main.getDuelManager().getRequests()) {
			final long difference = now.getTime() - request.getCreationDate().getTime();
			
			if(difference > 1000*60*2) {
				
				// more than two minutes
				main.getDuelManager().removeRequest(request);
				continue;
			}
		}
	}

}
