package xyz.mintydev.uhcdeathmatch.managers;

import java.util.ArrayList;
import java.util.List;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.GameState;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;

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
