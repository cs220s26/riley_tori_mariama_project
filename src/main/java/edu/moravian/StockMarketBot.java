package edu.moravian;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class StockMarketBot
{
    public static void main(String[] args)
    {
        String token = loadToken();
        RedisStorage storage;
        try {
            storage = new RedisStorage("localhost", 6379);
        }
        catch (JedisConnectionException e) {
            System.out.println("Could not connect to Redis");
            return;
        }
        StockMarketGame game = new StockMarketGame(storage);
        BotResponder bot = new BotResponder(game);

        JDA api = JDABuilder.createDefault(token).enableIntents(GatewayIntent.MESSAGE_CONTENT).build();
        
        api.addEventListener(new ListenerAdapter()
        {
            @Override
            public void onMessageReceived(MessageReceivedEvent event)
            {
                if (event.getAuthor().isBot())
                    return;
                if (!event.getChannel().getName().equals("trigo-bot"))
                    return;
                String playerName = event.getAuthor().getName();
                String message = event.getMessage().getContentRaw();

                event.getChannel().sendMessage(bot.respond(message, playerName)).queue();
            }
        });
    }

    public static void loadToken() {

        String secretName = "220_Discord_Token";
        Region region = Region.of("us-east-1");

        // Create a Secrets Manager client
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse getSecretValueResponse;

        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
        } catch (Exception e) {
            throw new InternalSystemException("Failed to load discord token");
        }

        return secret = getSecretValueResponse.secretString();
    }
}