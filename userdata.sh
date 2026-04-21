#!/bin/bash
yum install git redis6 maven-amazon-corretto21 -y
git clone https://github.com/cs220s26/riley_tori_mariama_ahmed
cd /riley_tori_mariama_ahmed
mvn package
cp discord_bot.service /etc/systemd/system
sudo systemctl start redis6
sudo systemctl enable discord_bot.service 
sudo systemctl start discord_bot.service
