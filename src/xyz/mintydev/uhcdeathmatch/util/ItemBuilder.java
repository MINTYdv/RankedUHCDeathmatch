package xyz.mintydev.uhcdeathmatch.util;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ItemBuilder {

	public static ItemStack createItem(ItemStack base, int amount, String name, List<String> lore) {
		ItemStack result = base;
		result.setAmount(amount);
		
		ItemMeta meta = result.getItemMeta();
		meta.setDisplayName(name);
		if(lore != null) {
			meta.setLore(lore);
		}
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		
		result.setItemMeta(meta);
		return result;
	}
	
	public static ItemStack createItem(Material material, int amount, String name, List<String> lore) {
		return createItem(new ItemStack(material, amount), amount, name, lore);
	}
	
	public static ItemStack getDyedPiece(Material material, Color color) {
		ItemStack result = new ItemStack(material, 1);
		ItemMeta meta = result.getItemMeta();

		LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
		armorMeta.setColor(color);
		
		result.setItemMeta(armorMeta);
		return result;
	}
	
}
