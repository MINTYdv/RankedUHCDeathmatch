package xyz.mintydev.uhcdeathmatch.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

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
import xyz.mintydev.uhcdeathmatch.core.modes.UHCModeType;
import xyz.mintydev.uhcdeathmatch.data.EloPlayer;
import xyz.mintydev.uhcdeathmatch.runnables.GameRunnable;
import xyz.mintydev.uhcdeathmatch.util.ItemBuilder;
import xyz.mintydev.uhcdeathmatch.util.TitleUtil;

public class GameManager {

	private final UHCDeathMatch main;
	
	private List<UHCMode> modes = new ArrayList<>();
	private Map<UHCMode, List<UHCGame>> games = new HashMap<>();
	
	private boolean gamesStopped = false;
	
	public GameManager(UHCDeathMatch main) {
		this.main = main;
		this.gamesStopped = false;
		
		new BukkitRunnable() {

			@Override
			public void run() {
				// create modes
				modes.add(new ClassicMode());
				modes.add(new NodebuffMode());
				
				// create games
				for(UHCMode mode : modes) {
					for(int i = 1; i <= main.getConfig().getInt("settings.games-per-mode"); i++) {
						createNewGame(mode);
					}
				}
				System.out.println("Created " + games.size() + " games");
				
				// register runnable
				new GameRunnable(main).runTaskTimer(main, 0, 20);
			}

		}.runTask(main);

	}
	
	public void addKillElo(UHCModeType type, Player player) {
		final EloPlayer ePlayer = main.getEloPlayersManager().getPlayer(player);
		ePlayer.addElo(type, main.getConfig().getInt("settings.elo.kill"));
		player.sendMessage(Lang.get("elo-messages.kill"));
	}
	
	public void addWinElo(UHCModeType type, Player player) {
		final EloPlayer ePlayer = main.getEloPlayersManager().getPlayer(player);
		ePlayer.addElo(type, main.getConfig().getInt("settings.elo.win"));
		player.sendMessage(Lang.get("elo-messages.win"));
	}
	
	public void removeDeathElo(UHCModeType type, Player player) {
		final EloPlayer ePlayer = main.getEloPlayersManager().getPlayer(player);
		ePlayer.removeElo(type, main.getConfig().getInt("settings.elo.death"));
		player.sendMessage(Lang.get("elo-messages.death"));
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
		
		uhcPlayer.setLastDamager(null);
		player.setFoodLevel(20);
		player.setHealth(player.getMaxHealth());
		if(uhcPlayer.getPreviousLocation() != null) {
			player.teleport(uhcPlayer.getPreviousLocation());
		}
		
		if(player.hasPotionEffect(PotionEffectType.REGENERATION)) {
			player.removePotionEffect(PotionEffectType.REGENERATION);
		}
		if(player.hasPotionEffect(PotionEffectType.ABSORPTION)) {
			player.removePotionEffect(PotionEffectType.ABSORPTION);
		}
		if(player.hasPotionEffect(PotionEffectType.SPEED)) {
			player.removePotionEffect(PotionEffectType.SPEED);
		}
		
		// set items
		final ItemStack swordItem = ItemBuilder.createItem(Material.DIAMOND_SWORD, 1, Lang.get("items.sword"), new ArrayList<>());
		player.getInventory().setItem(0, swordItem);
	}
	
	public void playerKill(UHCGame game, Player victim, Player killer, List<ItemStack> drops) {
		if(!(game.getAlivePlayers().contains(victim))) return;
		
		game.getAlivePlayers().remove(victim);
		
		TitleUtil.sendTitle(victim, 0, 20*3, 10, Lang.get("titles.death.title"), Lang.get("titles.death.subtitle"));
		victim.playSound(victim.getLocation(), Sound.VILLAGER_HIT, 1, 1);
		if(killer != null) {
			killer.playSound(victim.getLocation(), Sound.VILLAGER_HIT, 1, 1);
		}

		// set spectator
		final UHCPlayer uPlayer = main.getPlayersManager().getPlayer(victim);
		uPlayer.setState(PlayerState.SPECTATOR);
		
		if(killer != null) {
			// add kill to player
			final UHCPlayer uKiller = main.getPlayersManager().getPlayer(killer);
			uKiller.addKill();
			addKillElo(game.getMode().getType(), killer);
		}
		removeDeathElo(game.getMode().getType(), victim);

		// set spec
		new BukkitRunnable() {

			@Override
			public void run() {
				// spectator
				victim.spigot().respawn();
				victim.setGameMode(GameMode.SPECTATOR);
			}
			
		}.runTask(main);

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
	
		final Arena arena = game.getArena();
		
		/* save blocks */
		if(arena.getSavedBlocks().size() > 0) {
			game.getArena().getSavedBlocks().clear();
		}
		for(Block block : arena.getCuboid()) {
			if(block.getType() == Material.AIR) continue;
			
			final Location loc = block.getLocation();
			arena.getSavedBlocks().put(loc, block.getState());
		}
		/* save blocks */
		
		for(Player player : game.getPlayers()) {
			player.setWalkSpeed(0.2f);
		
			// safety checks
			player.getInventory().clear();
			player.setGameMode(GameMode.SURVIVAL);
			
			// give stuff
			game.getMode().giveKit(player);
			
			final ItemStack helmet = ItemBuilder.getEnchantedUnbreakableItem(Material.DIAMOND_HELMET,
					new UHCEnchant(Enchantment.PROTECTION_PROJECTILE, 1),
					new UHCEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3));
			final ItemStack chestplate = ItemBuilder.getEnchantedUnbreakableItem(Material.DIAMOND_CHESTPLATE,
					new UHCEnchant(Enchantment.PROTECTION_PROJECTILE, 1),
					new UHCEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3));
			final ItemStack leggings = ItemBuilder.getEnchantedUnbreakableItem(Material.DIAMOND_LEGGINGS,
					new UHCEnchant(Enchantment.PROTECTION_PROJECTILE, 1),
					new UHCEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3));
			final ItemStack boots = ItemBuilder.getEnchantedUnbreakableItem(Material.DIAMOND_BOOTS,
					new UHCEnchant(Enchantment.PROTECTION_PROJECTILE, 1),
					new UHCEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3));
			
			player.getInventory().setHelmet(helmet);
			player.getInventory().setChestplate(chestplate);
			player.getInventory().setLeggings(leggings);
			player.getInventory().setBoots(boots);
		}
		
		main.getBorderManager().startGame(game);
		game.setState(GameState.RUNNING);
		game.getAlivePlayers().addAll(game.getPlayers());
		
		// cleanup items & other entities
		for(Entity entity : game.getArena().getWorld().getEntities()) {
			if(!(entity instanceof Item) && !(entity instanceof Arrow) && !(entity instanceof TNTPrimed)) continue;
			entity.remove();
		}
	}
	
	public void endGame(UHCGame game) {
		for(Player player : game.getPlayers()) {
			setLobby(player);
		}
		main.getDeathChestManager().resetChests(game);
		
		resetGame(game);
	}
	
	public void winGame(UHCGame game, Player winner) {
		if(game.getState() != GameState.RUNNING) return;
		
		addWinElo(game.getMode().getType(), winner);
		
		game.setState(GameState.FINISHED);
		winner.setHealth(winner.getMaxHealth());
		game.setWinnerName(winner.getName());
		
		TitleUtil.sendTitle(winner, 0, 20*3, 10, Lang.get("titles.victory.title"), Lang.get("titles.victory.subtitle"));
		game.broadcastMessage(Lang.get("game.winner").replaceAll("%winner%", game.getWinnerName()));
		
		new BukkitRunnable() {

			@Override
			public void run() {
				endGame(game);
			}

		}.runTaskLater(main, 20L*5);
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

	@SuppressWarnings("deprecation")
	public void resetGame(UHCGame game) {
		// set things
		if(game.getArena() != null) {
			final Arena arena = game.getArena();
			
			arena.setUsed(false);
			arena.resetTeleportations();
			
			/* reset blocks */
			if(game.getArena().getSavedBlocks().size() > 0) {
				for(Block block : game.getArena().getCuboid()) {
					if(arena.getSavedBlocks().containsKey(block.getLocation())) {
						final BlockState state = arena.getSavedBlocks().get(block.getLocation());
						
						block.getState().setRawData(state.getRawData());
//						block.setType(state.getType());
//						block.getState().setData(state.getData());
//						block.getState().update(true);
					} else {
						block.setType(Material.AIR);
					}
				}
			}
			/* reset blocks */
		}
		game.setState(GameState.WAITING);
		game.setStartTimer(-1);
		
		main.getBorderManager().endGame(game);
		
		// get arena
		Arena arena = main.getArenaManager().getAvailableArea(game);
		game.setArena(arena);
		if(arena != null) {
			arena.setUsed(true);
		}

		// clear players
		game.getPlayers().clear();
		game.getAlivePlayers().clear();
	}
	
	public List<UHCGame> getGames(UHCMode mode){
		return games.get(mode);
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public boolean areGamesStopped() {
		return gamesStopped;
	}
	
	public List<UHCMode> getModes() {
		return modes;
	}
	
	public void setGamesStopped(boolean gamesStopped) {
		this.gamesStopped = gamesStopped;
	}

	public Map<UHCMode, List<UHCGame>> getGames() {
		return games;
	}
	
}
