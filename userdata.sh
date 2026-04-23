#!/bin/bash
sudo yum install -y git redis6 maven-amazon-corretto21
git clone https://github.com/cs220s26/riley_tori_mariama_ahmed.git
cd /riley_tori_mariama_ahmed
sudo mvn package
sudo cp discord_bot.service /etc/systemd/system
sudo systemctl start redis6
sudo systemctl enable discord_bot.service 
sudo systemctl start discord_bot.service
./scripts/seed_active_game.sh
