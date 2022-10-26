package xyz.mintydev.uhcdeathmatch.util;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class UHCUtil {

	public static void sendActionText(Player player, String message){
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
	
	public static void removeOne(Player player, ItemStack item) {
		if(item.getAmount() == 1) {
			player.getInventory().remove(item);
		} else {
			item.setAmount(item.getAmount()-1);
		}
	}
	
	// NATTY
	
}
