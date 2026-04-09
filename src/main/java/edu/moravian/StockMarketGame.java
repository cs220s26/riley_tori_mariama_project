package edu.moravian;

import edu.moravian.Exceptions.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class StockMarketGame {

    private final StockMarketStorage storage;
    private final double BASE_CASH = 10000.0;

    public StockMarketGame(StockMarketStorage storage) {
        this.storage = storage;
    }

    public List<String> getPlayers() {
        return storage.getPlayers();
    }

    public void addPlayer(String playerName) throws PlayerAlreadyExistsException {
        if(storage.getPlayers().contains(playerName)) {
            throw new PlayerAlreadyExistsException();
        }
        storage.addPlayer(playerName, BASE_CASH);
    }

    public void reset() {
        storage.resetGame();
    }

    /**
     * Buy a specified quantity of stock for a player >0, and update cash balance
     * @param playerName the name of the player
     * @param stockSymbol the stock symbol to buy
     * @param quantity the quantity to buy
     * @throws StorageException issue with storage
     */
    public void buyStock(String playerName, String stockSymbol, double quantity) throws StorageException {
        if(!storage.getPlayers().contains(playerName)) {
            throw new PlayerNotInGameException(playerName);
        }
        if (quantity <= 0) {
            throw new InvalidQuantityException();
        }
        double stockPrice = storage.getStockValue(stockSymbol);
        double totalCost = stockPrice * quantity;
        double playerCash = storage.getPlayerCash(playerName);
        if (playerCash < totalCost) {
            throw new InsufficientFundException();
        }

        double currentQuantity = storage.getPlayerStockQuantity(playerName, stockSymbol);
        storage.setPlayerStockQuantity(playerName, stockSymbol, currentQuantity + quantity);
        storage.setPlayerCash(playerName, playerCash - totalCost);
    }

    /**
     * Sell a specified quantity of stock for a player >0, and update cash balance
     * @param playerName the name of the player
     * @param stockSymbol the stock symbol to sell
     * @param quantity the quantity to sell
     * @throws StorageException issue with storage
     */
    public void sellStock(String playerName, String stockSymbol, double quantity) throws StorageException {
        if(!storage.getPlayers().contains(playerName)) {
            throw new PlayerNotInGameException(playerName);
        }
        if (quantity <= 0) {
            throw new InvalidQuantityException();
        }
        if (!storage.getMarketSymbols().contains(stockSymbol)) {
            throw new InvalidStockException();
        }
        double currentQuantity = storage.getPlayerStockQuantity(playerName, stockSymbol);
        if (currentQuantity < quantity) {
            throw new InsufficientStockException();
        }

        double stockPrice = storage.getStockValue(stockSymbol);
        double totalProceeds = stockPrice * quantity;
        storage.setPlayerStockQuantity(playerName, stockSymbol, currentQuantity - quantity);
        double playerCash = storage.getPlayerCash(playerName);
        storage.setPlayerCash(playerName, playerCash + totalProceeds);
    }

    /**
     * Get an overview of a player's portfolio including stock holdings and cash
     * @param playerName the name of the player
     * @return formatted string of portfolio
     * @throws StorageException issue with storage
     */
    public String getPortfolioOverview(String playerName) throws StorageException {
        if(!storage.getPlayers().contains(playerName)) {
            throw new PlayerNotInGameException(playerName);
        }
        StringBuilder sb = new StringBuilder();
        Set<String> stocks = storage.getPlayerStocks(playerName);
        for (String stock : stocks) {
            double quantity = storage.getPlayerStockQuantity(playerName, stock);
            double price = storage.getStockValue(stock);
            sb.append(String.format("%s: %.2f shares @ $%.2f each\n", stock, quantity, price));
        }
        return sb.toString();
    }

    /**
     * Get an overview of the current market stock and their prices
     * @return formatted string of market
     */
    public String getMarketOverview() {
        StringBuilder sb = new StringBuilder();
        Set<String> symbols = storage.getMarketSymbols();
        for (String sym : symbols) {
            double price = storage.getStockValue(sym);
            sb.append(String.format("%s: $%.2f\n", sym, price));
        }
        return sb.toString();
    }

    /**
     * Calculate the total portfolio value for a player including cash and stock holdings
     * @param playerName the name of the player
     * @return the total portfolio value
     * @throws StorageException issue with storage
     */
    public double getPortfolioValue(String playerName) throws StorageException {
        if(!storage.getPlayers().contains(playerName)) {
            throw new PlayerNotInGameException(playerName);
        }
        double totalValue = 0.0;
        Set<String> stocks = storage.getPlayerStocks(playerName);
        for (String stock : stocks) {
            double quantity = storage.getPlayerStockQuantity(playerName, stock);
            double price = storage.getStockValue(stock);
            totalValue += quantity * price;
        }
        totalValue += storage.getPlayerCash(playerName);
        return totalValue;
    }

    /**
     * Randomly updates stock prices from -5% to +15%
     */
    public void progressToNextRound() {
        Set<String> symbols = storage.getMarketSymbols();
        for (String sym : symbols) {
            double currentPrice = storage.getStockValue(sym);
            double changePercent = (Math.random() * 20) - 5; // -5% to +15%
            double newPrice = currentPrice * (1 + changePercent / 100);
            storage.setStockValue(sym, newPrice);
        }
    }

    public double getCash(String playerName) throws StorageException {
        if(!storage.getPlayers().contains(playerName)) {
            throw new PlayerNotInGameException(playerName);
        }
        return storage.getPlayerCash(playerName);
    }

    /**
     * Initializes market values from a file
     */
    public void createInitialMarketValues() {
        try {
            File file = new File("data/MarketValues");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    String stockSymbol = parts[0].trim();
                    double stockValue = Double.parseDouble(parts[1].trim());
                    storage.setStockValue(stockSymbol, stockValue);
                }
            }
            scanner.close();
        } catch (IOException e) {
            throw new InternalSystemException();
        }
    }
}
