package xyz.mintydev.uhcdeathmatch.core.modes;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.UHCEnchant;
import xyz.mintydev.uhcdeathmatch.util.ItemBuilder;

public class NodebuffMode extends UHCMode {

	public NodebuffMode() {
		super("nodebuff", Lang.get("items.modes.nodebuff"), new ItemStack(Material.POTION, 1));
	}

	@Override
	public void giveKit(Player player) {
        ItemStack potion = new ItemStack(Material.POTION, 1);
        Potion pot = new Potion(1);
        pot.setSplash(true);
        pot.setType(PotionType.INSTANT_HEAL);
        pot.setLevel(2);
        pot.apply(potion);

		for(int i = 1; i <= 35; i++) {
			player.getInventory().setItem(i, potion);
		}
		
		player.getInventory().setItem(0, ItemBuilder.getEnchantedItem(Material.DIAMOND_SWORD,
				new UHCEnchant(Enchantment.DAMAGE_ALL, 2)));
		player.getInventory().setItem(1, ItemBuilder.createItem(Material.ENDER_PEARL, 16, null, null));
		player.getInventory().setItem(8, ItemBuilder.createItem(Material.COOKED_BEEF, 64, null, null));
	}
	
}
