package xyz.mintydev.uhcdeathmatch.core.gui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.modes.UHCMode;
import xyz.mintydev.uhcdeathmatch.util.ItemBuilder;
import xyz.mintydev.uhcdeathmatch.util.gui.UHCGUI;

public class DuelModeSelectGUI extends UHCGUI {

	private final int[] slots = {3,5,4,2,6};
	private Map<Integer, UHCMode> modes = new HashMap<>();
	
	private final Player target;
	
	public DuelModeSelectGUI(UHCDeathMatch main, Player target) {
		super(main, "duel_mode_select", Lang.get("commands.duel.gui.title"), 1);
		this.target = target;
	}

	@Override
	public void contents(Player player, Inventory inv) {

		int i = 0;
		for(UHCMode mode : main.getGameManager().getModes()) {
			final ItemStack it = ItemBuilder.createItem(mode.getIcon(), 1, mode.getDisplayName(), null);
			final int slot = slots[i];
			inv.setItem(slot, it);
			i++;
			modes.put(slot, mode);
		}

	}
	
	@Override
	public void onClick(Player player, Inventory inv, ItemStack current, int slot) {
		if(modes.containsKey(slot)) {
			final UHCMode mode = modes.get(slot);
			
			// create a duel request
			main.getDuelManager().createRequest(player, target, mode);
			player.closeInventory();
		}
	}
	
}
