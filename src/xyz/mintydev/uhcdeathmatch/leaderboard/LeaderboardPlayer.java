package xyz.mintydev.uhcdeathmatch.leaderboard;

import xyz.mintydev.uhcdeathmatch.data.EloPlayer;

public class LeaderboardPlayer {

	private EloPlayer ePlayer;
	private int rank;
	private int value;
	
	public LeaderboardPlayer(EloPlayer ePlayer, int rank, int value) {
		this.ePlayer = ePlayer;
		this.rank = rank;
		this.value = value;
	}
	
	/* 
	 * Getters & Setters
	 * */
	
	public EloPlayer getePlayer() {
		return ePlayer;
	}
	
	public int getRank() {
		return rank;
	}
	
	public int getValue() {
		return value;
	}
	
	
}
