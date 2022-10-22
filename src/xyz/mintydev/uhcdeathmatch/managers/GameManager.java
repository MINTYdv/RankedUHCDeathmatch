package xyz.mintydev.uhcdeathmatch.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.GameState;
import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.PlayerState;
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
	
	public void setLobby(Player player) {
		UHCPlayer uhcPlayer = main.getPlayersManager().getPlayer(player);
		uhcPlayer.setState(PlayerState.LOBBY);
		player.getInventory().clear();
		
		// set items
		final ItemStack swordItem = ItemBuilder.createItem(Material.DIAMOND_SWORD, 1, Lang.get("items.sword"), new ArrayList<>());
		player.getInventory().setItem(0, swordItem);
	}
	
	private void createNewGame() {
		this.games.add(new UHCGame());
	}

	public void resetGame(UHCGame game) {
		game.setState(GameState.WAITING);
		game.setArena(null);
		
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
