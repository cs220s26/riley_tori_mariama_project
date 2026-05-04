# Stock Market Simulation Bot
This bot simulates a stock market. Players can buy and sell stocks, track their portfolio, and compete with others to see who can make the most profit. The game is designed into rounds where each round randomly progresses the stock prices. The project is separated into three main components: A delivery mechanism (a Discord bot), a data storage mechanism (Redis/Memory), and the core game logic.

Project designed for CSCI220 DevOps.

# Contributors

- [Riley Trigo](https://github.com/RileyJTrigo) 
- [Mariama Diallo](https://github.com/diallom-max)
- [Ahmed Saeed](https://github.com/AhmedSaeed-CS)
- [Tori Champagne](https://github.com/champagnet04)

# Setup
To set up this project:

* First, we need to install homebrew:

``` 
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

* To verify installation of homebrew type ``` brew -h ```. Output should be:

```
Example usage:
  brew search TEXT|/REGEX/
  brew info [FORMULA|CASK...]
  brew install FORMULA|CASK...
  brew update
  brew upgrade [FORMULA|CASK...]
  brew uninstall FORMULA|CASK...
  brew list [FORMULA|CASK...]

Troubleshooting:
  brew config
  brew doctor
  brew install --verbose --debug FORMULA|CASK

Contributing:
  brew create URL [--no-fetch]
  brew edit [FORMULA|CASK...]

Further help:
  brew commands
  brew help [COMMAND]
  man brew
  https://docs.brew.sh
```

* Then install git:

```
brew install git
```

* Then install java:

``` 
brew install openjdk
```

* Then install maven:

```
brew install maven
```

* Then install redis:

```
brew install redis
```

* Next, clone the repo and open the folder: 

```
git clone https://github.com/cs220s26/riley_tori_mariama_ahmed.git
cd riley_tori_mariama_ahmed
```

# Local Deployment
After cloning the repo, complete the following steps:

* Compile, Test, and package:

```
mvn package
```

* To retrieve your bot token, you will need to create an AWS secret manager. Go through these steps:

1. Set up your credentials. Log in to AWS learner lab (https://awsacademy.instructure.com/login/canvas), after logging in start the lab, then click on AWS Details on the top right and next to AWS CLI click Show. Copy the credintials and store them in this file ~/.aws/credentials . Note: You will need to store new credentials every time you start the lab.

2. After storing your credentials, from the AWS console go to Secrets Manager. click Store a new secret. Choose secret type "Other type of secret". In "Key" put DISCORD_TOKEN. In Value, Store your own Discord Token. Click next. Name the secret "220_Discord_Token" note the capitalization. For the next steps, the default settings are fine so click next until you can store it. After clicking store, reload the page to see the secret.


* Now, in one terminal window run redis:

```
redis-server
```

* in a second terminal window run the bot

```
java -jar target/dbot-1.0.0-jar-with-dependencies.jar
```

# EC2 Deployment

* 1. Launch the instance with Permissions to access Secrets Manager:
While launching the instance, go to Advanced details and choose LabInstanceProfile for IAM instance profile. 

* 2. In the EC2 instance, Do the following:
Install Java and Maven:

```
sudo yum install -y maven-amazon-corretto21
```

Install Git and Clone the Repo:

```
sudo yum install -y git
git clone https://github.com/cs220s26/riley_tori_mariama_ahmed.git
```

Install and launch Redis:

```
sudo yum install -y redis6
sudo systemctl start redis6
```
To verify Redis is running with ping (if Redis is running it will respond with PONG)

```
redis6-cli ping
```

* 3. Build and Run the bot:

```
mvn package
java -jar target/dbot-1.0.0-jar-with-dependencies.jar
```






