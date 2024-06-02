package CustomsCoinFlip.Hooks;

import CustomsCoinFlip.CustomsCoinFlipPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Locale;

public class PlaceHolderAPIHook extends PlaceholderExpansion {

    private static final long K = 1000;
    private static final long M = K * K;
    private static final long G = M * K;
    private static final long T = G * K;
    private final CustomsCoinFlipPlugin instance = CustomsCoinFlipPlugin.getInstance();

    // This is the most important method when registering an expansion from your plugin
    public boolean persist() {
// This tells PlaceholderAPI to not unregister this hook when /papi reload is executed
        return true;
    }

    // The identifier, shouldn't contain any _ or %
    public String getIdentifier() {
        return instance.getDescription().getName().toLowerCase();
    }

    // The author of the placeholder. This cannot be null
    public String getAuthor() {
        return "CustomEnchants";
    }

    // Same with #getAuthor() but for versioon
    public String getVersion() {
        return instance.getDescription().getVersion();
    }


    // Use this method to setup placeholders. This is somewhat similar to EZPlaceholderHook
    public String onPlaceholderRequest(Player player, String identifier) {
        if (!instance.getUsers().containsKey(player.getUniqueId())) {
            return "";
        }
        switch (identifier) {
            case "wins":{
                return String.valueOf(instance.getUsers().get(player.getUniqueId()).getWins());
            }
            case "winsFormatted":{
                return NumberFormat.getInstance(Locale.US).format(instance.getUsers().get(player.getUniqueId()).getWins());
            }
            case "losses":{
                return String.valueOf(instance.getUsers().get(player.getUniqueId()).getLosses());
            }
            case "lossesFormatted":{
                return NumberFormat.getInstance(Locale.US).format(instance.getUsers().get(player.getUniqueId()).getLosses());
            }
            default: {
                return "";
            }
        }
    }

}