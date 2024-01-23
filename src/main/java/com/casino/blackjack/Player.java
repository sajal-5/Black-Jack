package com.casino.blackjack;

public class Player {
	private int betAmount;
	
	public Player()
	{
	}

	public Player(int betAmount) {
		this.betAmount = betAmount;
	}

	public int getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(int betAmount) {
		this.betAmount = betAmount;
	}
	
	
}
