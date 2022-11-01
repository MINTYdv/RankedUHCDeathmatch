package xyz.mintydev.uhcdeathmatch.core.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.data.EloPlayer;
import xyz.mintydev.uhcdeathmatch.util.ItemBuilder;
import xyz.mintydev.uhcdeathmatch.util.SkullCreator;
import xyz.mintydev.uhcdeathmatch.util.gui.UHCGUI;

public class EloGUI extends UHCGUI {
	
	private final Player target;

	public EloGUI(UHCDeathMatch main, Player target) {
		super(main, "game_select", Lang.get("gui.elo.title").replaceAll("%player%", target.getName()), 1);
		this.target = target;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void contents(Player player, Inventory inv) {
		
		final String name = Lang.get("gui.elo.item-name").replaceAll("%player%", target.getName());
		final ItemStack base = SkullCreator.itemFromName(target.getName());
		
		final EloPlayer ePlayer = main.getEloPlayersManager().getPlayer(target);

		List<String> lore = new ArrayList<>();
		for(String str : Lang.getList("gui.elo.item-lore")) {
			lore.add(str.replaceAll("%elo%", ePlayer.getElo()+""));
		}
		
		final ItemStack it = ItemBuilder.createItem(base, 1, name, lore);
		inv.setItem(4, it);
		
	}
	
}
