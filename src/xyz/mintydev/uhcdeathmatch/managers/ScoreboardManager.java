package xyz.mintydev.uhcdeathmatch.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.mrmicky.fastboard.FastBoard;
import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.GameState;
import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;
import xyz.mintydev.uhcdeathmatch.core.UHCPlayer;

public class ScoreboardManager {

	private final UHCDeathMatch main;
	
	private Map<Player, FastBoard> boards = new HashMap<>();
	
	public ScoreboardManager(UHCDeathMatch main) {
		this.main = main;
	}
	
	public void updateScoreboard(Player player) {
		
		if(!(boards.containsKey(player))) {
			boards.put(player, new FastBoard(player));
		}
		
		FastBoard board = boards.get(player);
		board.updateTitle(Lang.get("scoreboards.title"));
		
		final boolean inGame = main.getGameManager().getGame(player) != null;
		
		if(!inGame) {
			// lobby scoreboard
			List<String> lines = new ArrayList<>();
			for(String str : Lang.getList("scoreboards.hub")) {
				str = str.replaceAll("%online%", Bukkit.getOnlinePlayers().size()+"");
				str = str.replaceAll("%ingame%", main.getGameManager().getAmountofIngamePlayers()+"");
				str = str.replaceAll("%elo%", main.getEloPlayersManager().getPlayer(player).getElo()+"");
				lines.add(str);
			}
			board.updateLines(lines);
			
			return;
		} else {
			final UHCGame game = main.getGameManager().getGame(player);
			final UHCPlayer uPlayer = main.getPlayersManager().getPlayer(player);
			
			List<String> lines = new ArrayList<>();
			if(game.getState() == GameState.WAITING) {
				for(String str : Lang.getList("scoreboards.game.waiting.content")) {
					str = str.replaceAll("%players%", game.getPlayers().size()+"");
					str = str.replaceAll("%mode%", ChatColor.stripColor(game.getMode().getDisplayName())+"");
					str = str.replaceAll("%online%", Bukkit.getOnlinePlayers().size()+"");
					str = str.replaceAll("%elo%", main.getEloPlayersManager().getPlayer(player).getElo()+"");
					
					String status = "§fWaiting for players..";
					if(game.getStartTimer() > 0) {
						status = "§fStarting in §b" + game.getStartTimer();
					}
					
					str = str.replaceAll("%status%", status);
					
					lines.add(str);
				}
			} else if(game.getState() == GameState.RUNNING) {
				for(String str : Lang.getList("scoreboards.game.running.content")) {
					str = str.replaceAll("%players%", game.getPlayers().size()+"");
					str = str.replaceAll("%online%", Bukkit.getOnlinePlayers().size()+"");
					str = str.replaceAll("%kills%", uPlayer.getKills()+"");
					str = str.replaceAll("%alive%", game.getAlivePlayers().size()+"");
					str = str.replaceAll("%elo%", main.getEloPlayersManager().getPlayer(player).getElo()+"");
					str = str.replaceAll("%mode%", ChatColor.stripColor(game.getMode().getDisplayName())+"");
					
					final int timer = main.getBorderManager().getBorderRunnables().get(game).getTimeUntilChange();
					
					final String timerString = timer >= 0 ? Lang.get("scoreboards.game.running.shrink.timer").replace("%timer%", timer+"") : Lang.get("scoreboards.game.running.shrink.final");
					
					str = str.replaceAll("%shrink%", timerString);
					
					lines.add(str);
				}
			} else if(game.getState() == GameState.FINISHED) {
				for(String str : Lang.getList("scoreboards.game.finished.content")) {
					str = str.replaceAll("%players%", game.getPlayers().size()+"");
					str = str.replaceAll("%online%", Bukkit.getOnlinePlayers().size()+"");
					str = str.replaceAll("%kills%", uPlayer.getKills()+"");
					str = str.replaceAll("%alive%", game.getAlivePlayers().size()+"");
					str = str.replaceAll("%mode%", ChatColor.stripColor(game.getMode().getDisplayName())+"");
					str = str.replaceAll("%elo%", main.getEloPlayersManager().getPlayer(player).getElo()+"");
					str = str.replaceAll("%winner%", game.getWinnerName());
					lines.add(str);
				}
			}
			board.updateLines(lines);
		}
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public Map<Player, FastBoard> getBoards() {
		return boards;
	}
	
}
