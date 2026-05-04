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

# CI Setup
* This project uses GitHub Actions for Continuous Integration. The CI workflow file is located in:
```
.github/workflows/ci.yml
```
* The `.github` folder is used by GitHub to store workflow files. Inside the `.github/workflows` folder, the `ci.yml` file tells GitHub Actions what to do when the workflow runs.
* The CI workflow is named `CI`:
```
name: CI
```
* The workflow runs on GitHub's latest Ubuntu environment:
```
runs-on: ubuntu-latest
```
* First, the workflow checks out the repository code:
```
uses: actions/checkout@v4
```
* Then it sets up Java 21 using Amazon Corretto:
```
uses: actions/setup-java@v4
with:
  java-version: '21
# CI Execution
* The CI workflow runs automatically every time code is pushed to the GitHub repository:
```
on: [push]
```
* When a push happens, GitHub Actions starts the workflow named `CI`.
* GitHub creates a temporary Ubuntu environment to run the workflow:
```
runs-on: ubuntu-latest
```
* After the environment is created, GitHub Actions checks out the project code, sets up Java 21, and runs the Maven test command:
```
mvn test
```
* The `mvn test` command runs checkstyle and unit tests for the project.
* If the tests and checkstyle pass, the CI workflow finishes successfully.
* If the tests fail, checkstyle fails, or the project does not build correctly, the CI workflow fails. The error can be viewed in the GitHub repository under the Actions tab.
# CD Setup
* This project uses GitHub Actions for Continuous Deployment to redeploy the bot on AWS EC2. The CD workflow file is located in:
```
.github/workflows/redeploy.yml
```
* The CD workflow is named `Redeploy on AWS`:
```
name: Redeploy on AWS
```
* This workflow is set up to be triggered manually from the GitHub Actions tab:
```
on:
  workflow_dispatch:
```
* The workflow runs on GitHub's latest Ubuntu environment:
```
runs-on: ubuntu-latest
```
* The deployment uses SSH to connect to the EC2 instance. It uses the `appleboy/ssh-action@v1` GitHub Action:
```
uses: appleboy/ssh-action@v1
```
* The workflow connects to the EC2 instance using the EC2 host, the username `ec2-user`, and the private key stored in GitHub Secrets:
```
host: ${{ secrets.EC2_HOST }}
username: ec2-user
key: ${{ secrets.LABSUSERPEM }}
```
* The following GitHub Secrets are needed for the CD workflow:
```
EC2_HOST
LABSUSERPEM
```
* `EC2_HOST` stores the public IP address or DNS name of the EC2 instance.
* `LABSUSERPEM` stores the private key used to SSH into the EC2 instance.
* These secrets can be added by going to the GitHub repository, then:
```
Settings -> Secrets and variables -> Actions -> New repository secret
```
* After the workflow connects to the EC2 instance, it goes into the project folder and runs the redeploy script:
```
cd /riley_tori_mariama_ahmed
sudo bash ./redeploy.sh
```
* This means the deployment setup depends on the EC2 instance already having the project folder and the `redeploy.sh` script ready to run.
# CD Execution
* The CD workflow does not run automatically when code is pushed. It is triggered manually from the GitHub Actions tab:
```
on:
  workflow_dispatch:
```
* To run the CD workflow, go to the GitHub repository, click the Actions tab, choose the `Redeploy on AWS` workflow, and click `Run workflow`.
* When the workflow starts, GitHub creates a temporary Ubuntu environment:
```
runs-on: ubuntu-latest
```
* Then the workflow uses SSH to connect to the AWS EC2 instance:
```
uses: appleboy/ssh-action@v1
```
* The workflow connects to the EC2 instance using the GitHub Secrets:
```
host: ${{ secrets.EC2_HOST }}
username: ec2-user
key: ${{ secrets.LABSUSERPEM }}
```
* After connecting to the EC2 instance, the workflow goes into the project folder:
```
cd /riley_tori_mariama_ahmed
```
* Then it runs the redeploy script:
```
sudo bash ./redeploy.sh
```
* The `redeploy.sh` script handles redeploying the bot on the EC2 instance.
* If the workflow runs successfully, the bot is redeployed on AWS EC2.
* If the SSH connection fails, the script fails, or the EC2 instance is not set up correctly, the CD workflow fails. The error can be viewed in the GitHub repository under the Actions tab.







