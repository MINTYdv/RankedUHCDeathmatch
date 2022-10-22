package xyz.mintydev.uhcdeathmatch.core.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.util.gui.UHCGUI;

public class GameSelectGUI extends UHCGUI {
	
	public GameSelectGUI(UHCDeathMatch main) {
		super(main, "game_select", Lang.get("gui.game-select.title"), 1);
	}

	@Override
	public void contents(Player player, Inventory inv) {
		
	}
	
}
