package edu.moravian;

import edu.moravian.Exceptions.*;
import java.util.HashMap;

public class BotResponder {
    StockMarketGame game;
    private GameState gameState;

    public BotResponder(StockMarketGame game) {
        this.game = game;
        this.gameState = GameState.NO_GAME;
    }

    public String respond(String message, String playerName) {
        try {
            if (message.equals("!help"))
                return handleHelp();
            else if (message.equals("!status"))
                return handleStatus();
            else if (message.equals("!startSim"))
                return handleStart();
            else if (message.equals("!endSim"))
                return handleEnd();
            else if (message.equals("!players"))
                return handlePlayers();
            else if (message.equals("!join"))
                return handleJoin(playerName);
            else if (message.startsWith("!buy"))
                return handleBuy(message, playerName);
            else if (message.startsWith("!sell"))
                return handleSell(message, playerName);
            else if (message.equals("!portfolio"))
                return handlePortfolio(playerName);
            else if (message.equals("!market"))
                return handleMarketOverview();
            else if (message.equals("!next"))
                return handleNextRound();
            return BotResponses.unknownCommand(message);
        }
        catch (PlayerNotInGameException e) {
            return BotResponses.playerNotInGame(playerName);
        }
        catch (InternalSystemException | StorageException e) {
            return BotResponses.internalSystemError();
        }
    }

    private String handleHelp() {
        return BotResponses.availableCommands();
    }

    private String handleStatus() {
        switch (gameState) {
            case NO_GAME:
                return BotResponses.noGameInProgress();
            case IN_PROGRESS:
                return BotResponses.gameIsInProgress();
        }
        throw new InternalSystemException();
    }

    private String handleStart() {
        if (gameState.equals(GameState.IN_PROGRESS)) {
            return BotResponses.gameAlreadyInProgress();
        }
        //game.reset();
        //game.createInitialMarketValues();
        gameState = GameState.IN_PROGRESS;
        return BotResponses.gameSuccessfullyStarted();
    }

    private String handleEnd() {
        if (gameState.equals(GameState.NO_GAME)) {
            return BotResponses.noGameInProgress();
        }
        // Ends game and reports players and their portfolio values
        HashMap<String, Double> finalValues = new HashMap<>();
        for (String player : game.getPlayers()) {
            double totalValue = game.getPortfolioValue(player);
            finalValues.put(player, totalValue);
        }
        game.reset();
        gameState = GameState.NO_GAME;
        return BotResponses.gameSuccessfullyEnded(finalValues);
    }

    private String handlePlayers() {
        if (gameState.equals(GameState.NO_GAME)) {
            return BotResponses.noGameInProgress();
        }
        if (game.getPlayers().isEmpty()) {
            return BotResponses.noPlayersInGame();
        }
        return BotResponses.listOfPlayers(game.getPlayers());
    }

    private String handleJoin(String name) {
        if (gameState.equals(GameState.NO_GAME)) {
            return BotResponses.noGameInProgress();
        }
        try {
            game.addPlayer(name);
        } catch (PlayerAlreadyExistsException e) {
            return BotResponses.playerAlreadyInGame(name);
        }
        return BotResponses.playerSuccessfullyJoined(name);
    }

    private String handleBuy(String message, String playerName) {
        if (gameState.equals(GameState.NO_GAME)) {
            return BotResponses.noGameInProgress();
        }
        // Extract message
        String[] parts = message.split(" ");
        if (parts.length != 3) {
            return BotResponses.wrongUseOfBuyCommand();
        }
        double quantity;
        try {
            quantity = Double.parseDouble(parts[2]);
        } catch (NumberFormatException e) {
            return BotResponses.wrongUseOfBuyCommand();
        }

        // Attempt to buy stock
        try {
            game.buyStock(playerName, parts[1], quantity);
        }
        catch (InsufficientFundException e) {
            return BotResponses.insufficientFunds();
        }
        catch (InvalidStockException e) {
            return BotResponses.invalidStock(parts[1]);
        }
        catch (InvalidQuantityException e) {
            return BotResponses.invalidQuantity();
        }
        return BotResponses.stockSuccessfullyBought(playerName, parts[1], quantity);
    }

    private String handleSell(String message, String playerName) {
        if (gameState.equals(GameState.NO_GAME)) {
            return BotResponses.noGameInProgress();
        }
        // Extract message
        String[] parts = message.split(" ");
        if (parts.length != 3) {
            return BotResponses.wrongUseOfSellCommand();
        }
        double quantity;

        try {
            quantity = Double.parseDouble(parts[2]);
        } catch (NumberFormatException e) {
            return BotResponses.wrongUseOfSellCommand();
        }

        // Attempt to sell stock
        try {
            game.sellStock(playerName, parts[1], quantity);
        }
        catch (InsufficientStockException e) {
            return BotResponses.insufficientStocks();
        }
        catch (InvalidStockException e) {
            return BotResponses.invalidStock(parts[1]);
        }
        catch (InvalidQuantityException e) {
            return BotResponses.invalidQuantity();
        }
        return BotResponses.stockSuccessfullySold(playerName, parts[1], quantity);
    }

    private String handlePortfolio(String playerName) {
        if (gameState.equals(GameState.NO_GAME)) {
            return BotResponses.noGameInProgress();
        }
        double cash = game.getCash(playerName);
        double totalValue = game.getPortfolioValue(playerName);
        String portfolioOverview = game.getPortfolioOverview(playerName);
        return BotResponses.portfolioOverview(playerName, portfolioOverview, cash, totalValue);
    }

    private String handleMarketOverview() {
        if (gameState.equals(GameState.NO_GAME)) {
            return BotResponses.noGameInProgress();
        }
        return BotResponses.marketOverview(game.getMarketOverview());
    }

    private String handleNextRound() {
        if (gameState.equals(GameState.NO_GAME)) {
            return BotResponses.noGameInProgress();
        }
        game.progressToNextRound();
        return BotResponses.roundSuccessfullyProgressed(game.getMarketOverview());
    }
}
