package xyz.mintydev.uhcdeathmatch.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.modes.UHCModeType;

public class EloPlayersManager {

	private static Map<UUID, EloPlayer> elarysPlayers = new HashMap<>();

    private final String folderName = "players";
    private final UHCDeathMatch main;
    private File dataFolder;
    
    private final Gson gson;
    
	public EloPlayersManager(UHCDeathMatch main) {
		this.main = main;
		this.gson = new GsonBuilder().setPrettyPrinting().create();;
		
		createCustomFolder();
		loadAllFilesData();
		loadAllPlayersData();
	}
	
	private void loadAllFilesData() {
		for(final File file : dataFolder.listFiles()) {
			try {
				String fileNameWithOutExt = FilenameUtils.removeExtension(file.getName());
				UUID uuid = UUID.fromString(fileNameWithOutExt);
				EloPlayer ePlayer = load(file);
				getEloPlayers().put(uuid, ePlayer);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadAllPlayersData() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			getPlayer(player);
		}
	}

	private void createCustomFolder() {
		File theDir = new File(main.getDataFolder(), folderName);
		if (!theDir.exists()) {
		    try{
		        theDir.mkdirs();
		    }
		    catch(Exception e){
		    	e.printStackTrace();
		    }      
		}
		dataFolder = new File(main.getDataFolder(), folderName);
	}

	public void saveAll() {
		savePlayersDataToConfig();
	}
	
	public void saveAndRemove(EloPlayer ePlayer) {
		try {
			save(ePlayer);
			elarysPlayers.remove(ePlayer.getUUID());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeIfNotOnline(EloPlayer ePlayer){
		if(ePlayer.getPlayer() == null || !Bukkit.getOfflinePlayer(ePlayer.getUUID()).isOnline()){
			elarysPlayers.remove(ePlayer.getUUID());
		}
	}
	
	private EloPlayer load(File file) throws FileNotFoundException {
		if(!(file.exists())) return null;
		final Reader reader = new FileReader(file);
		final EloPlayer p = gson.fromJson(reader, EloPlayer.class);
		return p;
	}
	
	private EloPlayer load(UUID UUID) {
		final File file = new File(getDataFolder(), UUID.toString() + ".json");
		try {
			return load(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void save(EloPlayer ePlayer) throws IOException {
		final File file = new File(getDataFolder(), ePlayer.getUUID().toString() + ".json");
		file.getParentFile().mkdir();
		if(!(file.exists())) {
			file.createNewFile();
		}
		final Writer writer = new FileWriter(file, false);
		gson.toJson(ePlayer, writer);
		writer.flush();
		writer.close();
	}
	
	private void savePlayersDataToConfig() {
		for(Entry<UUID, EloPlayer> ePlayer : getEloPlayers().entrySet()) {
			try {
				save(ePlayer.getValue());
			} catch (IOException e) { e.printStackTrace(); }
		}
	}
		
	public EloPlayer getPlayer(Player player) {
		if(player == null) return null;
		
		if(!(getEloPlayers().containsKey(player.getUniqueId()))) {
			if(load(player.getUniqueId()) == null) {
				final EloPlayer ePlayer = new EloPlayer();
				ePlayer.setUUID(player.getUniqueId());
				ePlayer.setUsername(player.getName());
				
				for(UHCModeType type : UHCModeType.values()) {
					ePlayer.setElo(type, main.getConfig().getInt("settings.elo.default"));
				}
				getEloPlayers().put(player.getUniqueId(), ePlayer);
			}else {
				// load from file
				final EloPlayer ePlayer = load(player.getUniqueId());
				getEloPlayers().put(player.getUniqueId(), ePlayer);
			}
		}
		return getEloPlayers().get(player.getUniqueId());
	}

	public EloPlayer getPlayerByUUID(UUID uuid){
		if(uuid == null) return null;

		if(!(getEloPlayers().containsKey(uuid))) return load(uuid);
		else return getEloPlayers().get(uuid);
	}
	
	/*
	 * Getters & Setters
	 * */
	
	public File getDataFolder() {
		return dataFolder;
	}
	
	public String getFolderName() {
		return folderName;
	}
	
	public static Map<UUID, EloPlayer> getEloPlayers() {
		return elarysPlayers;
	}
	
}
