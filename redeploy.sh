sudo git pull origin main
sudo mvn clean 
sudo mvn package
sudo systemctl daemon-reload
sudo systemctl restart discord_bot.service
