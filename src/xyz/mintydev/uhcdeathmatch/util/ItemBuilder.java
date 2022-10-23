package xyz.mintydev.uhcdeathmatch.util;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

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
	
	public static ItemStack getGhead(int amount) {
		ItemStack it = getCustomHead("PhantomTupac", amount, "");
		return it;
	}
	
	public static ItemStack getCustomHead(String name, int amount, String url) {

        ItemStack skull = new ItemStack(Material.SKULL_ITEM, amount);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        assert skullMeta != null;

        if (url.length() < 16) {

          skullMeta.setOwner(url);

          skullMeta.setDisplayName(name);

           skull.setItemMeta(skullMeta);
           return skull;
        }

        StringBuilder s_url = new StringBuilder();
        s_url.append("https://textures.minecraft.net/texture/").append(url); // We get the texture link.

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null); // We create a GameProfile

       // We get the bytes from the texture in Base64 encoded that comes from the Minecraft-URL.
        byte[] data = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", s_url.toString()).getBytes());

      // We set the texture property in the GameProfile.
        gameProfile.getProperties().put("textures", new Property("textures", new String(data)));


        skullMeta.setDisplayName(name); // We set a displayName to the skull
        skull.setItemMeta(skullMeta);

        return skull; //Finally, you have the custom head!

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
