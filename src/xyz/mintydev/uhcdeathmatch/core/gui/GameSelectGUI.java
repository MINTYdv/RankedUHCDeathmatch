package xyz.mintydev.uhcdeathmatch.core.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.GameState;
import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;
import xyz.mintydev.uhcdeathmatch.util.ItemBuilder;
import xyz.mintydev.uhcdeathmatch.util.gui.UHCGUI;

public class GameSelectGUI extends UHCGUI {
	
	public GameSelectGUI(UHCDeathMatch main) {
		super(main, "game_select", Lang.get("gui.game-select.title"), 1);
	}

	@Override
	public void contents(Player player, Inventory inv) {
		for(int i = 0; i < main.getGameManager().getGames().size(); i++) {
			final UHCGame game = main.getGameManager().getGames().get(i);
			
			List<String> lore = new ArrayList<>();
			for(String str : Lang.getList("gui.game-select.game-lore")) {
				str = str.replaceAll("%status%", game.getState().getDisplayName());
				str = str.replaceAll("%players%", game.getPlayers().size()+"");
				str = str.replaceAll("&", "§");
				lore.add(str);
			}
			lore.add("");
			lore.add(game.getState() == GameState.WAITING ? Lang.get("gui.game-select.lore-join") : Lang.get("gui.game-select.lore-no"));
			
			final ItemStack wool = new ItemStack(Material.WOOL, 1, game.getState() == GameState.WAITING ? (byte)14 : (byte) 13);
			final ItemStack it = ItemBuilder.createItem(wool, 1, Lang.get("items.game").replace("%amount%", i+1+""), lore);
			inv.setItem(i, it);
		}
	}
	
}
