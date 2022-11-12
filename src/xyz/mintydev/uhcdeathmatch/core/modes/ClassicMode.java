package xyz.mintydev.uhcdeathmatch.core.modes;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.UHCEnchant;
import xyz.mintydev.uhcdeathmatch.util.ItemBuilder;

public class ClassicMode extends UHCMode {

	public ClassicMode() {
		super(UHCModeType.CLASSIC, "classic_uhc", Lang.get("items.modes.uhc"), new ItemStack(Material.GOLDEN_APPLE, 1));
	}

	@Override
	public void giveKit(Player player) {
		final ItemStack sword = ItemBuilder.getEnchantedUnbreakableItem(Material.DIAMOND_SWORD,
				new UHCEnchant(Enchantment.DAMAGE_ALL, 2));
		
		player.getInventory().setItem(0, sword);
		player.getInventory().setItem(1, ItemBuilder.createItem(Material.FISHING_ROD, 1, null, null));
		
		player.getInventory().setItem(2, ItemBuilder.getEnchantedItem(Material.BOW,
				new UHCEnchant(Enchantment.ARROW_DAMAGE, 2)));
		player.getInventory().setItem(4, ItemBuilder.createItem(Material.GOLDEN_APPLE, 20, null, null));
		
		player.getInventory().setItem(4, ItemBuilder.createItem(Material.GOLDEN_APPLE, 20, null, null));
		player.getInventory().setItem(5, ItemBuilder.getGhead(4));
		
		player.getInventory().setItem(6, ItemBuilder.createItem(Material.WATER_BUCKET, 1, null, null));
		player.getInventory().setItem(33, ItemBuilder.createItem(Material.WATER_BUCKET, 1, null, null));
		player.getInventory().setItem(24, ItemBuilder.createItem(Material.WATER_BUCKET, 1, null, null));
		player.getInventory().setItem(7, ItemBuilder.createItem(Material.LAVA_BUCKET, 1, null, null));
		player.getInventory().setItem(34, ItemBuilder.createItem(Material.LAVA_BUCKET, 1, null, null));
		player.getInventory().setItem(25, ItemBuilder.createItem(Material.LAVA_BUCKET, 1, null, null));
		player.getInventory().setItem(16, ItemBuilder.createItem(Material.LAVA_BUCKET, 1, null, null));
		player.getInventory().setItem(8, ItemBuilder.createItem(Material.COBBLESTONE, 64, null, null));
		player.getInventory().setItem(35, ItemBuilder.createItem(Material.WOOD, 64, null, null));
		
		player.getInventory().setItem(9, ItemBuilder.createItem(Material.COOKED_BEEF, 64, null, null));
		player.getInventory().setItem(10, ItemBuilder.createItem(Material.DIAMOND_PICKAXE, 1, null, null));
		player.getInventory().setItem(18, ItemBuilder.createItem(Material.ARROW, 14, null, null));
		player.getInventory().setItem(19, ItemBuilder.createItem(Material.DIAMOND_AXE, 1, null, null));
	}
	
}
