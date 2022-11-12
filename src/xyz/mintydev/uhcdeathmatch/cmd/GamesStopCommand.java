package xyz.mintydev.uhcdeathmatch.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.Lang;

public class GamesStopCommand implements CommandExecutor {

	private final UHCDeathMatch main;
	
	public GamesStopCommand(UHCDeathMatch main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!(sender.hasPermission("uhcdm.gamestop"))) {
			sender.sendMessage(Lang.get("errors.commands.no-permission"));
			return false;
		}
		
		if(main.getGameManager().areGamesStopped()) {
			// unblock
			sender.sendMessage(Lang.get("commands.gamestop.success-unblock"));
		} else {
			// block
			sender.sendMessage(Lang.get("commands.gamestop.success"));
		}
		
		main.getGameManager().setGamesStopped(!main.getGameManager().areGamesStopped());
		return false;
	}
	
}
