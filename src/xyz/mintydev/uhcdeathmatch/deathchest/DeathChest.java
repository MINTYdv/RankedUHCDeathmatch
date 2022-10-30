package xyz.mintydev.uhcdeathmatch.deathchest;

import java.util.Date;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import xyz.mintydev.uhcdeathmatch.core.UHCGame;

public class DeathChest {

	private final UHCGame game;
	private final Player player;
	private final Material previousMat;
	private final Material previousMat2;
	private final List<Block> blocks;
	private final Date spawnedDate;
	private final List<ItemStack> content;
	
	public DeathChest(UHCGame game, Player player, List<Block> blocks, Date spawnedDate, Material previousMat, Material previousMat2, List<ItemStack> content) {
		this.game = game;
		this.player = player;
		this.blocks = blocks;
		this.spawnedDate = spawnedDate;
		this.previousMat = previousMat;
		this.previousMat2 = previousMat2;
		this.content = content;
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public List<Block> getBlocks() {
		return blocks;
	}
	
	public Material getPreviousMat2() {
		return previousMat2;
	}
	
	public List<ItemStack> getContent() {
		return content;
	}
	
	public UHCGame getGame() {
		return game;
	}
	
	public Material getPreviousMat() {
		return previousMat;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Date getSpawnedDate() {
		return spawnedDate;
	}
	
}
