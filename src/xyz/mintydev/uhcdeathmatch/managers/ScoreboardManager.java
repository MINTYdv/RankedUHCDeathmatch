package xyz.mintydev.uhcdeathmatch.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
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
		updateScoreboard(player);
	}
	
	public void updateScoreboard(Player player) {
		final boolean inGame = main.getGameManager().getGame(player) != null;
		
		if(!inGame) {
			// lobby scoreboard
			List<String> lines = new ArrayList<>();
			for(String str : Lang.getList("scoreboards.hub")) {
				str = str.replaceAll("%online%", Bukkit.getOnlinePlayers().size()+"");
				str = str.replaceAll("%ingame%", main.getGameManager().getAmountofIngamePlayers()+"");
				lines.add(str);
			}
			
			FastBoard board = getBoards().get(player);
			board.updateLines(lines);
			
			return;
		}
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public Map<Player, FastBoard> getBoards() {
		return boards;
	}
	
}
