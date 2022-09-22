package de.florianmichael.counterbot.listener;

import de.florianmichael.counterbot.CounterBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        final String command = event.getName();
        if (!command.equals("channel")) return;

        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            CounterBot.get().config().saveServer(event.getGuild().getId(), event.getChannel().getId());
            try {
                CounterBot.get().config().save();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            event.reply("Success!").queue();
        } else
            event.reply(CounterBot.get().config().noPermissions.get()).queue();
    }
}
