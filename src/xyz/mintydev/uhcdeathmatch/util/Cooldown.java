package xyz.mintydev.uhcdeathmatch.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

public class Cooldown {

	private Map<UUID, Long> playerCooldowns = new HashMap<>();
	
	private final double cooldownTime;
	
	public Cooldown(double time) {
		this.cooldownTime = time;
	}
	
	public void startCooldown(Player player) {
		if(!(this.playerCooldowns.containsKey(player.getUniqueId())))
		{
			this.playerCooldowns.put(player.getUniqueId(), new Date().getTime());
		}
	}
	
	public boolean isUnderCooldown(Player player) {
		if(!(this.playerCooldowns.containsKey(player.getUniqueId()))) {
			return false;
		}
		
		long now = getNow();
		long startTime = this.playerCooldowns.get(player.getUniqueId());
		
		long difference = now - startTime;
		if(difference > getCooldownTime()*1000) {
			// Stop cooldown
			this.playerCooldowns.remove(player.getUniqueId());
		}
		
		return this.playerCooldowns.containsKey(player.getUniqueId());
	}
	
	public long getRemainingTimeSeconds(Player player) {
		long now = getNow();
		long startTime = this.playerCooldowns.get(player.getUniqueId());
		long finishTime = (long) (startTime + getCooldownTime()*1000);
		
		long diff = finishTime - now;
		
        long second = 1000l;
        long minute = 60l * second;
		
        long seconds = (diff % minute) / second;

        return seconds;
    }
	
	public String getRemainingTimeFormatted(Player player) {
		long now = getNow();
		long startTime = this.playerCooldowns.get(player.getUniqueId());
		long finishTime = (long) (startTime + getCooldownTime()*1000);
		
		long diff = finishTime - now;
		
        long second = 1000l;
        long minute = 60l * second;
        long hour = 60l * minute;
		
        long hours = diff / hour;
        long minutes = (diff % hour) / minute;
        long seconds = (diff % minute) / second;

        String result = "";
        if(hours > 0) {
        	result += String.format("%02d", hours) + "h ";
        }
        if(minutes > 0) {
            result += String.format("%02d", minutes) + "m ";
        }
        if(seconds > 0) {
            result += String.format("%02d", seconds) + "s ";
        }
		if(result.length() == 0) result = "0s";

        return result;
    }
	
	public long getNow() {
		return new Date().getTime();
	}
	
	public double getCooldownTime() {
		return cooldownTime;
	}

}
