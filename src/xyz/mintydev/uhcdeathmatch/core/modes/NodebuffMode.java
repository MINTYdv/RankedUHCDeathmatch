package xyz.mintydev.uhcdeathmatch.core.modes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import xyz.mintydev.uhcdeathmatch.core.Lang;

public class NodebuffMode extends UHCMode {

	public NodebuffMode() {
		super("nodebuff", Lang.get("items.modes.nodebuff"), new ItemStack(Material.POTION, 1));
	}

}
