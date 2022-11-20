package xyz.mintydev.uhcdeathmatch.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.PlayerState;
import xyz.mintydev.uhcdeathmatch.core.UHCPlayer;
import xyz.mintydev.uhcdeathmatch.core.gui.DuelModeSelectGUI;
import xyz.mintydev.uhcdeathmatch.duels.DuelRequest;

public class DuelCommand implements CommandExecutor {

	private final UHCDeathMatch main;
	
	public DuelCommand(UHCDeathMatch main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		// don't allow console
		if(!(sender instanceof Player)) {
			sender.sendMessage(Lang.get("errors.commands.player-only"));
			return false;
		}
		
		final Player player = (Player) sender;
		final UHCPlayer uPlayer = main.getPlayersManager().getPlayer(player);
		
		if(args.length > 0) {
			if(args[0].equalsIgnoreCase("accept")) {
				if(args.length < 2) {
					sender.sendMessage(Lang.get("commands.duel.usages.main"));
					return false;
				}
				
				final String targetName = args[1];
				// can't find target player
				if(Bukkit.getPlayer(targetName) == null) {
					sender.sendMessage(Lang.get("commands.duel.errors.unknown-player").replaceAll("%name%", targetName));
					return false;
				}
				
				final Player target = Bukkit.getPlayer(targetName);
				
				// accept the duel request by that player
				
				if(main.getDuelManager().getRequestsFrom(target, player).size() == 0) {
					sender.sendMessage(Lang.get("commands.duel.errors.no-pending"));
					return false;
				}
				
				final DuelRequest request = main.getDuelManager().getRequestsFrom(target, player).get(0);
				
				main.getDuelManager().acceptRequest(request);
				
				return true;
			}
			
			final String targetName = args[0];
			
			// can't find target player
			if(Bukkit.getPlayer(targetName) == null) {
				sender.sendMessage(Lang.get("commands.duel.errors.unknown-player").replaceAll("%name%", targetName));
				return false;
			}
			
			// if sender is already in game
			if(uPlayer.getState() != PlayerState.LOBBY) {
				sender.sendMessage(Lang.get("commands.duel.errors.sender-playing"));
				return false;
			}
			
			final Player target = Bukkit.getPlayer(targetName);
			final UHCPlayer uTarget = main.getPlayersManager().getPlayer(target);
			
			// if target is already in game
			if(uTarget.getState() != PlayerState.LOBBY) {
				sender.sendMessage(Lang.get("commands.duel.errors.player-playing"));
				return false;
			}
			
			if(target.getName().equalsIgnoreCase(player.getName())) {
				sender.sendMessage(Lang.get("commands.duel.errors.yourself"));
				return false;
			}
			
			// can duel the player
			// check if the sender has already sent a duel request to the player
			
			if(main.getDuelManager().hasSentRequestTo(player, target)) {
				sender.sendMessage(Lang.get("commands.duel.errors.already-sent"));
				return false;
			}
			
			// open the gui
			main.getGuiManager().open(player, new DuelModeSelectGUI(main, target));
			return true;
		}
		
		sender.sendMessage(Lang.get("commands.duel.usages.main"));
		return false;
	}
	
}
