package xyz.mintydev.uhcdeathmatch.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.PlayerState;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;
import xyz.mintydev.uhcdeathmatch.core.UHCPlayer;

public class GameListener implements Listener {

	private UHCDeathMatch main;
	
	public GameListener(UHCDeathMatch main) {
		this.main = main;
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		final Player player = e.getPlayer();
		final UHCPlayer uPlayer = main.getPlayersManager().getPlayer(player);
		if(uPlayer.getState() != PlayerState.PLAYING) return;
		
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		
		final UHCGame game = main.getGameManager().getGame(player);
		if(game == null) return;
		
		main.getGameManager().leaveGame(player, null);
	}
}
