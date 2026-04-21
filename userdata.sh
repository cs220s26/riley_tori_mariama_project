#!/bin/bash
yum install git redis6 maven-amazon-corretto21 -y
git clone https://github.com/cs220s26/riley_tori_mariama_ahmed
cd /riley_tori_mariama_ahmed
python3 -m venv .venv
source .venv/bin/activate
mvn package
cp discord_bot.service /etc/systemd/system
systemctl enable discord_bot.service 
systemctl start discord_bot.service
