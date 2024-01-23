const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/websocket-endpoint'
});

window.onbeforeunload = function() {
    // Call the resetGame function to reset the game state
    disconnect();
};

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/game', (tableData) => {
        showTableData(JSON.parse(tableData.body));
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#drawCards").prop("disabled", true);
    $("#send").prop("disabled", !connected);
    $("#disconnect").prop("disabled", !connected);
    
    $("#stand").prop("disabled", true);
    $("#hit").prop("disabled", true);
    
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
	resetGame();
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendBet() {
    // Convert the betAmount to a number
    var betAmount = parseInt($("#betAmount").val(), 10);

    // Check if the conversion was successful
    if (isNaN(betAmount)) {
        console.error("Invalid betAmount. Please enter a valid number.");
        return;
    }

    // Use the number for communication
    stompClient.publish({
        destination: "/app/placeBet",
        body: JSON.stringify({'betAmount': betAmount})
    });
    
    $("#drawCards").prop("disabled", false);
}

function resetGame()
{
	$("#verdict").html("");
	stompClient.publish({
		destination: "/app/resetGame"
	});
}

function drawCards()
{
	$("#send").prop("disabled", true);
	$("#drawCards").prop("disabled", true);
	stompClient.publish({
        destination: "/app/drawCards"
    });
    
    $("#stand").prop("disabled", false);
    $("#hit").prop("disabled", false);
}

function hitCard()
{
	stompClient.publish({
        destination: "/app/hitCard"
    });
}

function playerStand()
{
	$("#hit").prop("disabled", true);
    $("#stand").prop("disabled", true);
	stompClient.publish({
        destination: "/app/playerStand"
    });
}

function showTableData(data) {
	
	var betAmount=data.betAmount;
	var playerSum=data.playerSum;
	var dealerSum=data.dealerSum;
	var playerCards=data.playerCards;
	var dealerCards=data.dealerCards;
	
    $("#greetings").append("<tr><td>" + betAmount + "</td><td>" + playerCards+" -> "+playerSum + "</td><td>" + dealerCards+" -> " +dealerSum + "</td></tr>");
    
    var gameStatus=data.gameStatus;
	
	if(gameStatus!="playing")
	{
		$("#verdict").append("<h2 >"+gameStatus+"</h2>");
		gameEnd(gameStatus);
		//disconnect();
	}
}

function gameEnd(gameStatus)
{
	console.log(gameStatus);
	$("#hit").prop("disabled", true);
    $("#stand").prop("disabled", true);
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendBet());
    $("#drawCards").click(()=>drawCards());
    $('#hit').click(()=>hitCard());
    $('#stand').click(()=>playerStand());
});