package xyz.mintydev.uhcdeathmatch.core.modes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import xyz.mintydev.uhcdeathmatch.core.Lang;

public class ClassicMode extends UHCMode {

	public ClassicMode() {
		super("classic_uhc", Lang.get("items.modes.uhc"), new ItemStack(Material.GOLDEN_APPLE, 1));
	}

}
