package xyz.mintydev.uhcdeathmatch.leaderboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.modes.UHCMode;
import xyz.mintydev.uhcdeathmatch.data.EloPlayer;
import xyz.mintydev.uhcdeathmatch.util.ConfigUtil;

public class LeaderboardManager {

	private final UHCDeathMatch main;
	
	private Map<UHCMode, List<LeaderboardPlayer>> players = new HashMap<>();
	private Map<UHCMode, Hologram> holograms = new HashMap<>();
	
	public LeaderboardManager(UHCDeathMatch main) {
		this.main = main;
	}
	
	public void init() {
		for(UHCMode mode : main.getGameManager().getModes()) {
			createHologram(mode);
			main.getLogger().info("Creating leaderboard for mode : " + mode.getType().toString() + " ...");
		}
		
		new BukkitRunnable() {

			@Override
			public void run() {
				updateRankings();
				updateHolograms();
			}
			
		}.runTaskTimer(main, 0, 20*90);
	}
	
	public void createHologram(UHCMode mode) {
		if(holograms.containsKey(mode)) return;
		
		final World world = Bukkit.getWorld(main.getConfig().getString("settings.leaderboards.world"));
		final Location loc = ConfigUtil.parseLocation(world, main.getConfig().getConfigurationSection("settings.leaderboards." + mode.getType().toString().toLowerCase()));
		
		Hologram hologram = HologramsAPI.createHologram(main, loc);
		
		holograms.put(mode, hologram);
	}
	
	public void updateHolograms() {
		for(UHCMode mode : holograms.keySet()) {
			updateHologram(mode);
		}
	}
	
	public void updateHologram(UHCMode mode) {
		
		final Hologram hologram = holograms.get(mode);
		
		hologram.clearLines();
		
		List<String> lines = new ArrayList<>();
		
		final int entriesAmount = main.getConfig().getInt("settings.leaderboards.entries");
		
		for(String str : Lang.getList("holograms.leaderboard.content")) {
			if(str.contains("%entries%")) {
				// add entries
				
				for(int i = 1; i <= entriesAmount; i++) {
					// check if there is a player at this place
					
					final LeaderboardPlayer lPlayer = getRankedPlayerForPosition(mode, i);
					if(lPlayer == null) {
						lines.add(Lang.get("holograms.leaderboard.entry-none").replaceAll("%place%", i+""));
						continue;
					}
					
					String toAdd = Lang.get("holograms.leaderboard.entry");
					toAdd = toAdd.replaceAll("%place%", i+"");
					toAdd = toAdd.replaceAll("%player%", lPlayer.getePlayer().getUsername());
					toAdd = toAdd.replaceAll("%elo%", lPlayer.getValue()+"");
					
					lines.add(toAdd);
				}
				
			} else {
				lines.add(str.replaceAll("%mode%", mode.getDisplayName()));
			}
		}
		
		for(String line : lines) hologram.appendTextLine(line);
	}
	
	public LeaderboardPlayer getRankedPlayerForPosition(UHCMode mode, int rank) {
		for(LeaderboardPlayer rPlayer : players.get(mode)) {
			if(rPlayer.getRank() == rank) {
				return rPlayer;
			}
		}
		return null;
	}
	
	public void updateRankings() {
		Map<UHCMode, List<LeaderboardPlayer>> res = new HashMap<>();
		
		for(final UHCMode mode : main.getGameManager().getModes()) {
			List<LeaderboardPlayer> _players = new ArrayList<>();
			
			Map<EloPlayer, Integer> sorted = getSortedLeaderboard(mode);
			
			int i = 0;
			for(Entry<EloPlayer, Integer> entry : sorted.entrySet()) {
				final int ranking = i+1;
				
				final EloPlayer ePlayer = entry.getKey();
				final int elo = ePlayer.getElo(mode.getType());
				
				final LeaderboardPlayer rankedPlayer = new LeaderboardPlayer(ePlayer, ranking, elo);
				_players.add(rankedPlayer);
				
				i++;
			}
			
			res.put(mode, _players);
		}
		
		players = res;
	}
	
	protected Map<EloPlayer, Integer> getUnsortedLeaderboard(UHCMode mode){
		Map<EloPlayer, Integer> res = new HashMap<>();
		
		for(EloPlayer ePlayer : main.getEloPlayersManager().getEloPlayers().values()) {
			res.put(ePlayer, ePlayer.getElo(mode.getType()));
		}
		return res;
	}
	
	protected Map<EloPlayer, Integer> getSortedLeaderboard(UHCMode mode) {
		Map<EloPlayer, Integer> unSortedMap = getUnsortedLeaderboard(mode);
		LinkedHashMap<EloPlayer, Integer> reverseSortedMap = new LinkedHashMap<>();

		unSortedMap.entrySet()
		  .stream()
		  .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) 
		  .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
		
		return reverseSortedMap;
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public Map<UHCMode, List<LeaderboardPlayer>> getPlayers() {
		return players;
	}
	
	public Map<UHCMode, Hologram> getHolograms() {
		return holograms;
	}
	
}
