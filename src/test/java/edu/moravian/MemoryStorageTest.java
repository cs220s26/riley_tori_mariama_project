package edu.moravian;

import edu.moravian.Exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemoryStorageTest {
    MemoryStorage storage;
    @BeforeEach
    public void setup() {
        storage = new MemoryStorage();
    }

    @Test
    public void initiallyNoPlayers() {
        assertEquals(List.of(), storage.getPlayers());
    }

    @Test
    public void initiallyMarketIsEmpty() {
        assertEquals(0, storage.getMarketSymbols().size());
    }

    @Test
    public void canAddPlayer() {
        storage.addPlayer("Bob", 10000.0);
        assertEquals(List.of("Bob"), storage.getPlayers());
    }

    @Test
    public void resetClearsAllPlayers() {
        storage.addPlayer("Alice", 10000.0);
        storage.addPlayer("Bob", 10000.0);
        storage.resetGame();
        assertEquals(List.of(), storage.getPlayers());
    }

    @Test
    public void canSetPlayerCash() {
        storage.addPlayer("Charlie", 5000.0);
        assertEquals(5000.0, storage.getPlayerCash("Charlie"));
        storage.setPlayerCash("Charlie", 7500.0);
        assertEquals(7500.0, storage.getPlayerCash("Charlie"));
    }

    @Test
    public void resetClearsPlayerCash() {
        storage.addPlayer("Alice", 10000.0);
        storage.resetGame();
        assertEquals(0.0, storage.getPlayerCash("Alice"));
    }

    @Test
    public void canSetPlayerStockQuantity() {
        storage.addPlayer("Bob", 10000.0);
        storage.setPlayerStockQuantity("Bob", "AAPL", 20.0);
        assertEquals(20.0, storage.getPlayerStockQuantity("Bob", "AAPL"));
    }

    @Test
    public void resetClearsPlayerStocks() {
        storage.addPlayer("Alice", 10000.0);
        storage.setPlayerStockQuantity("Alice", "AAPL", 10.0);
        storage.resetGame();
        assertEquals(0, storage.getPlayerStockQuantity("Alice", "AAPL"));
    }

    @Test
    public void canSetStockValues() {
        storage.setStockValue("MSFT", 250.0);
        assertEquals(250.0, storage.getStockValue("MSFT"));
    }

    @Test
    public void resetClearsMarketValues() {
        storage.setStockValue("Stock1", 150.0);
        storage.resetGame();
        assertFalse(storage.getMarketSymbols().contains("Stock1"));
    }

    @Test
    public void removePlayerStocks_CompletelyRemoves() {
        storage.addPlayer("Dana", 8000.0);
        storage.setPlayerStockQuantity("Dana", "GOOGL", 15.0);
        assertEquals(15.0, storage.getPlayerStockQuantity("Dana", "GOOGL"));
        storage.removePlayerStock("Dana", "GOOGL");
        assertFalse(storage.getPlayerStocks("Dana").contains("GOOGL"));
    }
}