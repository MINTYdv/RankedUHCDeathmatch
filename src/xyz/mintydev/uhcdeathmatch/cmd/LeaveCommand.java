package xyz.mintydev.uhcdeathmatch.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.GameState;
import xyz.mintydev.uhcdeathmatch.core.Lang;
import xyz.mintydev.uhcdeathmatch.core.PlayerState;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;
import xyz.mintydev.uhcdeathmatch.core.UHCPlayer;

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

		if(main.getGameManager().getGame(player) == null) {
			sender.sendMessage(Lang.get("errors.commands.no-game"));
			return false;
		}
		
		final UHCGame game = main.getGameManager().getGame(player);
		final UHCPlayer uPlayer = main.getPlayersManager().getPlayer(player);
		if(uPlayer.getState() != PlayerState.SPECTATOR && game.getState() == GameState.RUNNING) {
			sender.sendMessage(Lang.get("commands.leave.running"));
			return false;
		}
		
		// leave the game
		main.getGameManager().leaveGame(player, game);
		sender.sendMessage(Lang.get("commands.leave.success"));
		return false;
	}
	
}
