package xyz.mintydev.uhcdeathmatch;

import org.bukkit.plugin.java.JavaPlugin;

public class UHCDeathMatch extends JavaPlugin {

	@Override
	public void onEnable() {
		this.getLogger().info("Plugin is now enabled.");
	}
	
	@Override
	public void onDisable() {
		this.getLogger().info("Plugin is now disabled.");
	}
	
}
