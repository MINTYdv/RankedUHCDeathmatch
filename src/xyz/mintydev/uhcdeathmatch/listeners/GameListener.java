package xyz.mintydev.uhcdeathmatch.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.GameState;
import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.PlayerState;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;
import xyz.mintydev.uhcdeathmatch.core.UHCPlayer;
import xyz.mintydev.uhcdeathmatch.util.ItemBuilder;
import xyz.mintydev.uhcdeathmatch.util.UHCUtil;

public class GameListener implements Listener {

	private UHCDeathMatch main;
	
	public GameListener(UHCDeathMatch main) {
		this.main = main;
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		final Player victim = e.getEntity();
		final Player killer = victim.getKiller();
		if(killer == null) return;
		
		final UHCGame game = main.getGameManager().getGame(killer);
		if(game == null || game.getState() != GameState.RUNNING) return;
		if(main.getGameManager().getGame(victim) == null) return;
		
		List<ItemStack> drops = new ArrayList<>();
		drops.addAll(e.getDrops());
		
		main.getGameManager().playerKill(game, victim, killer, drops);
		e.setDeathMessage(null);
		game.broadcastMessage(Lang.get("misc.kill").replaceAll("%victim%", victim.getName()).replaceAll("%killer%", killer.getName()));

		e.getDrops().clear();
	}
	
	@EventHandler
	public void onGheadUse(PlayerInteractEvent e) {
		final Player player = e.getPlayer();
		final ItemStack item = e.getItem();
		if(item == null) return;
		final UHCPlayer uPlayer = main.getPlayersManager().getPlayer(e.getPlayer());
		if(uPlayer.getState() != PlayerState.PLAYING) return;
		
		final UHCGame game = main.getGameManager().getGame(player);
		if(game == null || game.getState() != GameState.RUNNING) return;
		
		if(item.equals(ItemBuilder.getGhead(item.getAmount()))) {
			// used ghead
			UHCUtil.removeOne(player, item);
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 12*20, 1));
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 4*20, 2));
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		final Player player = e.getPlayer();
		
		final UHCPlayer uPlayer = main.getPlayersManager().getPlayer(player);
		if(uPlayer.getState() != PlayerState.PLAYING) return;
		
		final UHCGame game = main.getGameManager().getGame(player);
		if(game == null) return;
		
		if(game.getState() != GameState.WAITING) return;
		
		final Location initial = uPlayer.getSpawnLocation();
		if(e.getTo().getX() != initial.getX()
				|| e.getTo().getY() > initial.getY()
				|| e.getTo().getZ() != initial.getZ()) {
			player.teleport(new Location(initial.getWorld(), initial.getX(), initial.getY(), initial.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
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
	public void onDrop(PlayerDropItemEvent e) {
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
		
		final UHCGame game = main.getGameManager().getGame(player);
		if(game == null) return;
		
		if(game.getState() != GameState.RUNNING) {
			e.setCancelled(true);
			return;
		}
		
		final Block block = e.getBlock();
		Bukkit.broadcastMessage(block.getType().toString());
		if(block.getType().toString().toUpperCase().contains("LEAVES")
				|| block.getType().toString().toUpperCase().contains("LOG")
				|| block.getType().toString().toUpperCase().contains("SNOW")
				|| block.getType() == Material.LONG_GRASS) {

			game.getBrokenBlocks().put(block.getLocation(), block.getType());
			
		} else {
			e.setCancelled(true);
		}
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
	
	@EventHandler(priority = EventPriority.LOW)
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		
		final UHCGame game = main.getGameManager().getGame(player);
		if(game == null) return;
		
		main.getGameManager().leaveGame(player, game);
		
		final UHCPlayer uPlayer = main.getPlayersManager().getPlayer(player);
		if(uPlayer.getState() == PlayerState.PLAYING) {
			if(uPlayer.getLastDamager() != null) {
				// add elo to last damager
				main.getGameManager().removeDeathElo(player);
			}
		}
	}
}
