package edu.moravian;

import edu.moravian.Exceptions.*;
import java.util.*;

public class MemoryStorage implements StockMarketStorage {
    private List<String> players;
    private HashMap<String, Double> playerCash;
    private HashMap<String, HashMap<String, Double>> playerStocks;
    private HashMap<String, Double> marketValues;

    public MemoryStorage() {
        this.players = new ArrayList<>();
        this.playerCash = new HashMap<>();
        this.playerStocks = new HashMap<>();
        this.marketValues = new HashMap<>();
    }

    @Override
    public void resetGame() {
        players.clear();
        playerCash.clear();
        playerStocks.clear();
        marketValues.clear();
    }

    @Override
    public void addPlayer(String player, double baseCash) throws PlayerAlreadyExistsException {
        players.add(player);
        playerCash.put(player, baseCash);
        playerStocks.put(player, new HashMap<>());
    }

    @Override
    public List<String> getPlayers() {
        return new ArrayList<>(players);
    }

    @Override
    public double getPlayerCash(String player) {
        return playerCash.getOrDefault(player, 0.0);
    }

    @Override
    public void setPlayerCash(String player, double cash) {
        playerCash.put(player, cash);
    }

    @Override
    public Set<String> getPlayerStocks(String player) {
        return playerStocks.getOrDefault(player, new HashMap<>()).keySet();
    }

    @Override
    public double getPlayerStockQuantity(String player, String stock) {
        HashMap<String, Double> stocks = playerStocks.get(player);
        if (stocks == null) {
            return 0.0;
        }
        return stocks.getOrDefault(stock, 0.0);
    }

    @Override
    public void setPlayerStockQuantity(String player, String stock, double qty) {
        HashMap<String, Double> stocks = playerStocks.get(player);
        if (stocks != null) {
            stocks.put(stock, qty);
        }
    }

    @Override
    public void removePlayerStock(String player, String stock) {
        HashMap<String, Double> stocks = playerStocks.get(player);
        if (stocks != null) {
            stocks.remove(stock);
        }
    }

    @Override
    public Set<String> getMarketSymbols() {
        return marketValues.keySet();
    }

    @Override
    public double getStockValue(String symbol) {
        Double value = marketValues.get(symbol);
        if (value == null) {
            throw new InvalidStockException();
        }
        return value;
    }

    @Override
    public void setStockValue(String symbol, double price) {
        marketValues.put(symbol, price);
    }
}
