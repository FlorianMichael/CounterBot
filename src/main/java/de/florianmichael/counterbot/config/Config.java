package de.florianmichael.counterbot.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.florianmichael.counterbot.DiscordServer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class Config {
    public final ConfigElement<String> jdaToken = new ConfigElement<>("JDA-Token", "");
    public final ConfigElement<String> rpcText = new ConfigElement<>("RPC Text", "Your Discord!");

    public final ConfigElement<String> noPermissions = new ConfigElement<>("No Permissions", "You don't have enough permissions to set the Channel!");

    public final List<DiscordServer> servers = new ArrayList<>();

    private final File configFile = new File("configuration.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public int getRecord(final String guildId) {
        return this.byGuildId(guildId).getRecord();
    }

    public void saveServer(final String guildId, final String channelId) {
        if (!this.hasServer(guildId))
            this.servers.add(new DiscordServer(guildId, channelId, 0));
    }

    public DiscordServer byGuildId(final String guildId) {
        return this.servers.stream().filter(s -> s.getGuildId().equals(guildId)).findFirst().orElse(null);
    }

    public boolean hasServer(final String guildId) {
        return this.byGuildId(guildId) != null;
    }

    public void load() {
        if (!configFile.exists()) {
            try {
                this.save();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
            return;
        }

        try {
            final JsonObject rootNode = gson.fromJson(new FileReader(this.configFile), JsonObject.class).getAsJsonObject();

            for (ConfigElement<?> element : this.getElements())
                element.load(rootNode);

            final JsonObject recordNode = rootNode.get("records").getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : recordNode.entrySet()) {
                final String guildId = entry.getKey();
                final JsonObject obj = entry.getValue().getAsJsonObject();

                this.servers.add(new DiscordServer(guildId, obj.get("channel").getAsString(), obj.get("record").getAsInt()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void save() throws Exception {
        this.configFile.delete();
        this.configFile.createNewFile();

        final JsonObject rootNode = new JsonObject();

        for (ConfigElement element : this.getElements())
            element.save(rootNode);

        final JsonObject mainRecordNode = new JsonObject();

        for (DiscordServer server : this.servers) {
            final JsonObject recordNode = new JsonObject();

            recordNode.addProperty("channel", server.getChannelId());
            recordNode.addProperty("record", server.getRecord());

            mainRecordNode.add(server.getGuildId(), recordNode);
        }

        rootNode.add("records", mainRecordNode);

        try (final FileWriter fileWriter = new FileWriter(this.configFile)) {
            fileWriter.write(gson.toJson(rootNode));
            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ConfigElement> getElements() throws Exception {
        final List<ConfigElement> list = new ArrayList<>();

        for (Field declaredField : Config.class.getDeclaredFields())
            if (declaredField.get(this) instanceof ConfigElement configElement)
                list.add(configElement);

        return list;
    }
}
