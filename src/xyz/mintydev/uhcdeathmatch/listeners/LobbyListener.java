package xyz.mintydev.uhcdeathmatch.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.PlayerState;
import xyz.mintydev.uhcdeathmatch.core.UHCPlayer;
import xyz.mintydev.uhcdeathmatch.core.gui.ModeSelectGUI;

public class LobbyListener implements Listener {

	private UHCDeathMatch main;
	
	public LobbyListener(UHCDeathMatch main) {
		this.main = main;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) return;
		final Player player = (Player) e.getWhoClicked();
		if(player.getGameMode() == GameMode.CREATIVE) return;
		
		final UHCPlayer uhcPlayer = main.getPlayersManager().getPlayer(player);
		if(uhcPlayer.getState() != PlayerState.LOBBY) return;
		
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		final Player player = e.getPlayer();
		if(player.getItemInHand() == null) return;
		if(player.getGameMode() == GameMode.CREATIVE) return;
		
		final UHCPlayer uhcPlayer = main.getPlayersManager().getPlayer(player);
		if(uhcPlayer.getState() != PlayerState.LOBBY) return;
		
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		// set lobby
		main.getGameManager().setLobby(player);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		final Player player = e.getPlayer();
		final ItemStack item = e.getItem();
		if(item == null || item.getType() == Material.AIR) return;
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;
		
		final UHCPlayer uhcPlayer = main.getPlayersManager().getPlayer(player);
		if(uhcPlayer.getState() != PlayerState.LOBBY) return;
		
		if(item.getType() == Material.DIAMOND_SWORD) {
			// open game select GUI
			main.getGuiManager().open(player, new ModeSelectGUI(main));
			return;
		}
	}
	
}
