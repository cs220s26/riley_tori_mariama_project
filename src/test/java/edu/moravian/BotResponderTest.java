package edu.moravian;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class BotResponderTest {
    private BotResponder responder;
    private StockMarketGame game;

    @BeforeEach
    public void createBotResponder() {
        MemoryStorage storage = new MemoryStorage();
        game = new StockMarketGame(storage);
        responder = new BotResponder(game);
    }

    @Test
    public void botProvidesHelpMessage() {
        String expected = BotResponses.availableCommands();
        assertEquals(expected, responder.respond("!help", "Alice"));
    }

    @Test
    public void botReportsUnknownCommand() {
        String expected = BotResponses.unknownCommand("!foobar");
        assertEquals(expected, responder.respond("!foobar", "Alice"));
    }

    @Test
    public void botReportsNoGameInitially() {
        String expected = BotResponses.noGameInProgress();
        assertEquals(expected, responder.respond("!status", "Alice"));
    }

    @Test
    public void botReportsGameStarted() {
        String expected = BotResponses.gameSuccessfullyStarted();
        assertEquals(expected, responder.respond("!startSim", "Alice"));
    }

    @Test
    public void botReportsGameInProgressAfterStart() {
        responder.respond("!startSim", "Alice");
        String expected = BotResponses.gameIsInProgress();
        assertEquals(expected, responder.respond("!status", "Alice"));
    }

    @Test
    public void botReportsAGameAlreadyInProgress() {
        responder.respond("!startSim", "Alice");
        String expected = BotResponses.gameAlreadyInProgress();
        assertEquals(expected, responder.respond("!startSim", "Bob"));
    }

    @Test
    public void botReportsNoPlayersInGame() {
        responder.respond("!startSim", "Alice");
        String expected = BotResponses.noPlayersInGame();
        assertEquals(expected, responder.respond("!players", "Alice"));
    }

    @Test
    public void botReportsNoGame_ForInGameCommands() {
        String expected = BotResponses.noGameInProgress();
        assertEquals(expected, responder.respond("!buy AAPL 10", "Alice"));
        assertEquals(expected, responder.respond("!sell AAPL 5", "Alice"));
        assertEquals(expected, responder.respond("!portfolio", "Alice"));
        assertEquals(expected, responder.respond("!market", "Alice"));
        assertEquals(expected, responder.respond("!next", "Alice"));
        assertEquals(expected, responder.respond("!join", "Alice"));
        assertEquals(expected, responder.respond("!endSim", "Alice"));
        assertEquals(expected, responder.respond("!players", "Alice"));
    }

    @Test
    public void botReportsPlayerJoinedGame() {
        responder.respond("!startSim", "Alice");
        String expected = BotResponses.playerSuccessfullyJoined("Bob");
        assertEquals(expected, responder.respond("!join", "Bob"));
    }

    @Test
    public void botReportsPlayerHasAlreadyJoined() {
        String expected = BotResponses.playerAlreadyInGame("Bob");
        responder.respond("!startSim", "Bob");
        responder.respond("!join", "Bob");
        assertEquals(expected, responder.respond("!join", "Bob"));
    }

    @Test
    public void botReportsPlayerNotInGame_ForTradingCommands() {
        responder.respond("!startSim", "Alice");
        responder.respond("!join", "Alice");
        String expected = BotResponses.playerNotInGame("Bob");
        assertEquals(expected, responder.respond("!buy AAPL 10", "Bob"));
        assertEquals(expected, responder.respond("!sell AAPL 5", "Bob"));
        assertEquals(expected, responder.respond("!portfolio", "Bob"));
    }

    @Test
    public void botReportsGameEndedSuccessfully() {
        responder.respond("!startSim", "Alice");
        responder.respond("!join", "Alice");
        responder.respond("!join", "Bob");
        HashMap<String, Double> finalValues = new HashMap<>();
        finalValues.put("Alice", 10000.0);
        finalValues.put("Bob", 10000.0);
        String expected = BotResponses.gameSuccessfullyEnded(finalValues);
        assertEquals(expected, responder.respond("!endSim", "Alice"));
    }

    @Test
    public void botReportsPlayersInGame() {
        responder.respond("!startSim", "Alice");
        responder.respond("!join", "Alice");
        responder.respond("!join", "Bob");
        ArrayList<String> players = new ArrayList<>();
        players.add("Alice");
        players.add("Bob");
        String expected = BotResponses.listOfPlayers(players);
        assertEquals(expected, responder.respond("!players", "Alice"));
    }
    @Test
    public void botReportsInvalidUseOfBuyCommand() {
        responder.respond("!startSim", "Alice");
        responder.respond("!join", "Alice");
        String expected = BotResponses.wrongUseOfBuyCommand();
        assertEquals(expected, responder.respond("!buy AAPL", "Alice"));
        assertEquals(expected, responder.respond("!buy", "Alice"));
        assertEquals(expected, responder.respond("!buy AAPL aaa", "Alice"));
    }

    @Test
    public void botreportsInsufficientFundsWhenBuying() {
        responder.respond("!startSim", "Alice");
        responder.respond("!join", "Alice");
        String expected = BotResponses.insufficientFunds();
        assertEquals(expected, responder.respond("!buy AAPL 1000000", "Alice"));
    }

    @Test
    public void botReportsStockDoesNotExistWhenBuying() {
        responder.respond("!startSim", "Alice");
        responder.respond("!join", "Alice");
        String expected = BotResponses.invalidStock("XXXX");
        assertEquals(expected, responder.respond("!buy XXXX 10", "Alice"));
    }

    @Test
    public void botReportsQuantityMustBePositiveWhenBuying() {
        responder.respond("!startSim", "Alice");
        responder.respond("!join", "Alice");
        String expected = BotResponses.invalidQuantity();
        assertEquals(expected, responder.respond("!buy AAPL -5", "Alice"));
        assertEquals(expected, responder.respond("!buy AAPL 0", "Alice"));
    }

    @Test
    public void botReportsSuccessfulPurchase() {
        responder.respond("!startSim", "Alice");
        responder.respond("!join", "Alice");
        String expected = BotResponses.stockSuccessfullyBought("Alice", "AAPL", 10);
        assertEquals(expected, responder.respond("!buy AAPL 10", "Alice"));
    }

    @Test
    public void botReportsInvalidUseOfSellCommand() {
        responder.respond("!startSim", "Alice");
        responder.respond("!join", "Alice");
        String expected = BotResponses.wrongUseOfSellCommand();
        assertEquals(expected, responder.respond("!sell AAPL", "Alice"));
        assertEquals(expected, responder.respond("!sell", "Alice"));
        assertEquals(expected, responder.respond("!sell AAPL bbb", "Alice"));
    }

    @Test
    public void botreportsInsufficientStockWhenSelling() {
        responder.respond("!startSim", "Alice");
        responder.respond("!join", "Alice");
        String expected = BotResponses.insufficientStocks();
        assertEquals(expected, responder.respond("!sell AAPL 10", "Alice"));
    }

    @Test
    public void botReportsStockDoesNotExistWhenSelling() {
        responder.respond("!startSim", "Alice");
        responder.respond("!join", "Alice");
        String expected = BotResponses.invalidStock("XXXX");
        assertEquals(expected, responder.respond("!sell XXXX 10", "Alice"));
    }

    @Test
    public void botReportsQuantityMustBePositiveWhenSelling() {
        responder.respond("!startSim", "Alice");
        responder.respond("!join", "Alice");
        String expected = BotResponses.invalidQuantity();
        assertEquals(expected, responder.respond("!sell AAPL -5", "Alice"));
        assertEquals(expected, responder.respond("!sell AAPL 0", "Alice"));
    }

    @Test
    public void botReportsSuccessfulSale() {
        responder.respond("!startSim", "Alice");
        responder.respond("!join", "Alice");
        responder.respond("!buy AAPL 10", "Alice");
        String expected = BotResponses.stockSuccessfullySold("Alice", "AAPL", 5);
        assertEquals(expected, responder.respond("!sell AAPL 5", "Alice"));
    }
}