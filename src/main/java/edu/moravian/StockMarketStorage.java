package edu.moravian;

import edu.moravian.Exceptions.StorageException;
import java.util.List;
import java.util.Set;

public interface StockMarketStorage {

    void resetGame() throws StorageException;

    void addPlayer(String player, double baseCash) throws StorageException;

    List<String> getPlayers() throws StorageException;

    double getPlayerCash(String player) throws StorageException;

    void setPlayerCash(String player, double cash) throws StorageException;

    Set<String> getPlayerStocks(String player) throws StorageException;

    double getPlayerStockQuantity(String player, String stock) throws StorageException;

    void setPlayerStockQuantity(String player, String stock, double qty) throws StorageException;

    void removePlayerStock(String player, String stock) throws StorageException;

    Set<String> getMarketSymbols() throws StorageException;

    double getStockValue(String symbol) throws StorageException;

    void setStockValue(String symbol, double price) throws StorageException;
}
