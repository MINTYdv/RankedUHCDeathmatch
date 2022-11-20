package xyz.mintydev.uhcdeathmatch.listeners;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.data.EloPlayer;
import xyz.mintydev.uhcdeathmatch.duels.DuelRequest;

public class CoreListener implements Listener {

	private final UHCDeathMatch main;
	
	public CoreListener(UHCDeathMatch main) {
		this.main = main;
	}
		
	@EventHandler
    public void onPlayerRegainHealth(EntityRegainHealthEvent event) {
		if(!(event.getEntity() instanceof Player)) return;
		
        if(event.getRegainReason() == RegainReason.SATIATED || event.getRegainReason() == RegainReason.REGEN)
            event.setCancelled(true);
    }
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		final Player player = e.getPlayer();
		
		final EloPlayer ePlayer = main.getEloPlayersManager().getPlayer(player);
		ePlayer.setUsername(player.getName());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onQuit(PlayerQuitEvent e) {
		final Player player = e.getPlayer();
		EloPlayer ePlayer = main.getEloPlayersManager().getPlayer(player);

		// delete player's duel requests
		for(DuelRequest request : main.getDuelManager().getSentRequests(player)) {
			main.getDuelManager().removeRequest(request);
		}
		
		if(ePlayer == null) return;

		// save elo data
		try {
			main.getEloPlayersManager().save(ePlayer);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
}
