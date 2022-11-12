package xyz.mintydev.uhcdeathmatch.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import xyz.mintydev.uhcdeathmatch.core.modes.UHCModeType;

public class ArenaManager {

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
			final boolean isNodebuff = sec.getBoolean(id + ".nodebuff");
			
			List<Location> locations = new ArrayList<>();
			ConfigurationSection locSec = sec.getConfigurationSection(id + ".players-pos");
			ConfigurationSection centerSec = sec.getConfigurationSection(id + ".center");
			final double centerX = centerSec.getDouble("x");
			final double centerY = centerSec.getDouble("y");
			final double centerZ = centerSec.getDouble("z");
			final double centerYaw = centerSec.getDouble("yaw");
			final double centerPitch = centerSec.getDouble("pitch");
			
			final Location center = new Location(world, centerX, centerY, centerZ, (float)centerYaw, (float)centerPitch);
			
			for(String locId : locSec.getKeys(false)) {
				final double x = locSec.getDouble(locId + ".x");
				final double y = locSec.getDouble(locId + ".y");
				final double z = locSec.getDouble(locId + ".z");
				final double yaw = locSec.getDouble(locId + ".yaw");
				final double pitch = locSec.getDouble(locId + ".pitch");
				locations.add(new Location(world, x, y, z, (float)yaw, (float)pitch));
			}
			
			final Arena arena = new Arena(isNodebuff, name, worldName, center, locations);
			this.arenas.add(arena);
		}
		main.getLogger().info("Loaded " + arenas.size() + " arenas.");
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
	
	public Arena getAvailableArea(UHCGame game) {
		for(Arena arena : this.arenas) {
			if(arena.isUsed()) continue;
			if(!(arena.isNodebuff()) && game.getMode().getType() == UHCModeType.NODEBUFF) continue;
			
			return arena;
		}
		return null;
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public FileConfiguration getCustomConfig() {
		return customConfig;
	}
	
	public File getCustomConfigFile() {
		return customConfigFile;
	}
	
}
