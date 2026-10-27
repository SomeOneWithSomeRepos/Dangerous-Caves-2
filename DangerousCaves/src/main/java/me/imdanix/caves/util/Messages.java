package me.imdanix.caves.util;

import me.imdanix.caves.configuration.Configurable;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class Messages implements Configurable {
    private String prefix;

    @Override
    public void reload(ConfigurationSection cfg) {
        prefix = Utils.clr(cfg.getString("prefix", "&8[&4DangerousCaves&8] &r"));
    }

    public String prefix() {
        return prefix;
    }

    public void send(CommandSender sender, String message) {
        sender.sendMessage(prefix + Utils.clr(message));
    }

    public void sendRaw(CommandSender sender, String message) {
        sender.sendMessage(message);
    }

    @Override
    public String getConfigPath() {
        return "";
    }
}
