package edu.moravian;

import java.util.HashMap;
import java.util.List;

public class BotResponses {
    public static String noGameInProgress() {
        return "No game in progress. Use !startSim to begin a new game.";
    }

    public static String noPlayersInGame() {
        return "No players have joined the game yet.";
    }

    public static String insufficientFunds() {
        return "Insufficient funds.";
    }

    public static String invalidStock(String stockSymbol) {
        return stockSymbol + " does not exist.";
    }

    public static String gameAlreadyInProgress() {
        return "Game already started.";
    }

    public static String gameSuccessfullyStarted() {
        return "Game has successfully started!";
    }

    public static String gameSuccessfullyEnded(HashMap<String, Double> finalStandings) {
        StringBuilder sb = new StringBuilder();
        sb.append("Game has ended! Final Standings:\n");
        for (String player : finalStandings.keySet()) {
            sb.append(String.format("- %s: $%.2f\n", player, finalStandings.get(player)));
        }
        return sb.toString();
    }

    public static String insufficientStocks() {
        return "Insufficient stocks.";
    }

    public static String availableCommands() {
        return  "`!help` - Show this help message" +
                "\n**Game Controls**" +
                "\n- `!startSim` - Starts the game" +
                "\n- `!endSim` - End the current game" +
                "\n- `!status` - Get the status of the game" +
                "\n- `!players` - List players in the current game" +
                "\n- `!join` - Join the current game" +
                "\n**Trading Commands**" +
                "\n- `!buy` <stockSymbol> <quantity> - Buy stocks" +
                "\n- `!sell` <stockSymbol> <quantity> - Sell stocks" +
                "\n- `!portfolio` - View your portfolio overview" +
                "\n- `!market` - View market overview" +
                "\n- `!next` - progress the market to the next round";
    }

    public static String playerAlreadyInGame(String playerName) {
        return "Player " + playerName + " has already joined the game.";
    }

    public static String playerSuccessfullyJoined(String playerName) {
        return "Player " + playerName + " has successfully joined the game.";
    }

    public static String invalidQuantity() {
        return "Invalid quantity specified. Must be greater than zero!";
    }

    public static String playerNotInGame(String playerName) {
        return "Player " + playerName + " is not in the game.";
    }

    public static String wrongUseOfBuyCommand() {
        return "\"Invalid Use: !buy <stockSymbol> <quantity>\"";
    }

    public static String wrongUseOfSellCommand() {
        return "\"Invalid Use: !sell <stockSymbol> <quantity>\"";
    }

    public static String internalSystemError() {
        return "Sorry, something went wrong on our end. Please try again later.";
    }

    public static String roundSuccessfullyProgressed(String marketOverview) {
        return "Market has progressed to the next round!\n" + marketOverview;
    }

    public static String gameIsInProgress() {
        return "A game is currently in progress.";
    }

    public static String listOfPlayers(List<String> players) {
        StringBuilder sb = new StringBuilder();
        sb.append("Players in the current game:\n");
        for (String player : players) {
            sb.append("- ").append(player).append("\n");
        }
        return sb.toString();
    }

    public static String stockSuccessfullyBought(String playerName, String stockName, double quantity) {
        return String.format("Player %s successfully bought %.2f shares of %s.", playerName, quantity, stockName);
    }

    public static String stockSuccessfullySold(String playerName, String stockName, double quantity) {
        return String.format("Player %s successfully sold %.2f shares of %s.", playerName, quantity, stockName);
    }

    public static String portfolioOverview(String playerName, String portfolioOverview, double cash, double totalValue) {
        return String.format("Portfolio Overview for %s:\n%sCash: $%.2f\nTotal Portfolio Value: $%.2f", playerName, portfolioOverview, cash, totalValue);
    }

    public static String marketOverview(String marketOverview) {
        return "Market Overview:\n" + marketOverview;
    }

    public static String unknownCommand(String message) {
        return "Unknown command: \"" + message + "\". Type !help for a list of available commands.";
    }
}
