package xyz.mintydev.uhcdeathmatch.cmd;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.gui.EloGUI;
import xyz.mintydev.uhcdeathmatch.core.modes.UHCModeType;
import xyz.mintydev.uhcdeathmatch.data.EloPlayer;
import xyz.mintydev.uhcdeathmatch.util.UHCUtil;

public class EloCommand implements CommandExecutor {

	private final UHCDeathMatch main;
	
	public EloCommand(UHCDeathMatch main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(args.length >= 1) {
			if(args[0].equalsIgnoreCase("reset")) {
				if(args.length < 2) {
					sender.sendMessage(Lang.get("commands.elo.usage-reset"));
					return false;
				}
				
				final String targetName = args[1];
				
				if(Bukkit.getPlayer(targetName) == null) {
					sender.sendMessage(Lang.get("commands.elo.error-player").replaceAll("%name%", args[0]));
					return false;
				}
				
				final Player target = Bukkit.getPlayer(targetName);
				final EloPlayer ePlayer = main.getEloPlayersManager().getPlayer(target);
				for(UHCModeType type : UHCModeType.values()) {
					ePlayer.setElo(type, main.getConfig().getInt("settings.elo.default"));
					sender.sendMessage(Lang.get("commands.elo.success").replaceAll("%elo%", ePlayer.getElo(type)+"").replaceAll("%mode%", type.toString()));
				}
				return false;
			}
			
			if(args[0].equalsIgnoreCase("give")) {
				if(args.length < 4) {
					sender.sendMessage(Lang.get("commands.elo.usage-give"));
					return false;
				}
				
				final String targetName = args[1];
				
				if(Bukkit.getPlayer(targetName) == null) {
					sender.sendMessage(Lang.get("commands.elo.error-player").replaceAll("%name%", args[0]));
					return false;
				}
				
				final Player target = Bukkit.getPlayer(targetName);
				final EloPlayer ePlayer = main.getEloPlayersManager().getPlayer(target);

				final String modeString = args[2];
				
				String modesList = "";
				for(UHCModeType type : UHCModeType.values()) {
					modesList += type.toString() + " ";
				}
				
				if(UHCModeType.valueOf(modeString.toUpperCase()) == null) {
					sender.sendMessage(Lang.get("commands.elo.error-mode") + " " + modesList);
					return false;
				}
				final UHCModeType type = UHCModeType.valueOf(modeString.toUpperCase());
				
				if(!(UHCUtil.isInteger(args[3]))) {
					sender.sendMessage(Lang.get("commands.elo.error-number"));
					return false;
				}
				
				final int amount = Integer.parseInt(args[3]);
				
				ePlayer.addElo(type, amount);
				
				sender.sendMessage(Lang.get("commands.elo.success").replaceAll("%elo%", ePlayer.getElo(type)+"").replaceAll("%mode%", type.toString()));
				return false;
			}
			
			if(args[0].equalsIgnoreCase("remove")) {
				if(args.length < 4) {
					sender.sendMessage(Lang.get("commands.elo.usage-remove"));
					return false;
				}
				
				final String targetName = args[1];
				
				if(Bukkit.getPlayer(targetName) == null) {
					sender.sendMessage(Lang.get("commands.elo.error-player").replaceAll("%name%", args[0]));
					return false;
				}
				
				final Player target = Bukkit.getPlayer(targetName);
				final EloPlayer ePlayer = main.getEloPlayersManager().getPlayer(target);

				final String modeString = args[2];
				
				String modesList = "";
				for(UHCModeType type : UHCModeType.values()) {
					modesList += type.toString() + " ";
				}
				
				if(UHCModeType.valueOf(modeString.toUpperCase()) == null) {
					sender.sendMessage(Lang.get("commands.elo.error-mode") + " " + modesList);
					return false;
				}
				final UHCModeType type = UHCModeType.valueOf(modeString.toUpperCase());
				
				if(!(UHCUtil.isInteger(args[3]))) {
					sender.sendMessage(Lang.get("commands.elo.error-number"));
					return false;
				}
				
				final int amount = Integer.parseInt(args[3]);
				
				ePlayer.removeElo(type, amount);
				
				sender.sendMessage(Lang.get("commands.elo.success").replaceAll("%elo%", ePlayer.getElo(type)+"").replaceAll("%mode%", type.toString()));
				return false;
			}
		}
		
		// simple elo
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(Lang.get("errors.commands.player-only"));
			return false;
		}
		
		final Player player = (Player) sender;

		EloPlayer targetPlayer = main.getEloPlayersManager().getPlayer(player);
		
		if(args.length == 1) {
			final String targetName = args[0];
			
			if(main.getEloPlayersManager().getPlayer(targetName) == null) {
				player.sendMessage(Lang.get("commands.elo.error-player").replaceAll("%name%", args[0]));
				return false;
			}
			
			targetPlayer = main.getEloPlayersManager().getPlayer(targetName);
		}
		
		// open the gui
		main.getGuiManager().open(player, new EloGUI(main, targetPlayer));
		return false;
	}
	
}
