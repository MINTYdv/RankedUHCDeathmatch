package xyz.mintydev.uhcdeathmatch.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;

public class Lang {

	private UHCDeathMatch main;
	
	private static File customConfigFile;
	private static FileConfiguration customConfig;
	
	private String fileName = "lang.yml";
	
	public Lang(UHCDeathMatch main)
	{
		this.main = main;
		createCustomConfig();
	}

	public static String get(String KEY) {
		return getCustomConfig().getString(KEY).replaceAll("&", "§");
	}
	
	public static List<String> getList(String KEY)
	{
		List<String> result = new ArrayList<>();
		for(String key : getCustomConfig().getStringList(KEY)) {
			result.add(key.replaceAll("&", "§"));
		}
		return result;
	}
	
    private void createCustomConfig() {
        customConfigFile = new File(main.getDataFolder(), fileName);
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            main.saveResource(fileName, false);
         }

        customConfig= new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    /* Getters & Setters */
    
    public static FileConfiguration getCustomConfig() {
		return customConfig;
	}
    
    public static File getCustomConfigFile() {
		return customConfigFile;
	}	
}
