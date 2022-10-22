package xyz.mintydev.uhcdeathmatch.util.gui;

import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;

public class UHCGUI
{
	private String title;
	private String id;
	private int size;
	protected UHCGUI previousMenu;
	
	protected UHCDeathMatch main;
	private Consumer<Player> closeListener;
	
	private boolean updateGui = false;

	private boolean cancelClick = true;

	private boolean cancelDrag = true;
	
	public UHCGUI(UHCDeathMatch main, String id, String title, int size, UHCGUI previousMenu) {
		this(main, id, title, size);
		this.previousMenu = previousMenu;
	}
	
	public UHCGUI(UHCDeathMatch main, String id, String title, int size) {
		this.title = title;
		this.id = id;
		this.size = size;
		this.main = main;
	}

	public UHCGUI(UHCDeathMatch main, String id, String title, int size, boolean cancelClick) {
		this.title = title;
		this.id = id;
		this.size = size;
		this.main = main;
		this.cancelClick = cancelClick;
	}

	public UHCGUI(UHCDeathMatch main, String id, String title, int size, boolean cancelClick, boolean cancelDrag) {
		this.title = title;
		this.id = id;
		this.size = size;
		this.main = main;
		this.cancelClick = cancelClick;
		this.cancelDrag = cancelDrag;
	}

	public void onPersonalInventoryClick(Player player, Inventory inv, ItemStack current, int slot, ClickType clickType) {}
	public void contents(Player player, Inventory inv) { }
	public void updateContents(Player player, Inventory inv) { }
	public void onRightClick(Player player, Inventory inv, ItemStack current, int slot) { }
	public void onMouseWheelClick(Player player, Inventory inv, ItemStack current, int slot) { }
	public void onClick(Player player, Inventory inv, ItemStack current, int slot) { }

	public void onStartDrag(Player player, Inventory inv, ItemStack clickedItem, InventoryClickEvent event, int slot) {}
	public void onEndDrag(Player player, Inventory inv, ItemStack oldItem, InventoryClickEvent event, int slot) {}
	
	public UHCGUI getPreviousMenu() {
		return previousMenu;
	}
	
	public UHCGUI setCloseListener(Consumer<Player> closeListener) {
		this.closeListener = closeListener;
		return this;
	}
	
	public String getId() {
		return id;
	}
	
	public Consumer<Player> getCloseListener() {
		return closeListener;
	}
	
	public int getSize() {
		return size;
	}
	
	public String getTitle() {
		return title;
	}
	public void setUpdateGui(boolean updateGui) {
		this.updateGui = updateGui;
	}
	
	public boolean doUpdateGui() {
		return updateGui;
	}

	public boolean isCancelClick(){
		return cancelClick;
	}

	public boolean isCancelDrag() {
		return cancelDrag;
	}
}