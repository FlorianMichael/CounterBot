package de.florianmichael.counterbot;

import de.florianmichael.counterbot.config.Config;
import de.florianmichael.counterbot.listener.NormalMessageListener;
import de.florianmichael.counterbot.listener.SlashCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class CounterBot {
    private final static CounterBot instance = new CounterBot();

    private final Config config = new Config();

    public void load() {
        this.config().load();
        final JDABuilder jda = JDABuilder.createDefault(this.config().jdaToken.get());

        jda.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        jda.setBulkDeleteSplittingEnabled(false);
        jda.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        jda.setCompression(Compression.NONE);
        jda.addEventListeners(new SlashCommandListener(), new NormalMessageListener());
        jda.setActivity(Activity.watching(this.config().rpcText.get()));

        final JDA finalJDA = jda.build();

        finalJDA.upsertCommand("channel", "Set the Channel of the Game").queue();
    }

    public static CounterBot get() {
        return CounterBot.instance;
    }

    public Config config() {
        return this.config;
    }
}
