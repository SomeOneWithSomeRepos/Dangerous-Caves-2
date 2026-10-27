package me.imdanix.caves.caverns;

import me.imdanix.caves.configuration.Configurable;
import me.imdanix.caves.ticks.TickLevel;
import me.imdanix.caves.ticks.Tickable;
import me.imdanix.caves.util.Locations;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class DangerScaling implements Tickable, Configurable {
    private static final double TICK_SECONDS = TickLevel.PLAYER.getTicks() / 20d;

    private final Map<UUID, Double> players = new WeakHashMap<>();

    private boolean disabled;
    private double minMultiplier;
    private double maxMultiplier;
    private double scaleTime;
    private double resetTime;

    @Override
    public void reload(ConfigurationSection cfg) {
        disabled = !cfg.getBoolean("enabled", true);
        minMultiplier = cfg.getDouble("min-multiplier", 0.5);
        maxMultiplier = cfg.getDouble("max-multiplier", 1.0);
        scaleTime = Math.max(cfg.getDouble("scale-time", 300), 1);
        resetTime = Math.max(cfg.getDouble("reset-time", 60), 1);
    }

    @Override
    public void tick() {
        if (disabled) return;
        for (World world : Bukkit.getWorlds()) {
            for (Player player : world.getPlayers()) {
                Location loc = player.getLocation();
                double scale = players.getOrDefault(player.getUniqueId(), minMultiplier);
                if (Locations.isCave(loc)) {
                    scale = Math.min(maxMultiplier, scale + TICK_SECONDS / scaleTime);
                } else {
                    scale = Math.max(minMultiplier, scale - TICK_SECONDS / resetTime);
                }
                players.put(player.getUniqueId(), scale);
            }
        }
    }

    public double getMultiplier(Player player) {
        if (disabled) return maxMultiplier;
        return players.getOrDefault(player.getUniqueId(), minMultiplier);
    }

    @Override
    public TickLevel getTickLevel() {
        return TickLevel.PLAYER;
    }

    @Override
    public String getConfigPath() {
        return "caverns.scaling";
    }
}
