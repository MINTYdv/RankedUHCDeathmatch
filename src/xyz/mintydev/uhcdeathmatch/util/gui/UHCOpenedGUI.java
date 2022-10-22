package xyz.mintydev.uhcdeathmatch.util.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class UHCOpenedGUI {

	private final Player player;
	private final Inventory inventory;
	private final UHCGUI gui;
	
	public UHCOpenedGUI(Player player, Inventory inventory, UHCGUI gui) {
		this.player = player;
		this.inventory = inventory;
		this.gui = gui;
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public Player getPlayer() {
		return player;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public UHCGUI getGui() {
		return gui;
	}
	
}
