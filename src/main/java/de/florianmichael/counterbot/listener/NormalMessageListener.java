package de.florianmichael.counterbot.listener;

import de.florianmichael.counterbot.CounterBot;
import de.florianmichael.counterbot.DiscordServer;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class NormalMessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);

        final DiscordServer server = CounterBot.get().config().byGuildId(event.getGuild().getId());

        if (server == null) return;
        if (!server.isThis(event.getGuild(), event.getChannel())) return;
        if (event.getAuthor().isBot()) return;

        final int nextMessage = this.isInt(event.getMessage().getContentDisplay());
        if (nextMessage != -1 && nextMessage == server.getRecord() + 1)
            server.updateRecord();
        else
            event.getMessage().delete().complete();
    }

    private Integer isInt(final String input) {
        try {
            return Integer.parseInt(input);
        } catch (Exception e) {
            return -1;
        }
    }
}
