package xyz.mintydev.uhcdeathmatch.util.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;

public class UHCGuiManager implements Listener {	

	private UHCDeathMatch main;

	private List<UHCOpenedGUI> openedGuis = new ArrayList<>();
	
	public UHCGuiManager(UHCDeathMatch main) {
		this.main = main;
		new GUIUpdateRunnable(main).runTaskTimer(main, 20L, 20L);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onClick(InventoryClickEvent event){

		final Player player = (Player) event.getWhoClicked();
		final Inventory inv = event.getInventory();
		ItemStack current = event.getCurrentItem();
		final UHCOpenedGUI openedGUI = getOpened(inv);

		if(event.getClickedInventory() == null) return;

		if(current == null) {
			if(event.getClick() == ClickType.NUMBER_KEY && openedGUI != null){
				if(openedGUI.getGui().isCancelClick()){
					event.setCancelled(true);
					event.setResult(Event.Result.DENY);
				};
			}
			if(openedGUI != null && !(event.getClickedInventory() instanceof PlayerInventory)) openedGUI.getGui().onEndDrag(player, inv, inv.getItem(event.getSlot()), event, event.getSlot());

			return;
		}

		if(openedGUI == null) {
			if(player.getOpenInventory().getTopInventory().getType() != InventoryType.CRAFTING) return;
			if(player.getInventory().getHelmet() != null) return;
			if(event.getClick() != ClickType.SHIFT_LEFT) return;
			if(current.getType() == Material.AIR) return;
			return;
		}
		if(event.getClick() == ClickType.NUMBER_KEY && openedGUI.getGui().isCancelClick() || event.getClick() == ClickType.CREATIVE) {
			event.setCancelled(true);
			event.setResult(Event.Result.DENY);
			return;
		}

		final UHCGUI gui = openedGUI.getGui();

		event.setCancelled(gui.isCancelClick());
		if(event.getClickedInventory() == player.getInventory()) {
			gui.onPersonalInventoryClick(player, inv, current, event.getSlot(), event.getClick());
			if(!gui.isCancelDrag() && event.getClick() == ClickType.LEFT) {
				if(player.getItemOnCursor().getType() == Material.AIR) gui.onStartDrag(player, inv, current, event, event.getSlot());
			}
		} else {
			if(event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT) {
				gui.onRightClick(player, inv, current, event.getSlot());
			} else if(event.getClick() == ClickType.MIDDLE){
				gui.onMouseWheelClick(player, inv, current, event.getSlot());
			} else {
				gui.onClick(player, inv, current, event.getSlot());
			}
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Inventory inv = e.getInventory();
		Player player = (Player) e.getPlayer();
		
		if(getOpened(inv) == null) return;
		
		UHCGUI gui = getOpened(inv).getGui();
		openedGuis.remove(getOpened(inv));
		
		if(gui.getCloseListener() != null) {
			gui.getCloseListener().accept(player);
		}
	}
	
	public UHCOpenedGUI getOpened(Inventory inventory) {
		for(UHCOpenedGUI openedGui : this.getOpenedGuis()) {
			if(openedGui.getInventory().equals(inventory)) {
				return openedGui;
			}
		}
		return null;
	}
	
	public UHCGUI open(Player player, UHCGUI menu) {
		
		final Inventory inv = Bukkit.createInventory(null, menu.getSize()*9, menu.getTitle());
		menu.contents(player, inv);
		
		openedGuis.add(new UHCOpenedGUI(player, inv, menu));
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				player.openInventory(inv);
			}
			
		}.runTaskLater(main, 1);
		return menu;
	}

	/* 
	 * Getters & Setters
	 * */

	public List<UHCOpenedGUI> getOpenedGuis() {
		return openedGuis;
	}
	
}