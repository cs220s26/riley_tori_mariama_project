#!/bin/bash
sudo yum install -y git redis6 maven-amazon-corretto21 jq aws-cli
git clone https://github.com/cs220s26/riley_tori_mariama_ahmed.git
cd /riley_tori_mariama_ahmed
sudo mvn package

./register_dns.sh

sudo systemctl enable redis6
./scripts/seed_active_game.sh

sudo cp discord_bot.service /etc/systemd/system
sudo systemctl daemon-reload
sudo systemctl enable discord_bot.service 
sudo systemctl start discord_bot.service
