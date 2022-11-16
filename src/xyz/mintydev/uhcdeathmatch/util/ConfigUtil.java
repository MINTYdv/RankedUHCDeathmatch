package xyz.mintydev.uhcdeathmatch.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigUtil {

	public static Location parseLocation(World world, ConfigurationSection sec) {
		final double x = sec.getDouble("x");
		final double y = sec.getDouble("y");
		final double z = sec.getDouble("z");
		
		double yaw = 0;
		double pitch = 0;
		if(sec.contains("yaw")) yaw = sec.getDouble("yaw");
		if(sec.contains("pitch")) pitch = sec.getDouble("pitch");
		
		return new Location(world, x, y, z, (float) yaw, (float) pitch);
	}
	
}
