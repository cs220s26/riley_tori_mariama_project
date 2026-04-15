#!/bin/bash
# Create an empty game state in Redis
# Market data is pre-loaded; no players or portfolios exist
# Run with: bash scripts/create_empty_game.sh

# Start Redis server in the background (if not already running)
redis-server --daemonize yes

# Wait for Redis to be ready
echo "Waiting for Redis to start..."
until redis-cli ping 2>/dev/null | grep -q PONG; do
    sleep 0.5
done
echo "Redis is up."

# Clear any existing data
redis-cli FLUSHDB

# Market symbols and prices (from data/MarketValues)
redis-cli SADD market:symbols AAPL GOOGL AMZN TSLA NVDA META JPM MSFT
redis-cli HSET market:prices AAPL 270 GOOGL 320 AMZN 230 TSLA 430 NVDA 180 META 650 JPM 200 MSFT 290

echo "Redis ready: empty game state (market loaded, no players)"
