package xyz.mintydev.uhcdeathmatch.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.GameMode;
import org.bukkit.Location;
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
import xyz.mintydev.uhcdeathmatch.core.modes.ClassicMode;
import xyz.mintydev.uhcdeathmatch.core.modes.NodebuffMode;
import xyz.mintydev.uhcdeathmatch.core.modes.UHCMode;
import xyz.mintydev.uhcdeathmatch.runnables.GameRunnable;
import xyz.mintydev.uhcdeathmatch.util.ItemBuilder;

public class GameManager {

	private final UHCDeathMatch main;
	
	private List<UHCMode> modes = new ArrayList<>();
	private Map<UHCMode, List<UHCGame>> games = new HashMap<>();
	
	public GameManager(UHCDeathMatch main) {
		this.main = main;
		
		// create modes
		modes.add(new NodebuffMode());
		modes.add(new ClassicMode());
		
		// create games
		for(UHCMode mode : modes) {
			for(int i = 1; i <= 8; i++) {
				createNewGame(mode);
			}
		}
		System.out.println("Created " + this.games.size() + " games");
		
		// register runnable
		new GameRunnable(main).runTaskTimer(main, 0, 20);
	}
	
	public int getAmountofIngamePlayers() {
		int res = 0;
		for(UHCMode mode : this.modes) {
			for(UHCGame game : getGames(mode)) {
				res += game.getPlayers().size();
			}
		}
		return res;
	}
	
	public UHCGame getGame(Player player) {
		for(Entry<UHCMode, List<UHCGame>> entry : games.entrySet()) {
			for(UHCGame game : entry.getValue()) {
				if(!(game.getPlayers().contains(player))) continue;
				return game;
			}
		}
		return null;
	}
	
	public void setLobby(Player player) {
		UHCPlayer uhcPlayer = main.getPlayersManager().getPlayer(player);
		uhcPlayer.setState(PlayerState.LOBBY);
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		
		player.setWalkSpeed(0.2f);
		
		player.setFoodLevel(20);
		player.setHealth(player.getMaxHealth());
		if(uhcPlayer.getPreviousLocation() != null) {
			player.teleport(uhcPlayer.getPreviousLocation());
		}
		
		// set items
		final ItemStack swordItem = ItemBuilder.createItem(Material.DIAMOND_SWORD, 1, Lang.get("items.sword"), new ArrayList<>());
		player.getInventory().setItem(0, swordItem);
	}
	
	public void joinGame(Player player, UHCGame game) {
		if(game == null || game.getState() != GameState.WAITING) return;
		if(game.getArena() == null) return;
		
		game.getPlayers().add(player);
		
		final UHCPlayer uPlayer = main.getPlayersManager().getPlayer(player);
		uPlayer.setKills(0);
		uPlayer.setState(PlayerState.PLAYING);
		uPlayer.setPreviousLocation(player.getLocation());
		
		// TODO tp player
		game.getArena().teleportPlayer(player);
		player.setFoodLevel(20);
		player.setHealth(player.getMaxHealth());
		player.setGameMode(GameMode.SURVIVAL);
		
		player.setWalkSpeed(0.0f);
		
		// give stuff
		player.getInventory().clear();

		game.getMode().giveKit(player);
		
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
	}
	
	public void leaveGame(Player player, UHCGame game) {
		final UHCPlayer uPlayer = main.getPlayersManager().getPlayer(player);
		uPlayer.setState(PlayerState.LOBBY);
		player.teleport(uPlayer.getPreviousLocation());
		
		if(game.getState() == GameState.WAITING) {
			game.getArena().removePlayer(player);
		}
		
		game.getPlayers().remove(player);
		if(game.getAlivePlayers().contains(player)) {
			game.getAlivePlayers().remove(player);
		}
		
		setLobby(player);
	}
	
	public void startGame(UHCGame game) {
		if(game == null || game.getState() != GameState.WAITING) return;
	
		for(Player player : game.getPlayers()) {
			player.setWalkSpeed(0.2f);
		}
		
		game.setState(GameState.RUNNING);
		game.getAlivePlayers().addAll(game.getPlayers());
	}
	
	public void endGame(UHCGame game) {
		for(Player player : game.getPlayers()) {
			setLobby(player);
		}
		
		resetGame(game);
	}
	
	private void createNewGame(UHCMode mode) {
		if(!(games.containsKey(mode))) {
			games.put(mode, new ArrayList<>());
		}
		List<UHCGame> list = games.get(mode);
		
		UHCGame game = new UHCGame(mode);
		list.add(game);
		games.remove(mode);
		games.put(mode, list);
		resetGame(game);
	}

	public void resetGame(UHCGame game) {
		// set things
		if(game.getArena() != null) {
			game.getArena().setUsed(false);
			game.getArena().resetTeleportations();
		}
		game.setState(GameState.WAITING);
		game.setStartTimer(-1);
		
		// get arena
		Arena arena = main.getArenaManager().getAvailableArea();
		game.setArena(arena);
		if(arena != null) {
			arena.setUsed(true);
		}
		
		for(Block block : game.getPlacedBlocks()) {
			block.setType(Material.AIR);
		}
		
		for(Entry<Location, Material> entry : game.getBrokenBlocks().entrySet()) {
			entry.getKey().getWorld().getBlockAt(entry.getKey()).setType(entry.getValue());
		}
		
		// clear players
		game.getPlacedBlocks().clear();
		game.getBrokenBlocks().clear();
		game.getPlayers().clear();
		game.getAlivePlayers().clear();
	}
	
	public List<UHCGame> getGames(UHCMode mode){
		return games.get(mode);
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public List<UHCMode> getModes() {
		return modes;
	}

	public Map<UHCMode, List<UHCGame>> getGames() {
		return games;
	}
	
}
