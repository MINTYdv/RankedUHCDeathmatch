package xyz.mintydev.uhcdeathmatch.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.Arena;
import xyz.mintydev.uhcdeathmatch.core.GameState;
import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.PlayerState;
import xyz.mintydev.uhcdeathmatch.core.UHCEnchant;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;
import xyz.mintydev.uhcdeathmatch.core.UHCPlayer;
import xyz.mintydev.uhcdeathmatch.util.ItemBuilder;

public class GameManager {

	private final UHCDeathMatch main;
	
	private List<UHCGame> games = new ArrayList<>();
	
	public GameManager(UHCDeathMatch main) {
		this.main = main;
		
		for(int i = 1; i <= 8; i++) {
			// create new game
			createNewGame();
		}
	}
	
	public UHCGame getGame(Player player) {
		for(UHCGame game : this.games) {
			if(!(game.getPlayers().contains(player))) continue;
			return game;
		}
		return null;
	}
	
	public void setLobby(Player player) {
		UHCPlayer uhcPlayer = main.getPlayersManager().getPlayer(player);
		uhcPlayer.setState(PlayerState.LOBBY);
		player.getInventory().clear();
		player.setFoodLevel(20);
		player.setHealth(player.getMaxHealth());
		
		// set items
		final ItemStack swordItem = ItemBuilder.createItem(Material.DIAMOND_SWORD, 1, Lang.get("items.sword"), new ArrayList<>());
		player.getInventory().setItem(0, swordItem);
	}
	
	public void joinGame(Player player, UHCGame game) {
		if(game == null || game.getState() != GameState.WAITING) return;
		if(game.getArena() == null) return;
		
		game.getPlayers().add(player);
		
		final UHCPlayer uPlayer = main.getPlayersManager().getPlayer(player);
		uPlayer.setState(PlayerState.PLAYING);
		uPlayer.setPreviousLocation(player.getLocation());
		
		// TODO tp player
		game.getArena().teleportPlayer(player);
		player.setFoodLevel(20);
		player.setHealth(player.getMaxHealth());
		player.setGameMode(GameMode.SURVIVAL);
		
		// give stuff
		player.getInventory().clear();

		player.getInventory().setItem(0, ItemBuilder.getEnchantedItem(Material.DIAMOND_SWORD,
				new UHCEnchant(Enchantment.DAMAGE_ALL, 2)));
		player.getInventory().setItem(1, ItemBuilder.createItem(Material.FISHING_ROD, 1, null, null));
		
		player.getInventory().setItem(2, ItemBuilder.createItem(Material.BOW, 1, null, null));
		player.getInventory().setItem(4, ItemBuilder.createItem(Material.GOLDEN_APPLE, 20, null, null));
		
		player.getInventory().setItem(4, ItemBuilder.createItem(Material.GOLDEN_APPLE, 20, null, null));
		player.getInventory().setItem(5, ItemBuilder.getGhead(4));
		
		player.getInventory().setItem(6, ItemBuilder.createItem(Material.WATER_BUCKET, 1, null, null));
		player.getInventory().setItem(33, ItemBuilder.createItem(Material.WATER_BUCKET, 1, null, null));
		player.getInventory().setItem(24, ItemBuilder.createItem(Material.WATER_BUCKET, 1, null, null));
		player.getInventory().setItem(7, ItemBuilder.createItem(Material.LAVA_BUCKET, 1, null, null));
		player.getInventory().setItem(34, ItemBuilder.createItem(Material.LAVA_BUCKET, 1, null, null));
		player.getInventory().setItem(25, ItemBuilder.createItem(Material.LAVA_BUCKET, 1, null, null));
		player.getInventory().setItem(16, ItemBuilder.createItem(Material.LAVA_BUCKET, 1, null, null));
		player.getInventory().setItem(8, ItemBuilder.createItem(Material.COBBLESTONE, 64, null, null));
		player.getInventory().setItem(35, ItemBuilder.createItem(Material.WOOD, 64, null, null));
		
		player.getInventory().setItem(9, ItemBuilder.createItem(Material.COOKED_BEEF, 64, null, null));
		player.getInventory().setItem(10, ItemBuilder.createItem(Material.DIAMOND_PICKAXE, 1, null, null));
		player.getInventory().setItem(18, ItemBuilder.createItem(Material.ARROW, 14, null, null));
		player.getInventory().setItem(19, ItemBuilder.createItem(Material.DIAMOND_AXE, 1, null, null));
		
		final ItemStack helmet = ItemBuilder.getEnchantedItem(Material.DIAMOND_HELMET,
				new UHCEnchant(Enchantment.PROTECTION_PROJECTILE, 1),
				new UHCEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3));
		final ItemStack chestplate = ItemBuilder.getEnchantedItem(Material.DIAMOND_CHESTPLATE,
				new UHCEnchant(Enchantment.PROTECTION_PROJECTILE, 1),
				new UHCEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3));
		final ItemStack leggings = ItemBuilder.getEnchantedItem(Material.DIAMOND_LEGGINGS,
				new UHCEnchant(Enchantment.PROTECTION_PROJECTILE, 1),
				new UHCEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3));
		final ItemStack boots = ItemBuilder.getEnchantedItem(Material.DIAMOND_BOOTS,
				new UHCEnchant(Enchantment.PROTECTION_PROJECTILE, 1),
				new UHCEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3));
		
		player.getInventory().setHelmet(helmet);
		player.getInventory().setChestplate(chestplate);
		player.getInventory().setLeggings(leggings);
		player.getInventory().setBoots(boots);
		
		// freeze player
	}
	
	public void leaveGame(Player player, UHCGame game) {
		final UHCPlayer uPlayer = main.getPlayersManager().getPlayer(player);
		uPlayer.setState(PlayerState.LOBBY);
		player.teleport(uPlayer.getPreviousLocation());
		
		if(game.getState() == GameState.WAITING) {
			game.getArena().removePlayer(player);
		}
	}
	
	public void startGame(UHCGame game) {
		if(game == null || game.getState() != GameState.WAITING) return;
		
		game.getAlivePlayers().addAll(game.getPlayers());
		
		// TODO unfreeze all players
		// enable damage
	}
	
	public void endGame(UHCGame game) {
		resetGame(game);
		
		for(Player player : game.getPlayers()) {
			setLobby(player);
		}
	}
	
	private void createNewGame() {
		UHCGame game = new UHCGame();
		this.games.add(game);
		resetGame(game);
	}

	public void resetGame(UHCGame game) {
		// set things
		if(game.getArena() != null) {
			game.getArena().setUsed(false);
			game.getArena().resetTeleportations();
		}
		game.setState(GameState.WAITING);
		
		// get arena
		Arena arena = main.getArenaManager().getAvailableArea();
		game.setArena(arena);
		if(arena != null) {
			arena.setUsed(true);
		}
		
		for(Block block : game.getPlacedBlocks()) {
			block.setType(Material.AIR);
		}
		
		// clear players
		game.getPlacedBlocks().clear();
		game.getPlayers().clear();
		game.getAlivePlayers().clear();
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public List<UHCGame> getGames() {
		return games;
	}
	
}
