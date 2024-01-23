package com.casino.blackjack;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

	private BlackJackGame game; 

    @MessageMapping("/placeBet")
    @SendTo("/topic/game")
    public BlackJackGame placeBet(Player player) {
        if (game == null) {
            // If the game is not initialized, create a new one
            game = new BlackJackGame();
        }
        // Add the player's betAmount to the existing game
        game.updateBetAmount(player.getBetAmount());
        return game;
    }
    
    @MessageMapping("/resetGame")
    @SendTo("/topic/game")
    public BlackJackGame resetGame() {
        // Reset the game state by creating a new BlackJackGame object
        game = new BlackJackGame();
        return game;
    }
    
    
    @MessageMapping("/drawCards")
    @SendTo("/topic/game")
    public BlackJackGame drawCards() {
    	if (game == null) {
            // If the game is not initialized, create a new one
            game = new BlackJackGame();
        }
    	
    	game.drawPlayerCard();
		game.drawDealerCard();
		game.drawPlayerCard();
//		game.drawDealerCard();
    	
        return game;
    }
    
    @MessageMapping("/hitCard")
    @SendTo("/topic/game")
    public BlackJackGame hitCard() 
    {
    	game.drawPlayerCard();
    	return game;
    }
    
    @MessageMapping("/playerStand")
    @SendTo("/topic/game")
    public BlackJackGame playerStand() 
    {
    	while(game.getDealerSum()<17 && game.getDealerSum()<=game.getPlayerSum() && game.getGameStatus()=="playing")
    	game.drawDealerCard();
    	
    	if(game.getGameStatus()=="playing")
    	{
    		if(game.getDealerSum()>21)
    			game.setGameStatus("Dealer Bust! Player Wins");
    		
    		else if(game.getDealerSum()>=game.getPlayerSum())
    			game.setGameStatus("Player Loses");
    		
    		else
    			game.setGameStatus("Player Wins");
    	}
    	
    	return game;
    }
}
