package xyz.mintydev.uhcdeathmatch.managers.border;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.mintydev.uhcdeathmatch.UHCDeathMatch;
import xyz.mintydev.uhcdeathmatch.core.UHCGame;

public class BorderRunnable extends BukkitRunnable {

	private final UHCDeathMatch main;
	private final UHCGame game;
	
	private int timeUntilChange = -1;
	
	public BorderRunnable(UHCDeathMatch main, UHCGame game) {
		this.main = main;
		this.game = game;
	}
	
	@Override
	public void run() {
		// manage stages
		final BorderStage stage = main.getBorderManager().getGamesStages().get(game);
		final int stageID = stage.getId();
		
		if(main.getBorderManager().getStages().size() <= stageID+1) {
			// no more stage
			// final stage
			timeUntilChange = -1;
		} else {
			final BorderStage nextStage = main.getBorderManager().getStages().get(stageID+1);
			
			if(timeUntilChange <= 0) {
				timeUntilChange = nextStage.getTimer();
			}
			
			if(timeUntilChange > 0) {
				timeUntilChange--;
				
				if(timeUntilChange == 0) {
					// change stage
					main.getBorderManager().getGamesStages().remove(game);
					main.getBorderManager().getGamesStages().put(game, nextStage);
				}
			}
			
			main.getBorderManager().updateBorder(game);
		}
		
		// tp if out
		for(Player player : game.getPlayers()) {
			if(isOutsideOfBorder(player)) {
				player.teleport(game.getArena().getCenter());
			}
		}
		
	}
	
	private boolean isOutsideOfBorder(Player p) {
        Location loc = p.getLocation();
        WorldBorder border = p.getWorld().getWorldBorder();
        double size = border.getSize()/2;
        Location center = border.getCenter();
        double x = loc.getX() - center.getX(), z = loc.getZ() - center.getZ();
        return ((x > size || (-x) > size) || (z > size || (-z) > size));
    }
	
	public int getTimeUntilChange() {
		return timeUntilChange;
	}

}
