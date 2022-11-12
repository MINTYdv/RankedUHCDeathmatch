package xyz.mintydev.uhcdeathmatch.duels;

import org.bukkit.entity.Player;

import xyz.mintydev.uhcdeathmatch.core.UHCGame;
import xyz.mintydev.uhcdeathmatch.core.modes.UHCMode;

public class DuelGame extends UHCGame {

	private final Player firstPlayer;
	private final Player secondPlayer;
	
	public DuelGame(UHCMode mode, Player firstPlayer, Player secondPlayer) {
		super(mode);
		
		this.firstPlayer = firstPlayer;
		this.secondPlayer = secondPlayer;
	}
	
	public boolean isFirst(Player player) {
		return firstPlayer.getName().equalsIgnoreCase(player.getName());
	}
	
	public Player getFirstPlayer() {
		return firstPlayer;
	}
	
	public Player getSecondPlayer() {
		return secondPlayer;
	}
	
}
