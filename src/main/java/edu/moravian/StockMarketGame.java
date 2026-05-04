package edu.moravian;

import edu.moravian.Exceptions.PlayerAlreadyExistsException;
import edu.moravian.Exceptions.PlayerNotInGameException;
import edu.moravian.Exceptions.InvalidQuantityException;
import edu.moravian.Exceptions.InvalidStockException;
import edu.moravian.Exceptions.InsufficientFundException;
import edu.moravian.Exceptions.InsufficientStockException;
import edu.moravian.Exceptions.InternalSystemException;
import edu.moravian.Exceptions.StorageException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class StockMarketGame {
    /** Handles all persistent storage operations for the game. */
    private final StockMarketStorage storage;

    /** Starting cash balance for each new player. */
    private final double BASE_CASH = 10000.0;

    public StockMarketGame(StockMarketStorage storage) {
        this.storage = storage;
    }

    public List<String> getPlayers() {
        return storage.getPlayers();
    }

    public void addPlayer(String playerName) throws PlayerAlreadyExistsException {
        if (storage.getPlayers().contains(playerName)) {
            throw new PlayerAlreadyExistsException();
        }
        storage.addPlayer(playerName, BASE_CASH);
    }

    public void reset() {
        storage.resetGame();
    }

    /**
     * Buy a specified quantity of stock for a player >0, and update cash balance.
     * @param playerName the name of the player
     * @param stockSymbol the stock symbol to buy
     * @param quantity the quantity to buy
     * @throws StorageException issue with storage
     */
    public void buyStock(String playerName, String stockSymbol, double quantity)
            throws StorageException {
        if (!storage.getPlayers().contains(playerName)) {
            throw new PlayerNotInGameException(playerName);
        }
        if (quantity <= 0) {
            throw new InvalidQuantityException();
        }
        final double stockPrice = storage.getStockValue(stockSymbol);
        final double totalCost = stockPrice * quantity;
        final double playerCash = storage.getPlayerCash(playerName);
        if (playerCash < totalCost) {
            throw new InsufficientFundException();
        }

        final double currentQuantity = storage.getPlayerStockQuantity(playerName, stockSymbol);
        storage.setPlayerStockQuantity(playerName, stockSymbol, currentQuantity + quantity);
        storage.setPlayerCash(playerName, playerCash - totalCost);
    }

    /**
     * Sell a specified quantity of stock for a player >0, and update cash balance.
     * @param playerName the name of the player
     * @param stockSymbol the stock symbol to sell
     * @param quantity the quantity to sell
     * @throws StorageException issue with storage
     */
    public void sellStock(String playerName, String stockSymbol, double quantity)
            throws StorageException {
        if (!storage.getPlayers().contains(playerName)) {
            throw new PlayerNotInGameException(playerName);
        }
        if (quantity <= 0) {
            throw new InvalidQuantityException();
        }
        if (!storage.getMarketSymbols().contains(stockSymbol)) {
            throw new InvalidStockException();
        }
        final double currentQuantity = storage.getPlayerStockQuantity(playerName, stockSymbol);
        if (currentQuantity < quantity) {
            throw new InsufficientStockException();
        }

        final double stockPrice = storage.getStockValue(stockSymbol);
        final double totalProceeds = stockPrice * quantity;
        storage.setPlayerStockQuantity(playerName, stockSymbol, currentQuantity - quantity);
        final double playerCash = storage.getPlayerCash(playerName);
        storage.setPlayerCash(playerName, playerCash + totalProceeds);
    }

    /**
     * Get an overview of a player's portfolio including stock holdings and cash.
     * @param playerName the name of the player
     * @return formatted string of portfolio
     * @throws StorageException issue with storage
     */
    public String getPortfolioOverview(String playerName) throws StorageException {
        if (!storage.getPlayers().contains(playerName)) {
            throw new PlayerNotInGameException(playerName);
        }
        final StringBuilder sb = new StringBuilder();
        final Set<String> stocks = storage.getPlayerStocks(playerName);
        for (String stock : stocks) {
            final double quantity = storage.getPlayerStockQuantity(playerName, stock);
            final double price = storage.getStockValue(stock);
            sb.append(String.format("%s: %.2f shares @ $%.2f each\n", stock, quantity, price));
        }
        return sb.toString();
    }

    /**
     * Get an overview of the current market stock and their prices.
     * @return formatted string of market
     */
    public String getMarketOverview() {
        final StringBuilder sb = new StringBuilder();
        final Set<String> symbols = storage.getMarketSymbols();
        for (String sym : symbols) {
            final double price = storage.getStockValue(sym);
            sb.append(String.format("%s: $%.2f\n", sym, price));
        }
        return sb.toString();
    }

    /**
     * Calculate the total portfolio value for a player including cash and stock holdings.
     * @param playerName the name of the player
     * @return the total portfolio value
     * @throws StorageException issue with storage
     */
    public double getPortfolioValue(String playerName) throws StorageException {
        if (!storage.getPlayers().contains(playerName)) {
            throw new PlayerNotInGameException(playerName);
        }
        double totalValue = 0.0;
        final Set<String> stocks = storage.getPlayerStocks(playerName);
        for (String stock : stocks) {
            final double quantity = storage.getPlayerStockQuantity(playerName, stock);
            final double price = storage.getStockValue(stock);
            totalValue += quantity * price;
        }
        totalValue += storage.getPlayerCash(playerName);
        return totalValue;
    }

    /**
     * Randomly updates stock prices from -5% to +15%.
     */
    public void progressToNextRound() {
        final Set<String> symbols = storage.getMarketSymbols();
        for (String sym : symbols) {
            final double currentPrice = storage.getStockValue(sym);
            final double changePercent = (Math.random() * 20) - 5; // -5% to +15%
            final double newPrice = currentPrice * (1 + changePercent / 100);
            storage.setStockValue(sym, newPrice);
        }
    }

    public double getCash(String playerName) throws StorageException {
        if (!storage.getPlayers().contains(playerName)) {
            throw new PlayerNotInGameException(playerName);
        }
        return storage.getPlayerCash(playerName);
    }

    /**
     * Initializes market values from a file.
     */
    public void createInitialMarketValues() {
        try {
            final File file = new File("data/MarketValues");
            final Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();
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
