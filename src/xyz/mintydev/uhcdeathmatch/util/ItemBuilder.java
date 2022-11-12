package xyz.mintydev.uhcdeathmatch.util;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.UHCEnchant;

public class ItemBuilder {

	public static ItemStack createItem(ItemStack base, int amount, String name, List<String> lore) {
		ItemStack result = base;
		result.setAmount(amount);
		
		ItemMeta meta = result.getItemMeta();
		if(name != null && name.length() > 0) {
			meta.setDisplayName(name);
		}
		if(lore != null) {
			meta.setLore(lore);
		}
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		
		result.setItemMeta(meta);
		return result;
	}
	
	public static ItemStack getPotion() {
        ItemStack potion = new ItemStack(Material.POTION, 1);
        Potion pot = new Potion(1);
        pot.setSplash(true);
        pot.setType(PotionType.INSTANT_HEAL);
        pot.setLevel(2);
        pot.apply(potion);
        return potion;
	}
	
	public static ItemStack getGhead(int amount) {
		ItemStack it = SkullCreator.itemFromUrl("https://textures.minecraft.net/texture/a24f3c846d552cbdc366d8751dd4bfabde60a3adad535c3620b1a0af5d3f553a");
		ItemMeta meta = it.getItemMeta();
		meta.setDisplayName(Lang.get("items.ghead"));
		it.setAmount(amount);
		it.setItemMeta(meta);
		return it;
	}
	
	public static ItemStack getEnchantedItem(Material mat, UHCEnchant... enchants) {
		ItemStack it = new ItemStack(mat, 1);
		ItemMeta meta = it.getItemMeta();
		for(UHCEnchant ench : enchants) {
			meta.addEnchant(ench.getEnchant(), ench.getLevel(), true);
		}
		it.setItemMeta(meta);
		return it;
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
