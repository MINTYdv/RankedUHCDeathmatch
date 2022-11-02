package xyz.mintydev.uhcdeathmatch.managers.border;

public class BorderStage {

	private final int id;
	private final int timer;
	private final int size;
	
	public BorderStage(int id, int timer, int size) {
		this.id = id;
		this.timer = timer;
		this.size = size;
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public int getId() {
		return id;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getTimer() {
		return timer;
	}
	
}
