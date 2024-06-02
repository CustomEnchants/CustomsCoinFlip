package CustomsCoinFlip.Utils;

import CustomsCoinFlip.CustomsCoinFlipPlugin;
import CustomsCoinFlip.MySQL.Database;
import CustomsCoinFlip.MySQL.SQLite;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileUtil {

    private File config;
    public Database database;

    private final CustomsCoinFlipPlugin instance = CustomsCoinFlipPlugin.getInstance();

    public ArrayList<String> coinFlipMenuCanNotCoinFlipAgainstSelf = new ArrayList<>();
    public ArrayList<String> coinFlipMenuInsufficientFunds = new ArrayList<>();

    public ArrayList<String> coinFlipMatchWon = new ArrayList<>();

    public ArrayList<String> commandCoinFlipCancelConsoleSender = new ArrayList<>();
    public ArrayList<String> commandCoinFlipCancelNoMatch = new ArrayList<>();
    public ArrayList<String> commandCoinFlipCancelAlreadyRunning = new ArrayList<>();
    public ArrayList<String> commandCoinFlipCancelCancelled = new ArrayList<>();

    public ArrayList<String> commandCoinFlipCreateTopHologramConsoleSender = new ArrayList<>();
    public ArrayList<String> commandCoinFlipCreateTopHologramNoPermission = new ArrayList<>();
    public ArrayList<String> commandCoinFlipCreateTopHologramInsufficientArguments = new ArrayList<>();
    public ArrayList<String> commandCoinFlipCreateTopHologramInvalidHologramType = new ArrayList<>();
    public ArrayList<String> commandCoinFlipCreateTopHologramCreated = new ArrayList<>();

    public ArrayList<String> commandCoinFlipDeleteTopHologramNoPermission = new ArrayList<>();
    public ArrayList<String> commandCoinFlipDeleteTopHologramInsufficientArguments = new ArrayList<>();
    public ArrayList<String> commandCoinFlipDeleteTopHologramInvalidHologramId = new ArrayList<>();
    public ArrayList<String> commandCoinFlipDeleteTopHologramDeleted = new ArrayList<>();

    public ArrayList<String> commandCoinFlipReloadNoPermission = new ArrayList<>();
    public ArrayList<String> commandCoinFlipReloadReloaded = new ArrayList<>();

    public ArrayList<String> commandCoinFlipStartConsoleSender = new ArrayList<>();
    public ArrayList<String> commandCoinFlipStartInsufficientArguments = new ArrayList<>();
    public ArrayList<String> commandCoinFlipStartInvalidAmount = new ArrayList<>();
    public ArrayList<String> commandCoinFlipStartInvalidSide = new ArrayList<>();
    public ArrayList<String> commandCoinFlipStartInsufficientBalance = new ArrayList<>();
    public ArrayList<String> commandCoinFlipStartAlreadyHasAMatch = new ArrayList<>();
    public ArrayList<String> commandCoinFlipStartCreated = new ArrayList<>();

    public ArrayList<String> commandCoinFlipStatsInvalidPlayer = new ArrayList<>();
    public ArrayList<String> commandCoinFlipStatsInsufficientArguments = new ArrayList<>();
    public ArrayList<String> commandCoinFlipStatsYourStatistics = new ArrayList<>();
    public ArrayList<String> commandCoinFlipStatsOthersStatistics = new ArrayList<>();

    public ArrayList<String> commandCoinFlipToggleConsoleSender = new ArrayList<>();
    public ArrayList<String> commandCoinFlipToggleEnabled = new ArrayList<>();
    public ArrayList<String> commandCoinFlipToggleDisabled = new ArrayList<>();

    public ArrayList<String> commandCoinFlipTopWinsConsoleSender = new ArrayList<>();
    public ArrayList<String> commandCoinFlipTopWinsHeader = new ArrayList<>();
    public String commandCoinFlipTopWinsLine;
    public String commandCoinFlipTopWinsEmptyLine;
    public ArrayList<String> commandCoinFlipTopWinsFooter = new ArrayList<>();

    public ArrayList<String> commandCoinFlipTopLossesConsoleSender = new ArrayList<>();
    public ArrayList<String> commandCoinFlipTopLossesHeader = new ArrayList<>();
    public String commandCoinFlipTopLossesLine;
    public String commandCoinFlipTopLossesEmptyLine;
    public ArrayList<String> commandCoinFlipTopLossesFooter = new ArrayList<>();



    public ArrayList<String> hologramLinesTopWinsHeader = new ArrayList<>();
    public String hologramLinesTopWinsLine;
    public String hologramLinesTopWinsEmptyLine;
    public ArrayList<String> hologramLinesTopWinsFooter = new ArrayList<>();

    public ArrayList<String> hologramLinesTopLossesHeader = new ArrayList<>();
    public String hologramLinesTopLossesLine;
    public String hologramLinesTopLossesEmptyLine;
    public ArrayList<String> hologramLinesTopLossesFooter = new ArrayList<>();




    public void loadValues(boolean reload) {
        if(reload){
            commandCoinFlipCancelConsoleSender.clear();
            commandCoinFlipCancelNoMatch.clear();
            commandCoinFlipCancelAlreadyRunning.clear();
            commandCoinFlipCancelCancelled.clear();
            commandCoinFlipCreateTopHologramConsoleSender.clear();
            commandCoinFlipCreateTopHologramNoPermission.clear();
            commandCoinFlipCreateTopHologramInsufficientArguments.clear();
            commandCoinFlipCreateTopHologramInvalidHologramType.clear();
            commandCoinFlipCreateTopHologramCreated.clear();
            commandCoinFlipDeleteTopHologramNoPermission.clear();
            commandCoinFlipDeleteTopHologramInsufficientArguments.clear();
            commandCoinFlipDeleteTopHologramInvalidHologramId.clear();
            commandCoinFlipDeleteTopHologramDeleted.clear();
            commandCoinFlipReloadNoPermission.clear();
            commandCoinFlipReloadReloaded.clear();
            commandCoinFlipStatsInvalidPlayer.clear();
            commandCoinFlipStatsInsufficientArguments.clear();
            commandCoinFlipStatsYourStatistics.clear();
            commandCoinFlipStatsOthersStatistics.clear();
            commandCoinFlipToggleConsoleSender.clear();
            commandCoinFlipToggleEnabled.clear();
            commandCoinFlipToggleDisabled.clear();
            commandCoinFlipTopWinsConsoleSender.clear();
            commandCoinFlipTopWinsHeader.clear();
            commandCoinFlipTopWinsFooter.clear();
            commandCoinFlipTopLossesConsoleSender.clear();
            commandCoinFlipTopLossesHeader.clear();
            commandCoinFlipTopLossesFooter.clear();
            hologramLinesTopWinsHeader.clear();
            hologramLinesTopWinsFooter.clear();
            hologramLinesTopLossesHeader.clear();
            hologramLinesTopLossesFooter.clear();

            commandCoinFlipStartConsoleSender.clear();
            commandCoinFlipStartInsufficientArguments.clear();
            commandCoinFlipStartInvalidAmount.clear();
            commandCoinFlipStartInvalidSide.clear();
            commandCoinFlipStartInsufficientBalance.clear();
            commandCoinFlipStartAlreadyHasAMatch.clear();
            commandCoinFlipStartCreated.clear();

            coinFlipMenuCanNotCoinFlipAgainstSelf.clear();
            coinFlipMenuInsufficientFunds.clear();
            coinFlipMatchWon.clear();
        }

        try {
            database = new SQLite(new File(instance.getDataFolder() + File.separator + "data.db"));
            database.openConnection();
            database.setupPlayersTable();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        FileConfiguration conf = YamlConfiguration.loadConfiguration(config);

        ConfigurationSection coinFlipMenu = conf.getConfigurationSection("CoinFlipMenu");
        coinFlipMenuInsufficientFunds = instance.getUtil().fixColours(coinFlipMenu.getStringList("insufficientBalance"));
        coinFlipMenuCanNotCoinFlipAgainstSelf = instance.getUtil().fixColours(coinFlipMenu.getStringList("canNotCoinFlipAgainstSelf"));

        ConfigurationSection coinFlipMatch = conf.getConfigurationSection("CoinFlipMatch");
        coinFlipMatchWon = instance.getUtil().fixColours(coinFlipMatch.getStringList("won"));

        ConfigurationSection hologramLines = conf.getConfigurationSection("holograms");
        hologramLinesTopWinsHeader = instance.getUtil().fixColours(hologramLines.getStringList("coinFlipTopWins.header"));
        hologramLinesTopWinsLine = instance.getUtil().fixColour(hologramLines.getString("coinFlipTopWins.line"));
        hologramLinesTopWinsEmptyLine = instance.getUtil().fixColour(hologramLines.getString("coinFlipTopWins.emptyLine"));
        hologramLinesTopWinsFooter = instance.getUtil().fixColours(hologramLines.getStringList("coinFlipTopWins.footer"));

        hologramLinesTopLossesHeader = instance.getUtil().fixColours(hologramLines.getStringList("coinFlipTopLosses.header"));
        hologramLinesTopLossesLine = instance.getUtil().fixColour(hologramLines.getString("coinFlipTopLosses.line"));
        hologramLinesTopLossesEmptyLine = instance.getUtil().fixColour(hologramLines.getString("coinFlipTopLosses.emptyLine"));
        hologramLinesTopLossesFooter = instance.getUtil().fixColours(hologramLines.getStringList("coinFlipTopLosses.footer"));

        ConfigurationSection commands = conf.getConfigurationSection("Commands");

        ConfigurationSection coinFlipCommand = commands.getConfigurationSection("CoinFlip");

        ConfigurationSection coinFlipCancelCommand = coinFlipCommand.getConfigurationSection("Cancel");
        commandCoinFlipCancelConsoleSender = instance.getUtil().fixColours(coinFlipCancelCommand.getStringList("consoleSender"));
        commandCoinFlipCancelNoMatch = instance.getUtil().fixColours(coinFlipCancelCommand.getStringList("noMatch"));
        commandCoinFlipCancelAlreadyRunning = instance.getUtil().fixColours(coinFlipCancelCommand.getStringList("alreadyRunning"));
        commandCoinFlipCancelCancelled = instance.getUtil().fixColours(coinFlipCancelCommand.getStringList("cancelled"));

        ConfigurationSection coinFlipCreateHologramCommand = coinFlipCommand.getConfigurationSection("CreateHologram");
        commandCoinFlipCreateTopHologramConsoleSender = instance.getUtil().fixColours(coinFlipCreateHologramCommand.getStringList("consoleSender"));
        commandCoinFlipCreateTopHologramNoPermission = instance.getUtil().fixColours(coinFlipCreateHologramCommand.getStringList("noPermission"));
        commandCoinFlipCreateTopHologramInsufficientArguments = instance.getUtil().fixColours(coinFlipCreateHologramCommand.getStringList("insufficientArguments"));
        commandCoinFlipCreateTopHologramInvalidHologramType = instance.getUtil().fixColours(coinFlipCreateHologramCommand.getStringList("invalidHologramType"));
        commandCoinFlipCreateTopHologramCreated = instance.getUtil().fixColours(coinFlipCreateHologramCommand.getStringList("created"));

        ConfigurationSection coinFlipDeleteHologramCommand = coinFlipCommand.getConfigurationSection("DeleteHologram");
        commandCoinFlipDeleteTopHologramDeleted = instance.getUtil().fixColours(coinFlipDeleteHologramCommand.getStringList("deleted"));
        commandCoinFlipDeleteTopHologramInvalidHologramId = instance.getUtil().fixColours(coinFlipDeleteHologramCommand.getStringList("invalidHologramId"));
        commandCoinFlipDeleteTopHologramInsufficientArguments = instance.getUtil().fixColours(coinFlipDeleteHologramCommand.getStringList("insufficientArguments"));
        commandCoinFlipDeleteTopHologramNoPermission = instance.getUtil().fixColours(coinFlipDeleteHologramCommand.getStringList("noPermission"));


        ConfigurationSection coinFlipReloadCommand = coinFlipCommand.getConfigurationSection("Reload");
        commandCoinFlipReloadNoPermission = instance.getUtil().fixColours(coinFlipReloadCommand.getStringList("noPermission"));
        commandCoinFlipReloadReloaded = instance.getUtil().fixColours(coinFlipReloadCommand.getStringList("reloaded"));

        ConfigurationSection coinFlipStartCommand = coinFlipCommand.getConfigurationSection("Start");
        commandCoinFlipStartInsufficientArguments = instance.getUtil().fixColours(coinFlipStartCommand.getStringList("insufficientArguments"));
        commandCoinFlipStartInsufficientBalance = instance.getUtil().fixColours(coinFlipStartCommand.getStringList("insufficientBalance"));
        commandCoinFlipStartConsoleSender = instance.getUtil().fixColours(coinFlipStartCommand.getStringList("consoleSender"));
        commandCoinFlipStartCreated = instance.getUtil().fixColours(coinFlipStartCommand.getStringList("created"));
        commandCoinFlipStartInvalidAmount = instance.getUtil().fixColours(coinFlipStartCommand.getStringList("invalidAmount"));
        commandCoinFlipStartInvalidSide = instance.getUtil().fixColours(coinFlipStartCommand.getStringList("invalidSide"));
        commandCoinFlipStartAlreadyHasAMatch = instance.getUtil().fixColours(coinFlipStartCommand.getStringList("alreadyHasAMatch"));

        ConfigurationSection coinFlipStatsCommand = coinFlipCommand.getConfigurationSection("Stats");
        commandCoinFlipStatsInvalidPlayer = instance.getUtil().fixColours(coinFlipStatsCommand.getStringList("invalidPlayer"));
        commandCoinFlipStatsInsufficientArguments = instance.getUtil().fixColours(coinFlipStatsCommand.getStringList("insufficientArguments"));
        commandCoinFlipStatsYourStatistics = instance.getUtil().fixColours(coinFlipStatsCommand.getStringList("yourStatistics"));
        commandCoinFlipStatsOthersStatistics = instance.getUtil().fixColours(coinFlipStatsCommand.getStringList("othersStatistics"));

        ConfigurationSection coinFlipToggleCommand = coinFlipCommand.getConfigurationSection("Toggle");
        commandCoinFlipToggleConsoleSender = instance.getUtil().fixColours(coinFlipToggleCommand.getStringList("consoleSender"));
        commandCoinFlipToggleEnabled = instance.getUtil().fixColours(coinFlipToggleCommand.getStringList("enabled"));
        commandCoinFlipToggleDisabled = instance.getUtil().fixColours(coinFlipToggleCommand.getStringList("disabled"));

        ConfigurationSection coinFlipTopWinsCommand = coinFlipCommand.getConfigurationSection("TopWins");
        commandCoinFlipTopWinsConsoleSender = instance.getUtil().fixColours(coinFlipTopWinsCommand.getStringList("consoleSender"));
        commandCoinFlipTopWinsHeader = instance.getUtil().fixColours(coinFlipTopWinsCommand.getStringList("header"));
        commandCoinFlipTopWinsLine = instance.getUtil().fixColour(coinFlipTopWinsCommand.getString("line"));
        commandCoinFlipTopWinsEmptyLine = instance.getUtil().fixColour(coinFlipTopWinsCommand.getString("emptyLine"));
        commandCoinFlipTopWinsFooter = instance.getUtil().fixColours(coinFlipTopWinsCommand.getStringList("footer"));

        ConfigurationSection coinFlipTopLossesCommand = coinFlipCommand.getConfigurationSection("TopLosses");
        commandCoinFlipTopLossesConsoleSender = instance.getUtil().fixColours(coinFlipTopLossesCommand.getStringList("consoleSender"));
        commandCoinFlipTopLossesHeader = instance.getUtil().fixColours(coinFlipTopLossesCommand.getStringList("header"));
        commandCoinFlipTopLossesLine = instance.getUtil().fixColour(coinFlipTopLossesCommand.getString("line"));
        commandCoinFlipTopLossesEmptyLine = instance.getUtil().fixColour(coinFlipTopLossesCommand.getString("emptyLine"));
        commandCoinFlipTopLossesFooter = instance.getUtil().fixColours(coinFlipTopLossesCommand.getStringList("footer"));
    }


    public void setup(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        config = new File(dir + File.separator + "Config.yml");
        if (!config.exists()) {
            try {
                FileConfiguration conf = YamlConfiguration.loadConfiguration(config);
                conf.set("Author", "CustomEnchants");

                ConfigurationSection coinFlipMenu = conf.createSection("CoinFlipMenu");
                coinFlipMenu.set("canNotCoinFlipAgainstSelf",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&cYou can not CoinFlip against your self."));
                coinFlipMenu.set("insufficientBalance",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&cYou do have insufficient funds to do this CoinFlip."));

                ConfigurationSection coinFlipMatch = conf.createSection("CoinFlipMatch");
                coinFlipMatch.set("won",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&c%winner% &7has won the CoinFlip against &c%loser% &7with &c%type% &7for &2$%wagerFormatted%"));

                ConfigurationSection hologramLines = conf.createSection("holograms");

                hologramLines.set("coinFlipTopLosses.header", Collections.singletonList("&d&lCOINFLIP-TOP &5&lLOSSES"));
                hologramLines.set("coinFlipTopLosses.line","&7%rank%. &b%player% &a%losses%");
                hologramLines.set("coinFlipTopLosses.emptyLine","&7%rank%.");
                hologramLines.set("coinFlipTopLosses.footer",Collections.singletonList("&8&l(&7Command: &f/CoinFlip TopLosses&8&l)"));

                hologramLines.set("coinFlipTopWins.header", Collections.singletonList("&f&lCOINFLIP-TOP &b&lWINS"));
                hologramLines.set("coinFlipTopWins.line","&7%rank%. &b%player% &a%wins%");
                hologramLines.set("coinFlipTopWins.emptyLine","&7%rank%.");
                hologramLines.set("coinFlipTopWins.footer",Collections.singletonList("&8&l(&7Command: &f/CoinFlip TopWins&8&l)"));


                ConfigurationSection commands = conf.createSection("Commands");

                ConfigurationSection coinFlipCommand = commands.createSection("CoinFlip");

                ConfigurationSection coinFlipCancelCommand = coinFlipCommand.createSection("Cancel");
                coinFlipCancelCommand.set("consoleSender",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&cYou cannot execute this command as console"));
                coinFlipCancelCommand.set("noMatch",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&cYou do not have a CoinFlip match"));
                coinFlipCancelCommand.set("alreadyRunning",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&cYou cannot cancel a active CoinFlip"));
                coinFlipCancelCommand.set("cancelled",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&7You have cancelled your CoinFlip"));


                ConfigurationSection coinFlipCreateHologramCommand = coinFlipCommand.createSection("CreateHologram");
                coinFlipCreateHologramCommand.set("consoleSender",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&cYou cannot execute this command as console"));
                coinFlipCreateHologramCommand.set("noPermission",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&cYou do not have permission to execute this command!"));
                coinFlipCreateHologramCommand.set("insufficientArguments",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&cInsufficient arguments","&7Command Usage: &c/CreateTopHologram <hologramtype>"));
                coinFlipCreateHologramCommand.set("invalidHologramType",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&cYou have specified a invalid hologramType"));
                coinFlipCreateHologramCommand.set("created",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&7You have created a &c%hologramtype% &7Top Hologram"));

                ConfigurationSection coinFlipDeleteHologramCommand = coinFlipCommand.createSection("DeleteHologram");
                coinFlipDeleteHologramCommand.set("noPermission",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&cYou do not have permission to execute this command!"));
                coinFlipDeleteHologramCommand.set("insufficientArguments",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&cInsufficient arguments","&7Command Usage: &c/DeleteTopHologram <hologramid>"));
                coinFlipDeleteHologramCommand.set("invalidHologramId",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&cYou have specified a invalid hologram id"));
                coinFlipDeleteHologramCommand.set("deleted",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&7You have deleted a top hologram by the id of &c%id%"));


                ConfigurationSection coinFlipStartCommand = coinFlipCommand.createSection("Start");
                coinFlipStartCommand.set("consoleSender",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&cYou cannot execute this command as console"));
                coinFlipStartCommand.set("insufficientArguments",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&7Command Usage: &c/CoinFlip Start <amount> <heads/tails>"));
                coinFlipStartCommand.set("insufficientBalance",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&cYou have insufficient funds to do this CoinFlip."));
                coinFlipStartCommand.set("invalidAmount",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&cYou specified a invalid amount"));
                coinFlipStartCommand.set("invalidSide",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&cYou have specified a invalid side"));
                coinFlipStartCommand.set("alreadyHasAMatch",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&cYou already have a CoinFlip"));
                coinFlipStartCommand.set("created",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&7You have created a CoinFlip with &c%type% &7for &2$%wagerFormatted%"));

                ConfigurationSection coinFlipStatsCommand = coinFlipCommand.createSection("Stats");
                coinFlipStatsCommand.set("invalidPlayer",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&c%name% &7has never played on this server"));
                coinFlipStatsCommand.set("insufficientArguments",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&7Command Usage: &c/CoinFlip Stats <name>"));
                coinFlipStatsCommand.set("yourStatistics",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&7Your Statistics","&7Wins: &c%wins%","&7Losses: &c%losses%"));
                coinFlipStatsCommand.set("othersStatistics",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&c%name%'s &7Statistics","&7Wins: &c%wins%","&7Losses: &c%losses%"));

                ConfigurationSection coinFlipReloadCommand = coinFlipCommand.createSection("Reload");
                coinFlipReloadCommand.set("noPermission",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&cYou do not have permission to execute this command!"));
                coinFlipReloadCommand.set("reloaded",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&7You have reloaded the config"));

                ConfigurationSection coinFlipToggleCommand = coinFlipCommand.createSection("Toggle");
                coinFlipToggleCommand.set("consoleSender",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&cYou cannot execute this command as console"));
                coinFlipToggleCommand.set("enabled",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&7You have &2&lENABLED&7 CoinFlip notifications!"));
                coinFlipToggleCommand.set("disabled",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&7You have &c&lDISABLED&7 CoinFlip notifications!"));

                ConfigurationSection coinFlipTopWinsCommand = coinFlipCommand.createSection("TopWins");
                coinFlipTopWinsCommand.set("consoleSender",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&cYou cannot execute this command as console"));
                coinFlipTopWinsCommand.set("header",Collections.singletonList("&b&lCoinFlip TopWins Top 10"));
                coinFlipTopWinsCommand.set("line","&b&l#%rank% &b%name% &8- &e%winsFormatted%");
                coinFlipTopWinsCommand.set("emptyLine","&b&l#%rank%");
                coinFlipTopWinsCommand.set("footer",Collections.singletonList("&7(Your wins: &6%CustomsCoinFlip_winsFormatted%&7)"));

                ConfigurationSection coinFlipTopLossesCommand = coinFlipCommand.createSection("TopLosses");
                coinFlipTopLossesCommand.set("consoleSender",Arrays.asList("&6_________________.[ &4&lThe&6&lLegend&e&lCraft &6]._________________","&cYou cannot execute this command as console"));
                coinFlipTopLossesCommand.set("header",Collections.singletonList("&b&lCoinFlip TopLosses Top 10"));
                coinFlipTopLossesCommand.set("line","&b&l#%rank% &b%name% &8- &e%lossesFormatted%");
                coinFlipTopLossesCommand.set("emptyLine","&b&l#%rank%");
                coinFlipTopLossesCommand.set("footer",Collections.singletonList("&7(Your losses: &6%CustomsCoinFlip_lossesFormatted%&7)"));


                conf.save(config);
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
        File databaseFile = new File(dir + File.separator + "data.db");
        if(!databaseFile.exists()){
            try {
                databaseFile.createNewFile();
            }catch(IOException e){
                System.out.println(e.getLocalizedMessage());
            }
        }
        loadValues(false);
    }

}
