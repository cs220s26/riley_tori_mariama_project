package edu.moravian;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
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

    private static String loadToken()
    {
        try
        {
            Dotenv dotenv = Dotenv.load();
            return dotenv.get("DISCORD_TOKEN");
        }
        catch(DotenvException e)
        {
            System.err.println("Failed to load .env file\n\nIs it present?");
            System.exit(1);
            return null;  // needed for the compiler
        }
    }
}