package xyz.mintydev.uhcdeathmatch.core.modes;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.UHCEnchant;
import xyz.mintydev.uhcdeathmatch.util.ItemBuilder;

public class NodebuffMode extends UHCMode {

	public NodebuffMode() {
		super("nodebuff", Lang.get("items.modes.nodebuff"), ItemBuilder.getPotion());
	}

	@Override
	public void giveKit(Player player) {
        ItemStack potion = ItemBuilder.getPotion();

		for(int i = 1; i <= 35; i++) {
			player.getInventory().setItem(i, potion);
		}
		
		final ItemStack sword = ItemBuilder.getEnchantedUnbreakableItem(Material.DIAMOND_SWORD,
				new UHCEnchant(Enchantment.DAMAGE_ALL, 2));
		
		ItemMeta swordMeta = sword.getItemMeta();
		swordMeta.spigot().setUnbreakable(true);
		sword.setItemMeta(swordMeta);
		player.getInventory().setItem(0, sword);
		
		player.getInventory().setItem(1, ItemBuilder.createItem(Material.ENDER_PEARL, 16, null, null));
		player.getInventory().setItem(8, ItemBuilder.createItem(Material.COOKED_BEEF, 64, null, null));
	}
	
}
