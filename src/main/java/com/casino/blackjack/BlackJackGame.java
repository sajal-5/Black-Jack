package com.casino.blackjack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BlackJackGame {
	
private int betAmount;
private ArrayList<Integer>playerCards=new ArrayList<>();
private ArrayList<Integer>dealerCards=new ArrayList<>();
private int dealerSum;
private int playerSum;
private String gameStatus;

private Map<Integer, Integer>availableCards=new HashMap<Integer, Integer>();

	public BlackJackGame() {
		this.betAmount = 0;
		gameStatus="playing";
		dealerSum=0;
		playerSum=0;
		
		for(int i=1;i<=9;i++)
		 {
			 availableCards.put(i,4);
		 }
		 
		 playerCards.clear();
		 dealerCards.clear();
		 
	}

	public String getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

	public ArrayList<Integer> getPlayerCards() {
		return playerCards;
	}

	public ArrayList<Integer> getDealerCards() {
		return dealerCards;
	}

	public int getDealerSum() {
		return dealerSum;
	}

	public void updateDealerSum(int dealerSum) {
		this.dealerSum += dealerSum;
	}

	public int getPlayerSum() {
		return playerSum;
	}

	public void updatePlayerSum(int playerSum) {
		this.playerSum += playerSum;
	}

	public int getBetAmount() {
		return betAmount;
	}

	public void updateBetAmount(int betAmount) {
		this.betAmount += betAmount;
	}
	
	public void drawPlayerCard()
	{
		int newCard=drawCard();
		playerCards.add(newCard);
		updatePlayerSum(newCard);
		checkBlackJack();
	}
	
	public void drawDealerCard()
	{
		int newCard=drawCard();
		dealerCards.add(newCard);
		updateDealerSum(newCard);
		
	}
	
	public void checkBlackJack()
	{
		if(playerSum==21)
			gameStatus="Black Jack! Player Wins";
		
		else if(playerSum>21)
			gameStatus="Player Bust, Player Lose";
	}
	
	private int drawCard() {
	     int randomKey=getRandomKey(availableCards);
	     
	     int currentCount=availableCards.get(randomKey);
	     int updatedCount=currentCount-1;
	     
	     if (updatedCount == 0) {
	         // Remove the key if the updated value is 0
	         availableCards.remove(randomKey);
	     } else {
	         // Update the value in the HashMap
	    	 availableCards.put(randomKey, updatedCount);
	     }
	     
	     return randomKey;
	 }
	 
	 private static Integer getRandomKey(Map<Integer, Integer> mp) {

	     // Get a random index
	     int randomIndex = new Random().nextInt(mp.size());
	     
	     Integer i=0;
	     for(Integer key:mp.keySet())
	     {
	    	 if(i==randomIndex)
	    		 return key;

	    	 i++;
	     }
	     return i;
	 }

}