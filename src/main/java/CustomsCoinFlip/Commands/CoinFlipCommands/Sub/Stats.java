package CustomsCoinFlip.Commands.CoinFlipCommands.Sub;

import CustomsCoinFlip.CustomsCoinFlipPlugin;
import CustomsCoinFlip.Objects.SubCommand;
import CustomsCoinFlip.Objects.UserDAO;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Stats extends SubCommand {

    private final CustomsCoinFlipPlugin instance = CustomsCoinFlipPlugin.getInstance();

    public Stats() {
        super("Stats", "Show statistics", "<player=you>", "customscoinflip.stats", false);
    }

    private void showStats(CommandSender cs,String targetPlayer){
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(targetPlayer);
        UserDAO user = instance.getUsers().containsKey(offlinePlayer.getUniqueId()) ? instance.getUsers().get(offlinePlayer.getUniqueId()) : new UserDAO(offlinePlayer.getUniqueId());
        if(!user.isRegistered()){
            instance.getFileUtil().commandCoinFlipStatsInvalidPlayer.stream().map(string -> string.replace("%name%", targetPlayer)).forEach(cs::sendMessage);
            return;
        }
        if(!user.isOnline()){
            user.load();
        }
        instance.getFileUtil().commandCoinFlipStatsOthersStatistics.stream().map(string -> instance.getUtil().parseUser(user, string)).forEach(cs::sendMessage);
    }

    @Override
    public void run(CommandSender cs, String[] args) {
        new BukkitRunnable() {
            public void run() {
                if (args.length == 0) {
                    if(!(cs instanceof Player)){
                        instance.getFileUtil().commandCoinFlipStatsInsufficientArguments.forEach(cs::sendMessage);
                        cancel();
                        return;
                    }
                    Player player = (Player) cs;
                    UserDAO user = instance.getUsers().get(player.getUniqueId());
                    instance.getFileUtil().commandCoinFlipStatsYourStatistics.stream().map(string -> instance.getUtil().parseUser(user, string)).forEach(player::sendMessage);
                }else{
                    showStats(cs,args[0]);
                }
                cancel();
            }
        }.runTaskAsynchronously(instance);
    }




}
