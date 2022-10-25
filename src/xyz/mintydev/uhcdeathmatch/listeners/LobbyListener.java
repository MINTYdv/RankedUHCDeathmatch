package xyz.mintydev.uhcdeathmatch.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		// set lobby & create scoreboard
		main.getGameManager().setLobby(player);
		main.getScoreboardManager().createScoreboard(player);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		final Player player = e.getPlayer();
		final ItemStack item = e.getItem();
		if(item == null || item.getType() == Material.AIR) return;
		
		final UHCPlayer uhcPlayer = main.getPlayersManager().getPlayer(player);
		if(uhcPlayer.getState() != PlayerState.LOBBY) return;
		
		if(item.getType() == Material.DIAMOND_SWORD) {
			// open game select GUI
			main.getGuiManager().open(player, new ModeSelectGUI(main));
			return;
		}
	}
	
}
