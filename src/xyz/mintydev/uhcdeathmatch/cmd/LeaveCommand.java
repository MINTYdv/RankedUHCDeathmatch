package xyz.mintydev.uhcdeathmatch.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.GameState;
import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;
import xyz.mintydev.uhcdeathmatch.duels.DuelGame;

public class LeaveCommand implements CommandExecutor {

	private final UHCDeathMatch main;
	
	public LeaveCommand(UHCDeathMatch main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!(sender instanceof Player)) {
			sender.sendMessage(Lang.get("errors.commands.player-only"));
			return false;
		}
		
		final Player player = (Player) sender;

		final UHCGame game = main.getGameManager().getGame(player);
		if(game == null) {
			sender.sendMessage(Lang.get("errors.commands.no-game"));
			return false;
		}
		
		if(game.getState() == GameState.WAITING && game instanceof DuelGame) {
			for(final Player p : game.getPlayers()) {
				if(p != null && p.isOnline()) {
					main.getGameManager().leaveGame(p, game);
					
					if(p != player) {
						p.sendMessage(Lang.get("commands.duel.messages.opponent-left"));
					}
				}
			}
			
			main.getGameManager().endGame(game);
		}
		
		// leave the game
		if(game.getState() == GameState.RUNNING) {
			game.broadcastMessage(Lang.get("commands.leave.player-left").replaceAll("%player%", player.getName()+""));
		}
		main.getGameManager().leaveGame(player, game);
		sender.sendMessage(Lang.get("commands.leave.success"));
		return false;
	}
	
}
