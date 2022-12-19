package xyz.mintydev.uhcdeathmatch.core;

import java.util.List;

public class UHCMap {

	private String id;
	private String displayName;
	private List<Arena> arenas;
	private boolean nodebuff;
	
	public UHCMap(String id, String displayName, List<Arena> arenas, boolean nodebuff) {
		this.id = id;
		this.displayName = displayName;
		this.arenas = arenas;
		this.nodebuff = nodebuff;
	}
	
	public boolean isNodebuff() {
		return nodebuff;
	}
	
	public List<Arena> getArenas() {
		return arenas;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getId() {
		return id;
	}
	
}
