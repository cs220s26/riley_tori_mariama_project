#!/bin/bash
yum install git -y
git clone https://github.com/cs220s26/riley_tori_mariama_ahmed
cd /riley_tori_mariama_ahmed
python3 -m venv .venv
.venv/bin/pip install -r requirements.txt 
cp discord_bot.service /etc/systemd/system
systemctl enable discord_bot.service 
systemctl start discord_bot.service
