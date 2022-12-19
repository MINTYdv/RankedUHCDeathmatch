package xyz.mintydev.uhcdeathmatch.core.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.GameState;
import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.PlayerState;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;
import xyz.mintydev.uhcdeathmatch.core.UHCPlayer;
import xyz.mintydev.uhcdeathmatch.core.modes.UHCMode;
import xyz.mintydev.uhcdeathmatch.util.ItemBuilder;
import xyz.mintydev.uhcdeathmatch.util.gui.UHCGUI;

public class GameSelectGUI extends UHCGUI {
	
	private final UHCMode mode;
	private Map<Integer, UHCGame> games = new HashMap<>();
	
	public GameSelectGUI(UHCMode mode, UHCDeathMatch main) {
		super(main, "game_select", Lang.get("gui.game-select.title"), 1);
		this.mode = mode;
	}

	@Override
	public void contents(Player player, Inventory inv) {
		for(int i = 0; i < main.getGameManager().getGames(mode).size(); i++) {
			final UHCGame game = main.getGameManager().getGames(mode).get(i);
			
			List<String> lore = new ArrayList<>();
			for(String str : Lang.getList("gui.game-select.game-lore")) {
				
				if(game.getArena() == null) {
					str = str.replaceAll("%arena%", "§cCouldn't find an available arena.");
				} else {
					str = str.replaceAll("%arena%", main.getArenaManager().getMap(game.getArena()).getDisplayName() + " - " + game.getArena().getName());
				}
				
				str = str.replaceAll("%status%", game.getState().getDisplayName());
				str = str.replaceAll("%players%", game.getPlayers().size()+"");
				str = str.replaceAll("&", "§");
				lore.add(str);
			}
			lore.add("");
			lore.add(game.getState() == GameState.WAITING ? Lang.get("gui.game-select.lore-join") : Lang.get("gui.game-select.lore-no"));
			
			final ItemStack wool = new ItemStack(Material.WOOL, 1, game.getState() == GameState.WAITING ? (byte)13 : (byte) 14);
			final ItemStack it = ItemBuilder.createItem(wool, 1, Lang.get("items.game").replace("%number%", i+1+""), lore);
			inv.setItem(i, it);
			games.put(i, game);
		}
	}
	
	@Override
	public void onClick(Player player, Inventory inv, ItemStack current, int slot) {
		if(games.containsKey(slot)) {
			final UHCGame game = games.get(slot);
			
			if(game.getState() != GameState.WAITING) {
				player.sendMessage(Lang.get("gui.game-select.messages.cant-join"));
				return;
			}
			
			if(main.getGameManager().areGamesStopped()) {
				player.sendMessage(Lang.get("gui.game-select.messages.games-stopped"));
				return;
			}
			
			final UHCPlayer uhcPlayer = main.getPlayersManager().getPlayer(player);
			if(!(uhcPlayer.getState() == PlayerState.LOBBY)) return;
			
			if(game.getArena() == null) {
				player.sendMessage(Lang.get("gui.game-select.messages.no-arena"));
				return;
			}
			
			if(game.getPlayers().size() == 4) {
				player.sendMessage(Lang.get("gui.game-select.messages.game-full"));
				return;
			}
			
			// join the game
			player.closeInventory();
			main.getGameManager().joinGame(player, game);
			
		}
	}
	
}
