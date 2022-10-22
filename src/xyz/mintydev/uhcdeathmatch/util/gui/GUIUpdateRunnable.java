package xyz.mintydev.uhcdeathmatch.util.gui;

import org.bukkit.scheduler.BukkitRunnable;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;

public class GUIUpdateRunnable extends BukkitRunnable {

	private final UHCDeathMatch main;
	
	public GUIUpdateRunnable(UHCDeathMatch main) {
		this.main = main;
	}
	
	@Override
	public void run() {
		
		for(UHCOpenedGUI opened : main.getGuiManager().getOpenedGuis()) {
			if(!(opened.getGui().doUpdateGui())) continue;
			
			opened.getGui().updateContents(opened.getPlayer(), opened.getInventory());
		}
		
	}

}
