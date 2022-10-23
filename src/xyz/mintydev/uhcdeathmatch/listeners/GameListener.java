package xyz.mintydev.uhcdeathmatch.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;

public class GameListener implements Listener {

	private UHCDeathMatch main;
	
	public GameListener(UHCDeathMatch main) {
		this.main = main;
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		
		final UHCGame game = main.getGameManager().getGame(player);
		if(game == null) return;
		
		main.getGameManager().leaveGame(player, null);
	}
}
