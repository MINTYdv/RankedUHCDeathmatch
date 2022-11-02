package xyz.mintydev.uhcdeathmatch.deathchest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.GameState;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;
import xyz.mintydev.uhcdeathmatch.util.ItemBuilder;

public class DeathChestManager implements Listener {

	private final UHCDeathMatch main;
	
	private Map<UHCGame, List<DeathChest>> chests = new HashMap<>();

	public DeathChestManager(UHCDeathMatch main) {
		this.main = main;
		
		// register runnable
		new DeathChestRunnable(main).runTaskTimer(main, 0, 20);
	}
	
	public void spawnDeathChest(Player player, Location location, List<ItemStack> drops) {
		final UHCGame game = main.getGameManager().getGame(player);
		if(game == null || game.getState() != GameState.RUNNING) return;
		
		// spawn chest
		final Material previousMat = location.getBlock().getType();
		Location secondLoc = new Location(location.getWorld(), location.getX()+1, location.getY(), location.getZ());
		final Material previousMat2 = secondLoc.getBlock().getType();
		
		drops.add(new ItemStack(Material.GOLDEN_APPLE));
		drops.add(ItemBuilder.getGhead(1));
		
		List<Block> blocks = new ArrayList<>();
		blocks.add(location.getBlock());
		blocks.add(secondLoc.getBlock());
		
		DeathChest chest = new DeathChest(game, player, blocks, new Date(), previousMat, previousMat2, drops);
		addChest(game, chest);
		
		// set block
		
        Block block1 = location.getBlock();
        Block block2 = secondLoc.getBlock();
        
        block1.setType(Material.CHEST);
        block2.setType(Material.CHEST);
    
        new BukkitRunnable() {

			@Override
			public void run() {
		        Chest chest1 = (Chest) location.getBlock().getState();
		        int i = 0;
		        for(ItemStack it : drops) {
		        	chest1.getInventory().setItem(i, it);
		        	i++;
		        }
		        chest1.update();
			}
        	
        }.runTask(main);
        
	}
	
	public void resetChests(UHCGame game) {
		if(getChests(game) == null || getChests(game).size() == 0) return;
		
		final List<DeathChest> list = getChests(game);
		for(DeathChest chest : list) {
			removeChest(chest);
		}
		chests.remove(game);
	}
	
	public void removeChest(DeathChest chest) {
		final UHCGame game = chest.getGame();
		
		List<DeathChest> res = new ArrayList<>();
		if(chests.containsKey(game)) {
			res.addAll(getChests(game));
		}
		res.remove(chest);
		chests.put(game, res);
		
		chest.getBlocks().get(0).setType(chest.getPreviousMat());
		chest.getBlocks().get(1).setType(chest.getPreviousMat2());
	}

	private void addChest(UHCGame game, DeathChest chest) {
		List<DeathChest> res = new ArrayList<>();
		if(chests.containsKey(game)) {
			res.addAll(getChests(game));
			chests.remove(game);
		}
		res.add(chest);
		chests.put(game, res);
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public Map<UHCGame, List<DeathChest>> getChests() {
		return chests;
	}
	
	public List<DeathChest> getChests(UHCGame game){
		return this.chests.get(game);
	}
	
}
