# Stock Market Simulation Bot
This bot simulates a stock market. Players can buy and sell stocks, track their portfolio, and compete with others to see who can make the most profit. The game is designed into rounds where each round randomly progresses the stock prices. The project is separated into three main components: A delivery mechanism (a Discord bot), a data storage mechanism (Redis/Memory), and the core game logic.

Project designed for CSCI220 DevOps.

# Contributers

- [Riley Trigo](https://github.com/RileyJTrigo) 
- [Mariama Diallo](https://github.com/diallom-max)
- [Ahmed Saeed](https://github.com/AhmedSaeed-CS)
- [Tori Champagne](https://github.com/champagnet04)

# Set Up
To set up this this project:

* First we need to install homebrew:

``` 
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

* to verify installation of homebrew type brew -h. Output should be:

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

* then install git:

```
brew install git
```

* then install java:

``` 
brew install openjdk
```

* then install maven:

```
brew install maven
```

* then install redis:

```
brew install redis
```

* Next, clone the repo and open the folder: 

```
git clone https://github.com/cs220s26/riley_tori_mariama_ahmed.git
cd riley_tori_mariama_ahmed
```

# Local Deploy
After cloning the repo, complete the following steps:

* Compile, Test, Package:

```
mvn package
```

* Bot token 
(we need to do that)


* in one terminal window run redis:

```
redis-server
```

* in a second terminal window run the bot
java -jar target/....


