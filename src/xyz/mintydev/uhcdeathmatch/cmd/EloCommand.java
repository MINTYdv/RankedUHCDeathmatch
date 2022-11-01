package xyz.mintydev.uhcdeathmatch.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.gui.EloGUI;
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
				ePlayer.setElo(main.getConfig().getInt("settings.elo.default"));
				
				sender.sendMessage(Lang.get("commands.elo.success").replaceAll("%elo%", ePlayer.getElo()+""));
				return false;
			}
			
			if(args[0].equalsIgnoreCase("give")) {
				if(args.length < 3) {
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

				if(!(UHCUtil.isInteger(args[2]))) {
					sender.sendMessage(Lang.get("commands.elo.error-number"));
					return false;
				}
				
				final int amount = Integer.parseInt(args[2]);
				
				ePlayer.addElo(amount);
				
				sender.sendMessage(Lang.get("commands.elo.success").replaceAll("%elo%", ePlayer.getElo()+""));
				return false;
			}
			
			if(args[0].equalsIgnoreCase("remove")) {
				if(args.length < 3) {
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

				if(!(UHCUtil.isInteger(args[2]))) {
					sender.sendMessage(Lang.get("commands.elo.error-number"));
					return false;
				}
				
				final int amount = Integer.parseInt(args[2]);
				
				ePlayer.removeElo(amount);
				
				sender.sendMessage(Lang.get("commands.elo.success").replaceAll("%elo%", ePlayer.getElo()+""));
				return false;
			}
		}
		
		// simple elo
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(Lang.get("errors.commands.player-only"));
			return false;
		}
		
		final Player player = (Player) sender;

		Player targetPlayer = player;
		
		if(args.length == 1) {
			final String targetName = args[0];
			
			if(Bukkit.getPlayer(targetName) == null) {
				player.sendMessage(Lang.get("commands.elo.error-player").replaceAll("%name%", args[0]));
				return false;
			}
			
			targetPlayer = Bukkit.getPlayer(targetName);
		}
		
		// open the gui
		main.getGuiManager().open(player, new EloGUI(main, targetPlayer));
		return false;
	}
	
}
