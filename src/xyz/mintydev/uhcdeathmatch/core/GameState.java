package xyz.mintydev.uhcdeathmatch.core;

public enum GameState {

	WAITING("§aWaiting for players"),
	RUNNING("§cRunning"),
	FINISHED("§6Finished");
	
	final String displayName;
	
	GameState(String displayName){
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
}
