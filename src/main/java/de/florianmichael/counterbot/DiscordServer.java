package de.florianmichael.counterbot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;

public class DiscordServer {
    private final String guildId;
    private final String channelId;
    private int record;

    public DiscordServer(String guildId, String channelId, final int record) {
        this.guildId = guildId;
        this.channelId = channelId;
        this.record = record;
    }

    public boolean isThis(final Guild guild, final Channel channel) {
        return guild.getId().equals(this.guildId) && channel.getId().equals(this.channelId);
    }

    public String getGuildId() {
        return guildId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void updateRecord() {
        this.record++;
    }

    public int getRecord() {
        return record;
    }
}
