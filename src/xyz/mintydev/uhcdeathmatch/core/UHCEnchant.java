package xyz.mintydev.uhcdeathmatch.core;

import org.bukkit.enchantments.Enchantment;

public class UHCEnchant {

	private final Enchantment enchant;
	private final int level;
	
	public UHCEnchant(Enchantment enchant, int level) {
		this.enchant = enchant;
		this.level = level;
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public Enchantment getEnchant() {
		return enchant;
	}
	
	public int getLevel() {
		return level;
	}
	
}
