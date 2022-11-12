package xyz.mintydev.uhcdeathmatch.core.modes;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class UHCMode {

	private final UHCModeType type;
	private final String id;
	private final String displayName;
	private final ItemStack icon;
	
	public UHCMode(UHCModeType type, String id, String displayName, ItemStack icon) {
		this.type = type;
		this.id = id;
		this.displayName = displayName;
		this.icon = icon;
	}
	
	public void giveKit(Player player) { }
	
	/* 
	 * Getters & Setters
	 * */
	
	public UHCModeType getType() {
		return type;
	}
	
	public String getId() {
		return id;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public ItemStack getIcon() {
		return icon;
	}
	
}
