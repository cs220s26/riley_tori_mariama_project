package edu.moravian;

import edu.moravian.Exceptions.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import java.util.*;

public class RedisStorage implements StockMarketStorage {
    private final Jedis jedis;

    public RedisStorage(String hostname, int port) throws StorageException {
        try {
            jedis = new Jedis(hostname, port);
            jedis.ping();
        } catch (JedisConnectionException e) {
            throw new StorageException();
        }
    }

    @Override
    public void resetGame() throws StorageException {
        try {
            jedis.flushDB();
        } catch (JedisConnectionException e) {
            throw new StorageException();
        }
    }

    @Override
    public void addPlayer(String name, double baseCash) throws StorageException {
        try {
            jedis.sadd("players", name);
            jedis.hset("portfolio:" + name, "cash", Double.toString(baseCash));
        } catch (JedisConnectionException e) {
            throw new StorageException();
        }
    }

    @Override
    public List<String> getPlayers() throws StorageException {
        try {
            Set<String> players = jedis.smembers("players");
            return new ArrayList<>(players);
        } catch (JedisConnectionException e) {
            throw new StorageException();
        }
    }

    @Override
    public double getPlayerCash(String playerName) throws StorageException {
        try {
            String cash = jedis.hget("portfolio:" + playerName, "cash");
            if (cash == null) return 0.0;
            return Double.parseDouble(cash);
        } catch (JedisConnectionException | NumberFormatException e) {
            throw new StorageException();
        }
    }

    @Override
    public void setPlayerCash(String playerName, double cash) throws StorageException {
        try {
            jedis.hset("portfolio:" + playerName, "cash", Double.toString(cash));
        } catch (JedisConnectionException e) {
            throw new StorageException();
        }
    }

    @Override
    public Set<String> getPlayerStocks(String playerName) throws StorageException {
        try {
            Map<String, String> entirePortfolio = jedis.hgetAll("portfolio:" + playerName);

            Set<String> stocks = new TreeSet<>();
            for (String field : entirePortfolio.keySet()) {
                if (!field.equals("cash")) {
                    stocks.add(field);
                }
            }
            return stocks;

        } catch (JedisConnectionException e) {
            throw new StorageException();
        }
    }

    @Override
    public double getPlayerStockQuantity(String playerName, String stockSymbol)
            throws StorageException {
        try {
            String qty = jedis.hget("portfolio:" + playerName, stockSymbol);
            if (qty == null) return 0.0;
            return Double.parseDouble(qty);
        } catch (JedisConnectionException | NumberFormatException e) {
            throw new StorageException();
        }
    }

    @Override
    public void setPlayerStockQuantity(String playerName, String stockSymbol, double qty)
            throws StorageException {
        try {
            if (!jedis.sismember("players", playerName)) {
                throw new PlayerNotInGameException(playerName);
            }

            jedis.hset("portfolio:" + playerName, stockSymbol, Double.toString(qty));
        } catch (JedisConnectionException e) {
            throw new StorageException();
        }
    }

    @Override
    public void removePlayerStock(String playerName, String stockSymbol) throws StorageException {
        try {
            if (!jedis.sismember("players", playerName)) {
                throw new PlayerNotInGameException(playerName);
            }
            jedis.hdel("portfolio:" + playerName, stockSymbol);
        } catch (JedisConnectionException e) {
            throw new StorageException();
        }
    }

    @Override
    public Set<String> getMarketSymbols() throws StorageException {
        try {
            return jedis.smembers("market:symbols");
        } catch (JedisConnectionException e) {
            throw new StorageException();
        }
    }

    @Override
    public double getStockValue(String stockSymbol) throws StorageException {
        try {
            String val = jedis.hget("market:prices", stockSymbol);
            if (val == null) {
                throw new InvalidStockException();
            }
            return Double.parseDouble(val);
        } catch (JedisConnectionException | NumberFormatException e) {
            throw new StorageException();
        }
    }

    @Override
    public void setStockValue(String stockSymbol, double price) throws StorageException {
        try {
            jedis.hset("market:prices", stockSymbol, Double.toString(price));
            jedis.sadd("market:symbols", stockSymbol);
        } catch (JedisConnectionException e) {
            throw new StorageException();
        }
    }
}