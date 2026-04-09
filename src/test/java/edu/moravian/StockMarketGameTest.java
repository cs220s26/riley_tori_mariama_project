package edu.moravian;

import edu.moravian.Exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StockMarketGameTest {
    private StockMarketGame game;
    @BeforeEach
    public void createEmptyGame() {
        StockMarketStorage storage = new MemoryStorage();
        game = new StockMarketGame(storage);
        game.createInitialMarketValues();
    }

    @Test
    public void playerCannotJoinTwice() {
        game.addPlayer("Bob");
        assertThrows(PlayerAlreadyExistsException.class, () -> game.addPlayer("Bob"));
    }

    @Test
    public void playerNoInGameCannotBuyStock() {
        assertThrows(PlayerNotInGameException.class, () -> game.buyStock("Alice", "AAPL", 1));
    }

    @Test
    public void playerCanBuyStock_WhenTheyHaveEnoughFunds() throws InsufficientFundException {
        game.addPlayer("Bob");
        assertDoesNotThrow(() -> game.buyStock("Bob", "AAPL", 1));
    }

    @Test
    public void playerCannotBuyInvalidStock() {
        game.addPlayer("Bob");
        assertThrows(InvalidStockException.class, () -> game.buyStock("Bob", "INVALID", 1));
    }

    @Test void playerCannotBuyZeroStock() {
        game.addPlayer("Bob");
        assertThrows(InvalidQuantityException.class, () -> game.buyStock("Bob", "AAPL", 0));
    }

    @Test
    public void playerCannotBuyStock_WhenTheyLackFunds() {
        game.addPlayer("Bob");
        assertThrows(InsufficientFundException.class, () -> game.buyStock("Bob", "GOOGL", 1000));
    }

    @Test
    public void playerNoInGameCannotSellStock() {
        assertThrows(PlayerNotInGameException.class, () -> game.sellStock("Alice", "AAPL", 1));
    }

    @Test
    public void playerCanSellStockTheyOwn() {
        game.addPlayer("Bob");
        assertDoesNotThrow(() -> {
            game.buyStock("Bob", "AAPL", 2);
            game.sellStock("Bob", "AAPL", 2);
        });
    }

    @Test
    public void playerCannotSellInvalidStock() {
        game.addPlayer("Bob");
        assertThrows(InvalidStockException.class, () -> game.sellStock("Bob", "INVALID", 1));
    }

    @Test void playerCannotSellZeroStock() {
        game.addPlayer("Bob");
        assertThrows(InvalidQuantityException.class, () -> game.sellStock("Bob", "AAPL", 0));
    }

    @Test
    public void playerCannotSellStock_TheyDoNotOwn() {
        game.addPlayer("Bob");
        assertThrows(InsufficientStockException.class, () -> game.sellStock("Bob", "AAPL", 1));
    }

    @Test
    public void playerCannotSellStock_IfTheyLackEnough() {
        game.addPlayer("Bob");
        assertDoesNotThrow(() -> game.buyStock("Bob", "AAPL", 2));
        assertThrows(InsufficientStockException.class, () -> game.sellStock("Bob", "AAPL", 3));
    }

    @Test
    public void playerNotInGameCannotGetPortfolioOverview() {
        assertThrows(PlayerNotInGameException.class, () -> game.getPortfolioOverview("Alice"));
    }
}