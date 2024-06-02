package CustomsCoinFlip.Utils;

import CustomsCoinFlip.CustomsCoinFlipPlugin;
import CustomsCoinFlip.Enums.HologramType;
import CustomsCoinFlip.Enums.Side;
import CustomsCoinFlip.Objects.UserDAO;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.NumberFormat;
import java.util.*;

import static CustomsCoinFlip.Enums.Side.HEADS;

public class Util {

    private final CustomsCoinFlipPlugin instance = CustomsCoinFlipPlugin.getInstance();

    public String fixColour(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public ArrayList<String> fixColours(List<String> input) {
        ArrayList<String> result = new ArrayList<>();
        input.forEach(string -> result.add(fixColour(string)));
        return result;
    }

    public String parseUser(UserDAO user, String string){
        string = string
                .replace("%wins%",""+user.getWins())
                .replace("%losses%",""+user.getLosses())
                .replace("%name%",user.getName());
        return string;
    }

    public ItemStack buildItem(Material material, int amount, short data, String displayName, List<String> lore){
        ItemStack itemStack = new ItemStack(material, amount, data);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(fixColour(displayName));
        itemMeta.setLore(fixColours(lore));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public String formatInt(int amount){
        return NumberFormat.getInstance(Locale.US).format(amount);
    }

    public HologramType getHologramType(String input){
        return Arrays.stream(HologramType.values()).filter(hologramType -> hologramType.name().equalsIgnoreCase(input)).findFirst().orElse(null);
    }

    public Side getSide(String input){
        return Arrays.stream(Side.values()).filter(side -> side.name().equalsIgnoreCase(input)).findFirst().orElse(null);
    }

    public ItemStack createHead(Player player, double wager, Side side){
        ItemStack result = side == HEADS ? instance.getCoinFlipMenuFileUtil().skullHeadsItem.clone() : instance.getCoinFlipMenuFileUtil().skullTailsItem.clone();
        SkullMeta skullMeta = (SkullMeta) result.getItemMeta();
        skullMeta.setOwner(player.getName());
        skullMeta.setDisplayName(skullMeta.getDisplayName().replace("%name%",player.getName()));
        ArrayList<String> newLore = new ArrayList<>();
        if(skullMeta.hasLore()) {
            for (String string : skullMeta.getLore()) {
                newLore.add(string.replace("%side%",side.name()).replace("%wagerFormatted%",NumberFormat.getInstance(Locale.US).format(wager)).replace("%wager%", String.valueOf(wager)));
            }
        }
        skullMeta.setLore(newLore);
        result.setItemMeta(skullMeta);
        return result;
    }


}
