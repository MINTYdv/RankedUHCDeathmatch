package xyz.mintydev.uhcdeathmatch.core.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.modes.UHCModeType;
import xyz.mintydev.uhcdeathmatch.data.EloPlayer;
import xyz.mintydev.uhcdeathmatch.util.ItemBuilder;
import xyz.mintydev.uhcdeathmatch.util.SkullCreator;
import xyz.mintydev.uhcdeathmatch.util.gui.UHCGUI;

public class EloGUI extends UHCGUI {
	
	private final EloPlayer target;

	public EloGUI(UHCDeathMatch main, EloPlayer target) {
		super(main, "game_select", Lang.get("gui.elo.title").replaceAll("%player%", target.getUsername()), 1);
		this.target = target;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void contents(Player player, Inventory inv) {
		
		final String name = Lang.get("gui.elo.item-name").replaceAll("%player%", target.getUsername());
		final ItemStack base = SkullCreator.itemFromName(target.getUsername());
		
		List<String> lore = new ArrayList<>();
		for(String str : Lang.getList("gui.elo.item-lore")) {
			str = str.replaceAll("%elo_CLASSIC%", target.getElo(UHCModeType.CLASSIC)+"");
			str = str.replaceAll("%elo_NODEBUFF%", target.getElo(UHCModeType.NODEBUFF)+"");
			
			lore.add(str);
		}
		
		final ItemStack it = ItemBuilder.createItem(base, 1, name, lore);
		inv.setItem(4, it);
		
	}
	
}
