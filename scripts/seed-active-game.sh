#!/bin/bash
# Seed Redis with an active game state
# Players: riley, tori, mariama, ahmed
# Run with: bash scripts/seed-active-game.sh

# Start Redis server in the background (if not already running)
sudo systemctl start redis6

# Wait for Redis to be ready
echo "Waiting for Redis to start..."
until redis6-cli ping 2>/dev/null | grep -q PONG; do
    sleep 0.5
done
echo "Redis is up."

# Clear any existing data
redis6-cli FLUSHDB

# Market symbols and prices (from data/MarketValues)
redis6-cli SADD market:symbols AAPL GOOGL AMZN TSLA NVDA META JPM MSFT
redis6-cli HSET market:prices AAPL 270 GOOGL 320 AMZN 230 TSLA 430 NVDA 180 META 650 JPM 200 MSFT 290

# Players
redis6-cli SADD players riley tori_moravian_account mariama ahmed

# riley: lots of cash, a few safe picks
redis6-cli HSET portfolio:riley cash 7500.00 AAPL 10 MSFT 5

# tori: spread across several stocks
redis6-cli HSET portfolio:tori cash 3200.00 GOOGL 3 NVDA 8 JPM 4

# mariama: big bet on TSLA
redis6-cli HSET portfolio:mariama cash 1000.00 TSLA 12 META 2

# ahmed: mostly cash, one stock
redis6-cli HSET portfolio:ahmed cash 9100.00 AMZN 4

echo "Redis seeded: active game state with players riley, tori, mariama, and ahmed"
