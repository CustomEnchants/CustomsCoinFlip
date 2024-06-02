package CustomsCoinFlip.Commands.CoinFlipCommands.Sub;

import CustomsCoinFlip.Objects.SubCommand;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class TopLosses extends SubCommand {

    public TopLosses() {
        super("TopLosses", "Show the top losses", "", "customscoinflip.toplosses", false);
    }

    @Override
    public void run(CommandSender cs, String[] args) {
        if(!(cs instanceof Player)){
            instance.getFileUtil().commandCoinFlipTopLossesConsoleSender.forEach(cs::sendMessage);
            return;
        }
        Player player = (Player) cs;
        new BukkitRunnable(){
            public void run(){
                ArrayList<String> users = instance.getFileUtil().database.getTop10Losses();
                instance.getFileUtil().commandCoinFlipTopLossesHeader.forEach(string -> player.sendMessage(PlaceholderAPI.setPlaceholders(player, string)));
                for(int i = 0; i<10; i++){
                    if(users.size() <= i){
                        player.sendMessage(instance.getFileUtil().commandCoinFlipTopLossesEmptyLine.replace("%rank%",""+(i+1)));
                    }else {
                        String user = users.get(i);
                        String[] split = user.split(" ");
                        String name = split[0];
                        int amount = Integer.parseInt(split[1]);
                        String amountFormatted = instance.getUtil().formatInt(amount);
                        player.sendMessage(instance.getFileUtil().commandCoinFlipTopLossesLine.replace("%lossesFormatted%", amountFormatted).replace("%losses%", "" + amount).replace("%name%", name).replace("%rank%", "" + (i + 1)));
                    }
                }
                instance.getFileUtil().commandCoinFlipTopLossesFooter.forEach(string -> player.sendMessage(PlaceholderAPI.setPlaceholders(player, string)));
            }
        }.runTaskAsynchronously(instance);
    }
}
