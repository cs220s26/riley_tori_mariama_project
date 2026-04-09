package edu.moravian;

import com.github.fppt.jedismock.RedisServer;
import edu.moravian.Exceptions.InvalidStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RedisStorageTest {
    private RedisServer server;

    @BeforeEach
    public void setUp() throws IOException {
        server = RedisServer.newRedisServer();
        server.start();
    }

    @Test
    public void initiallyNoPlayers() {
        RedisStorage storage = new RedisStorage(server.getHost(), server.getBindPort());
        assertEquals(List.of(), storage.getPlayers());
    }

    @Test
    public void initiallyMarketIsEmpty() {
        RedisStorage storage = new RedisStorage(server.getHost(), server.getBindPort());
        assertEquals(0, storage.getMarketSymbols().size());
    }

    @Test
    public void canAddPlayer() {
        RedisStorage storage = new RedisStorage(server.getHost(), server.getBindPort());
        storage.addPlayer("Bob", 10000.0);
        assertEquals(List.of("Bob"), storage.getPlayers());
    }

    @Test
    public void resetClearsAllPlayers() {
        RedisStorage storage = new RedisStorage(server.getHost(), server.getBindPort());
        storage.addPlayer("Alice", 10000.0);
        storage.addPlayer("Bob", 10000.0);
        storage.resetGame();
        assertEquals(List.of(), storage.getPlayers());
    }

    @Test
    public void canSetPlayerCash() {
        RedisStorage storage = new RedisStorage(server.getHost(), server.getBindPort());
        storage.addPlayer("Charlie", 5000.0);
        assertEquals(5000.0, storage.getPlayerCash("Charlie"));
        storage.setPlayerCash("Charlie", 7500.0);
        assertEquals(7500.0, storage.getPlayerCash("Charlie"));
    }

    @Test
    public void resetClearsPlayerCash() {
        RedisStorage storage = new RedisStorage(server.getHost(), server.getBindPort());
        storage.addPlayer("Alice", 10000.0);
        storage.resetGame();
        assertEquals(0.0, storage.getPlayerCash("Alice"));
    }

    @Test
    public void canSetPlayerStockQuantity() {
        RedisStorage storage = new RedisStorage(server.getHost(), server.getBindPort());
        storage.addPlayer("Bob", 10000.0);
        storage.setPlayerStockQuantity("Bob", "AAPL", 20.0);
        assertEquals(20.0, storage.getPlayerStockQuantity("Bob", "AAPL"));
    }

    @Test
    public void resetClearsPlayerStocks() {
        RedisStorage storage = new RedisStorage(server.getHost(), server.getBindPort());
        storage.addPlayer("Alice", 10000.0);
        storage.setPlayerStockQuantity("Alice", "AAPL", 10.0);
        storage.resetGame();
        assertEquals(0, storage.getPlayerStockQuantity("Alice", "AAPL"));
    }

    @Test
    public void canSetStockValues() {
        RedisStorage storage = new RedisStorage(server.getHost(), server.getBindPort());
        storage.setStockValue("MSFT", 250.0);
        assertEquals(250.0, storage.getStockValue("MSFT"));
    }

    @Test
    public void resetClearsMarketValues() {
        RedisStorage storage = new RedisStorage(server.getHost(), server.getBindPort());
        storage.setStockValue("TSLA", 600.0);
        storage.resetGame();
        assertThrows(InvalidStockException.class, () -> storage.getStockValue("TSLA"));
    }

    @Test
    public void removePlayerStocks_CompletelyRemoves() {
        RedisStorage storage = new RedisStorage(server.getHost(), server.getBindPort());
        storage.addPlayer("Dave", 10000.0);
        storage.setPlayerStockQuantity("Dave", "GOOGL", 15.0);
        storage.removePlayerStock("Dave", "GOOGL");
        assertEquals(0, storage.getPlayerStockQuantity("Dave", "GOOGL"));
    }
}