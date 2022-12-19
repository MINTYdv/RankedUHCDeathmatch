package xyz.mintydev.uhcdeathmatch.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.Arena;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;
import xyz.mintydev.uhcdeathmatch.core.UHCMap;
import xyz.mintydev.uhcdeathmatch.core.modes.UHCMode;
import xyz.mintydev.uhcdeathmatch.core.modes.UHCModeType;
import xyz.mintydev.uhcdeathmatch.duels.DuelGame;
import xyz.mintydev.uhcdeathmatch.util.ConfigUtil;

public class ArenaManager {

	private List<UHCMap> maps = new ArrayList<>();
	private List<Arena> arenas = new ArrayList<>();
		
    private File customConfigFile;
    private FileConfiguration customConfig;
	
	private final UHCDeathMatch main;
	
	private final static String fileName = "arenas.yml";
	
	public ArenaManager(UHCDeathMatch main) {
		this.main = main;
		
		createCustomConfig();
		loadArenas();
	}	

	private void loadArenas() {
		ConfigurationSection sec = getCustomConfig().getConfigurationSection("arenas");
		
		for(String id : sec.getKeys(false)) {
			final String worldName = sec.getString(id + ".world").replaceAll(" ", "_");
			final String name = sec.getString(id + ".name").replaceAll("&", "§");
			final World world = Bukkit.getWorld(worldName);

			List<Location> locations = new ArrayList<>();
			ConfigurationSection locSec = sec.getConfigurationSection(id + ".players-pos");
			ConfigurationSection centerSec = sec.getConfigurationSection(id + ".center");

			final Location center = ConfigUtil.parseLocation(world, centerSec);
			final Location pos1 = ConfigUtil.parseLocation(world, sec.getConfigurationSection(id + ".pos1"));
			final Location pos2 = ConfigUtil.parseLocation(world, sec.getConfigurationSection(id + ".pos2"));
			
			for(String locId : locSec.getKeys(false)) {
				final double x = locSec.getDouble(locId + ".x");
				final double y = locSec.getDouble(locId + ".y");
				final double z = locSec.getDouble(locId + ".z");
				final double yaw = locSec.getDouble(locId + ".yaw");
				final double pitch = locSec.getDouble(locId + ".pitch");
				locations.add(new Location(world, x, y, z, (float)yaw, (float)pitch));
			}
			
			final Arena arena = new Arena(id, name, worldName, pos1, pos2, center, locations);
			this.arenas.add(arena);
		}
		
		// Loading maps
		ConfigurationSection secMaps = getCustomConfig().getConfigurationSection("maps");
		
		for(String id : secMaps.getKeys(false)) {
			final String mapName = secMaps.getString(id + ".name");
			final boolean nodebuff = secMaps.getBoolean(id + ".nodebuff");
			
			List<Arena> arenas = new ArrayList<>();
			for(String arenaID : secMaps.getStringList(id + ".arenas")) {
				final Arena arena = getArenaById(arenaID);
				if(arena == null) {
					main.getLogger().warning("Couldn't find an arena with the ID of : " + arenaID + " - while creating map : " + id);
					continue;
				}
				arenas.add(arena);
			}
			
			System.out.println("Map " + id + " - " + arenas.size());
			final UHCMap map = new UHCMap(id, mapName, arenas, nodebuff);
			this.maps.add(map);
		}
		
		main.getLogger().info("Loaded " + arenas.size() + " arenas and " + maps.size() + " maps");
	}

	private void createCustomConfig() {
        customConfigFile = new File(main.getDataFolder(), fileName);
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            main.saveResource(fileName, false);
         }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
	
	public Arena getArenaById(String ID) {
		for(Arena arena : this.arenas) {
			if(arena.getId().equalsIgnoreCase(ID)) {
				return arena;
			}
		}
		return null;
	}
	
	public List<String> getTypes(){
		List<String> result = new ArrayList<>();
		for(Arena arena : this.arenas) {
			if(result.contains(arena.getType())) continue;
			
			result.add(arena.getType());
		}
		return result;
	}
	
	public void distributeArenas() {
		
		for(UHCMode mode : main.getGameManager().getModes()) {
			
			int i = 0;
			for(int z = 0; z < main.getGameManager().getGames(mode).size(); z++) {
				
				final UHCGame game = main.getGameManager().getGames(mode).get(z);
				
				i++;
				if(main.getArenaManager().getMaps(mode).size() == i) i = 0;
				
				UHCMap map = main.getArenaManager().getMaps(mode).get(i);
				
				final Arena arena = getArena(game, map);
				game.setArena(arena);
				if(arena != null) {
					arena.setUsed(true);
				} else {
					if(map.isNodebuff()) {
						Bukkit.broadcastMessage("arena is null for game " + (z+1));	
					}
				}
			}
		}
	}
	
	public Arena getArena(UHCGame game, UHCMap map) {
		for(Arena arena : map.getArenas()) {
			if(arena.isUsed()) continue;
			if(map.isNodebuff()) Bukkit.broadcastMessage("return " + arena.getId());
			return arena;
		}
		if(map.isNodebuff()) Bukkit.broadcastMessage("return null");
		return null;
	}
	
	public Arena getValidDuelArena(DuelGame game) {
		List<Arena> shuffled = new ArrayList<>();
		shuffled.addAll(arenas);
		Collections.shuffle(shuffled);
		
		for(Arena arena : shuffled) {
			if(!(isValidArena(arena, game))) continue;
			return arena;
		}
		return null;
	}
	
	private boolean isValidArena(Arena arena, UHCGame game) {
		if(arena.isUsed()) return false;
		
		final UHCMap map = getMap(arena);
		if(map.isNodebuff() && game.getMode().getType() != UHCModeType.NODEBUFF) return false;
		return true;
	}
	
	public UHCMap getMap(Arena arena) {
		for(UHCMap map : maps) {
			if(map.getArenas().contains(arena)) return map;
		}
		return null;
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public List<UHCMap> getMaps(UHCMode mode) {
		List<UHCMap> res = new ArrayList<>();
		for(UHCMap map : maps) {
			if(map.isNodebuff() && mode.getType() == UHCModeType.CLASSIC) continue;
			res.add(map);
		}
		return res;
	}
	
	public FileConfiguration getCustomConfig() {
		return customConfig;
	}
	
	public File getCustomConfigFile() {
		return customConfigFile;
	}

}
