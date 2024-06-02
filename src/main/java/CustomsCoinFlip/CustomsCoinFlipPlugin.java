package CustomsCoinFlip;

import CustomsCoinFlip.Commands.CoinFlipCommands.*;
import CustomsCoinFlip.Hooks.PlaceHolderAPIHook;
import CustomsCoinFlip.Listeners.*;
import CustomsCoinFlip.Objects.*;
import CustomsCoinFlip.Tasks.AutoSavePlayerData;
import CustomsCoinFlip.Tasks.UpdateHolograms;
import CustomsCoinFlip.Utils.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CustomsCoinFlipPlugin extends JavaPlugin {

    private static CustomsCoinFlipPlugin instance;
    private final HashMap<UUID, UserDAO> users = new HashMap<>();
    private final ArrayList<TopHologram> topHolograms = new ArrayList<>();
    private final ArrayList<CoinFlipMatch> coinFlipMatches = new ArrayList<>();

    public Economy economy = null;

    private FileUtil fileUtil;
    private Util util;
    private HologramFileUtil hologramFileUtil;
    private CoinFlipMenuFileUtil coinFlipMenuFileUtil;

    public boolean hookedHolographicDisplays;
    public boolean hookedDecentHolograms;

    public static CustomsCoinFlipPlugin getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        if(!Bukkit.getServer().getPluginManager().isPluginEnabled("Vault")){
            System.out.println("Plugin depends on Vault");
            setEnabled(false);
            return;
        }
        if (!setupEconomy()) {
            System.out.println("Plugin depends on Vault and a economy plugin");
            setEnabled(false);
            return;
        }
        if(!Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")){
            System.out.println("Plugin disabling due to placeholderapi not being installed and it is required");
            setEnabled(false);
            return;
        }
        hookedHolographicDisplays = Bukkit.getServer().getPluginManager().isPluginEnabled("HolographicDisplays");
        hookedDecentHolograms = Bukkit.getServer().getPluginManager().isPluginEnabled("DecentHolograms");

        if(!hookedDecentHolograms && !hookedHolographicDisplays){
            System.out.println("Plugin disabling due to no valid hologram plugin being installed and it is required");
            setEnabled(false);
            return;
        }
        util = new Util();
        fileUtil = new FileUtil();
        coinFlipMenuFileUtil = new CoinFlipMenuFileUtil();
        hologramFileUtil = new HologramFileUtil();
        getFileUtil().setup(getDataFolder());
        getCoinFlipMenuFileUtil().setup(getDataFolder());
        getHologramFileUtil().setup(getDataFolder());
        Bukkit.getPluginManager().registerEvents(new CoinFlipMenuListener(),this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        getCommand("CoinFlip").setExecutor(new CoinFlipMainCommand());
        getCommand("CoinFlip").setTabCompleter(new CoinFlipMainCommand());
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceHolderAPIHook().register();
        }
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this,new AutoSavePlayerData(), 20, 300*20);
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this,new UpdateHolograms(), 20, 60*20);

    }

    public void onDisable() {
        getCoinFlipMatches().clear();
        getTopHolograms().clear();
        instance = null;
    }

    public ArrayList<TopHologram> getTopHolograms() {
        return topHolograms;
    }

    public ArrayList<CoinFlipMatch> getCoinFlipMatches() {
        return coinFlipMatches;
    }

    public HashMap<UUID, UserDAO> getUsers() {
        return users;
    }


    public Util getUtil() {
        return util;
    }

    public FileUtil getFileUtil() {
        return fileUtil;
    }

    public CoinFlipMenuFileUtil getCoinFlipMenuFileUtil(){
        return coinFlipMenuFileUtil;
    }

    public HologramFileUtil getHologramFileUtil(){
        return hologramFileUtil;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }


}
