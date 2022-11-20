package xyz.mintydev.uhcdeathmatch.duels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.Lang;

public class DuelRequestExpireRunnable extends BukkitRunnable {

	private final UHCDeathMatch main;
	
	public DuelRequestExpireRunnable(UHCDeathMatch main) {
		this.main = main;
	}
	
	@Override
	public void run() {
		final Date now = new Date();
		
		// check expiration
		
		List<DuelRequest> toRemove = new ArrayList<>();
		
		for(DuelRequest request : main.getDuelManager().getRequests()) {
			final long difference = now.getTime() - request.getCreationDate().getTime();
			
			if(difference > 1000*60*2) {
				
				// more than two minutes
				if(request.getAskingPlayer() != null && request.getAskingPlayer().isOnline()) {
					request.getAskingPlayer().sendMessage(Lang.get("commands.duel.messages.expired-request").replaceAll("%player%", request.getOpponent().getName()));
				}
				toRemove.add(request);
				continue;
			}
		}
		
		toRemove.forEach(tr -> main.getDuelManager().removeRequest(tr));
	}

}
