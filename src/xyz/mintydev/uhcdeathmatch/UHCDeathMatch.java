package xyz.mintydev.uhcdeathmatch;

import org.bukkit.plugin.java.JavaPlugin;

import xyz.mintydev.uhcdeathmatch.core.Lang;

public class UHCDeathMatch extends JavaPlugin {

	private static UHCDeathMatch instance;

	@Override
	public void onEnable() {
		this.getLogger().info("Plugin is now enabled.");
		
		instance = this;
		new Lang(this);
	}
	
	@Override
	public void onDisable() {
		this.getLogger().info("Plugin is now disabled.");
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public static UHCDeathMatch get() {
		return instance;
	}
	
}
