package xyz.mintydev.uhcdeathmatch.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.GameState;
import xyz.mintydev.uhcdeathmatch.core.PlayerState;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;
import xyz.mintydev.uhcdeathmatch.core.UHCPlayer;

public class GameListener implements Listener {

	private UHCDeathMatch main;
	
	public GameListener(UHCDeathMatch main) {
		this.main = main;
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		final Player player = e.getPlayer();
		final UHCPlayer uPlayer = main.getPlayersManager().getPlayer(e.getPlayer());
		if(uPlayer.getState() != PlayerState.PLAYING) return;
		
		final UHCGame game = main.getGameManager().getGame(player);
		if(game == null) return;
		
		final Location to = e.getTo();
		final Location from = e.getFrom();
		
		if(game.getState() != GameState.RUNNING) {
			player.setVelocity(new Vector().zero());
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(!(e.getEntity() instanceof Player)) return;
		final Player player = (Player) e.getEntity();
		
		final UHCPlayer uPlayer = main.getPlayersManager().getPlayer(player);
		if(uPlayer.getState() != PlayerState.PLAYING) return;
		
		final UHCGame game = main.getGameManager().getGame(player);
		if(game == null) return;
		
		if(game.getState() != GameState.RUNNING) {
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		final Player player = e.getPlayer();
		final UHCPlayer uPlayer = main.getPlayersManager().getPlayer(player);
		if(uPlayer.getState() != PlayerState.PLAYING) return;
		
		final UHCGame game = main.getGameManager().getGame(player);
		if(game == null) return;
		
		if(game.getState() != GameState.RUNNING) {
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		final Player player = e.getPlayer();
		final UHCPlayer uPlayer = main.getPlayersManager().getPlayer(player);
		if(uPlayer.getState() != PlayerState.PLAYING) return;
		
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		final Player player = e.getPlayer();
		final UHCPlayer uPlayer = main.getPlayersManager().getPlayer(player);
		if(uPlayer.getState() != PlayerState.PLAYING) return;
		
		final UHCGame game = main.getGameManager().getGame(player);
		if(game == null) return;
		
		if(game.getState() != GameState.RUNNING) {
			e.setCancelled(true);
			return;
		}
		
		game.getPlacedBlocks().add(e.getBlock());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		
		final UHCGame game = main.getGameManager().getGame(player);
		if(game == null) return;
		
		main.getGameManager().leaveGame(player, game);
	}
}
