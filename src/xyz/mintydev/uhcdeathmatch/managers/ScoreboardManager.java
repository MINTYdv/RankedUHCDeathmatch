package xyz.mintydev.uhcdeathmatch.managers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import fr.mrmicky.fastboard.FastBoard;
import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.Lang;

public class ScoreboardManager {

	private final UHCDeathMatch main;
	
	private Map<Player, FastBoard> boards = new HashMap<>();
	
	public ScoreboardManager(UHCDeathMatch main) {
		this.main = main;
	}
	
	public void createScoreboard(Player player) {
		if(getBoards().containsKey(player)) return;
		
		FastBoard board = new FastBoard(player);
		
		board.updateTitle(Lang.get("scoreboards.title"));
		
		boards.put(player, board);
	}
	
	public void updateScoreboard(Player player) {
		
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public Map<Player, FastBoard> getBoards() {
		return boards;
	}
	
}
