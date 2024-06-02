package CustomsCoinFlip.Utils;

import CustomsCoinFlip.CustomsCoinFlipPlugin;
import CustomsCoinFlip.Enums.Side;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CoinFlipMenuFileUtil {

    private final CustomsCoinFlipPlugin instance = CustomsCoinFlipPlugin.getInstance();

    private File coinFlipConfig;
    public Inventory coinFlipInventory;

    public Inventory coinFlippingInventory;

    public int skullItemSlot;
    public ItemStack skullHeadsItem;
    public ItemStack skullTailsItem;

    public void createCoinFlipInventory(){
        FileConfiguration config = YamlConfiguration.loadConfiguration(coinFlipConfig);
        ConfigurationSection inventory = config.getConfigurationSection("Inventory");
        coinFlipInventory = Bukkit.createInventory(null,inventory.getInt("size"),instance.getUtil().fixColour(inventory.getString("name")));
    }

    public void loadMenu(){
        FileConfiguration config = YamlConfiguration.loadConfiguration(coinFlipConfig);
        ConfigurationSection inventory = config.getConfigurationSection("Inventory");
        createCoinFlipInventory();

        ConfigurationSection fillerBlockSection = inventory.getConfigurationSection("fillerBlock");
        ItemStack fillerBlock = fillerBlockSection.getItemStack("itemStack");

        fillerBlockSection.getIntegerList("slots").forEach(slot -> coinFlipInventory.setItem(slot, fillerBlock));

        ConfigurationSection infoItemSection = inventory.getConfigurationSection("infoItem");
        ItemStack infoItem = infoItemSection.getItemStack("itemStack");
        infoItemSection.getIntegerList("slots").forEach(slot -> coinFlipInventory.setItem(slot, infoItem));
    }

    public void loadCoinFlippingMenu(){
        FileConfiguration config = YamlConfiguration.loadConfiguration(coinFlipConfig);
        ConfigurationSection coinFlippingInventory = config.getConfigurationSection("coinFlippingInventory");
        this.coinFlippingInventory = Bukkit.createInventory(null,coinFlippingInventory.getInt("inventory.size"),instance.getUtil().fixColour(coinFlippingInventory.getString("inventory.name")));
        ConfigurationSection fillerBlockSection = coinFlippingInventory.getConfigurationSection("fillerBlock");
        ItemStack fillerBlock = fillerBlockSection.getItemStack("itemStack");
        fillerBlockSection.getIntegerList("slots").forEach(slot -> this.coinFlippingInventory.setItem(slot, fillerBlock));
        this.coinFlippingInventory.setItem(coinFlippingInventory.getInt("skull.itemSlot"),coinFlippingInventory.getItemStack("skull.heads.itemStack"));
        skullItemSlot = coinFlippingInventory.getInt("skull.itemSlot");
        skullHeadsItem = coinFlippingInventory.getItemStack("skull.heads.itemStack");
        skullTailsItem = coinFlippingInventory.getItemStack("skull.tails.itemStack");
    }


    public void setup(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        coinFlipConfig = new File(dir + File.separator + "CoinFlipMenu.yml");
        if(!coinFlipConfig.exists()){
            try{
                coinFlipConfig.createNewFile();
                FileConfiguration config = YamlConfiguration.loadConfiguration(coinFlipConfig);
                ConfigurationSection inventory = config.createSection("Inventory");
                inventory.set("name","&l» &rCoinFlip");
                inventory.set("size",54);

                ConfigurationSection fillerBlock = inventory.createSection("fillerBlock");
                fillerBlock.set("itemStack",getDefaultFillerBlock());
                fillerBlock.set("slots",Arrays.asList(0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,46,47,48,50,51,52,53));

                ConfigurationSection infoItem = inventory.createSection("infoItem");
                infoItem.set("itemStack",getDefaultInfoItem());
                infoItem.set("slots",Collections.singletonList(49));

                ConfigurationSection coinFlippingInventory = config.createSection("coinFlippingInventory");
                coinFlippingInventory.set("inventory.name","&l» CoinFlipping");
                coinFlippingInventory.set("inventory.size",27);
                coinFlippingInventory.set("fillerBlock.itemStack",getDefaultFillerBlock());
                coinFlippingInventory.set("fillerBlock.slots",Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,14,15,16,17,18,19,20,21,22,23,24,25,26));
                coinFlippingInventory.set("skull.itemSlot",13);
                coinFlippingInventory.set("skull.heads.itemStack",getDefaultSkull(Side.HEADS));
                coinFlippingInventory.set("skull.tails.itemStack",getDefaultSkull(Side.TAILS));

                config.save(coinFlipConfig);
            }catch(IOException e){
                System.out.println(e.getLocalizedMessage());
            }
        }
        loadMenu();
        loadCoinFlippingMenu();
    }

    private ItemStack getDefaultSkull(Side side){
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM,1,(short)3);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setDisplayName("§f§l%name%");
        skullMeta.setLore(Arrays.asList(""
                ,"§b§lWager"
                ," §7$%wager%"
                ,""
                ,"§b§lSide Chosen"
                ," §7%side%".replace("%side%",side.name())
                ,""
                ,"§7Click here to §a§lENTER §7the bet!"));
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }


    private ItemStack getDefaultInfoItem(){
        ItemStack itemStack = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§b§lCoin Flip Help");
        itemMeta.setLore(Arrays.asList(
                "",
                "§f/CoinFlip",
                "§7View all active CoinFlip matches",
                "",
                "§f/CoinFlip Start <Amount> <Heads/Tails>",
                "§7Start a CoinFlip match",
                "",
                "§f/CoinFlip Cancel",
                "§7Remove your CoinFlip match",
                "",
                "§f/CoinFlip Toggle",
                "§7Toggles CoinFlip broadcasts",
                "",
                "§f/CoinFlip TopWins",
                "§7To view the who has the most wins",
                "",
                "§f/CoinFlip TopLosses",
                "§7To view the who has the most losses"
        ));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack getDefaultFillerBlock(){
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE,1,(short)15);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§d");
        itemMeta.setLore(Arrays.asList("",""));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
