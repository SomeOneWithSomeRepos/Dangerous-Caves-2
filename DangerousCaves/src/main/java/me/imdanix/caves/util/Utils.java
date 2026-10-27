package me.imdanix.caves.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public final class Utils {
    private final static Pattern FLOAT = Pattern.compile("-?\\d+(\\.\\d+)?");

    public static String clr(String s){
        return s == null ? null : ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> clr(List<String> list) {
        list.replaceAll(Utils::clr);
        return list;
    }

    /**
     * Fill collection with world names from the config list.
     * Entries may contain glob patterns (eg. {@code world-*}), which are matched
     * against all currently loaded worlds. Plain names are kept as is.
     * If the list is empty, all loaded OVERWORLD worlds are used.
     * @param worldsStr List of world names and/or glob patterns from config
     * @param into Collection to fill with resolved world names
     */
    public static void fillWorlds(List<String> worldsStr, Collection<String> into) {
        into.clear();
        if (worldsStr.isEmpty()) {
            for (World world : Bukkit.getWorlds()) {
                if (world.getEnvironment() == World.Environment.NORMAL)
                    into.add(world.getName());
            }
        } else {
            List<Pattern> patterns = new ArrayList<>(worldsStr.size());
            for (String str : worldsStr) {
                if (str.indexOf('*') >= 0 || str.indexOf('?') >= 0) {
                    patterns.add(globToPattern(str));
                } else {
                    into.add(str);
                }
            }
            if (!patterns.isEmpty()) {
                for (World world : Bukkit.getWorlds()) {
                    String name = world.getName();
                    if (into.contains(name)) continue;
                    for (Pattern pattern : patterns) {
                        if (pattern.matcher(name).matches()) {
                            into.add(name);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Check if a world name matches a config entry.
     * Entries may contain glob patterns (eg. {@code world-*}).
     * @param name World name to check
     * @param entries Config entries (plain names and/or glob patterns)
     * @return Does the world name match any entry
     */
    public static boolean matchesWorld(String name, Collection<String> entries) {
        for (String entry : entries) {
            if (entry.indexOf('*') >= 0 || entry.indexOf('?') >= 0) {
                if (globToPattern(entry).matcher(name).matches()) return true;
            } else if (entry.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Convert a glob pattern ({@code *} and {@code ?} wildcards) to a regex pattern
     * @param glob Glob pattern
     * @return Compiled regex pattern matching the same strings
     */
    public static Pattern globToPattern(String glob) {
        StringBuilder regex = new StringBuilder(glob.length() + 8);
        for (int i = 0; i < glob.length(); i++) {
            char c = glob.charAt(i);
            switch (c) {
                case '*' -> regex.append(".*");
                case '?' -> regex.append('.');
                default -> {
                    if (!Character.isLetterOrDigit(c)) regex.append('\\');
                    regex.append(c);
                }
            }
        }
        return Pattern.compile(regex.toString());
    }

    public static void setMaxHealth(LivingEntity entity, double health) {
        entity.getAttribute(Attribute.MAX_HEALTH).setBaseValue(health);
        entity.setHealth(health);
    }

    public static String capitalize(String str) {
        StringBuilder builder = new StringBuilder(str.length() + 1);
        String[] split = str.split(" ");
        for (String word : split) {
            builder.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(' ');
        }
        return builder.substring(0, builder.length() - 1);
    }

    public static <T extends Enum<T>> Set<T> getEnumSet(Class<T> clazz, Collection<String> enumStrColl) {
        Set<T> enums = new HashSet<>();
        for (String enumStr : enumStrColl) {
            T t = getEnum(clazz, enumStr.toUpperCase(Locale.ROOT));
            if (t != null) enums.add(t);
        }
        return enums.isEmpty() ? Collections.emptySet() : EnumSet.copyOf(enums);
    }

    public static <T extends Enum<T>> T getEnum(Class<T> clazz, String name) {
        return getEnum(clazz, name, null);
    }

    public static <T extends Enum<T>> T getEnum(Class<T> clazz, String name, T def) {
        try {
            return Enum.valueOf(clazz, name);
        } catch (IllegalArgumentException e) {
            return def;
        }
    }

    public static <T> void runIteratingTask(Plugin plugin, Collection<T> col, Consumer<T> action, long timeout) {
        Iterator<T> iterator = col.iterator();
        plugin.getServer().getScheduler().runTaskTimer(plugin, (task) -> {
            if (iterator.hasNext()) {
                action.accept(iterator.next());
            } else {
                task.cancel();
            }
        }, timeout, timeout);
    }

    public static double getDouble(String str, double def) {
        if (!FLOAT.matcher(str).matches()) return def;
        return Double.parseDouble(str);
    }
}
