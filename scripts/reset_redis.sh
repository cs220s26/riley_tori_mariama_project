#!/bin/bash
# Reset Redis to a completely empty state
# Run with: bash scripts/reset_redis.sh

# Start Redis if needed
sudo systemctl start redis6

echo "Waiting for Redis to start..."
until redis6-cli ping 2>/dev/null | grep -q PONG; do
    sleep 0.5
done
echo "Redis is up."

# Clear all data
redis6-cli FLUSHDB

echo "Redis has been reset (empty database)"
