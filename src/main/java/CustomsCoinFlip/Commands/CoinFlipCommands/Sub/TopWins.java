package CustomsCoinFlip.Commands.CoinFlipCommands.Sub;

import CustomsCoinFlip.Objects.SubCommand;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class TopWins extends SubCommand {

    public TopWins() {
        super("TopWins", "Show the top wins", "", "customscoinflip.topwins", false);
    }

    @Override
    public void run(CommandSender cs, String[] args) {
        if(!(cs instanceof Player)){
            instance.getFileUtil().commandCoinFlipTopWinsConsoleSender.forEach(cs::sendMessage);
            return;
        }
        Player player = (Player) cs;
        new BukkitRunnable(){
            public void run(){
                ArrayList<String> users = instance.getFileUtil().database.getTop10Wins();
                instance.getFileUtil().commandCoinFlipTopWinsHeader.forEach(string -> player.sendMessage(PlaceholderAPI.setPlaceholders(player, string)));
                for(int i = 0; i<10; i++){
                    if(users.size() <= i){
                        player.sendMessage(instance.getFileUtil().commandCoinFlipTopWinsEmptyLine.replace("%rank%",""+(i+1)));
                    }else {
                        String user = users.get(i);
                        String[] split = user.split(" ");
                        String name = split[0];
                        int amount = Integer.parseInt(split[1]);
                        String amountFormatted = instance.getUtil().formatInt(amount);
                        player.sendMessage(instance.getFileUtil().commandCoinFlipTopWinsLine.replace("%winsFormatted%", amountFormatted).replace("%wins%", "" + amount).replace("%name%", name).replace("%rank%", "" + (i + 1)));
                    }
                }
                instance.getFileUtil().commandCoinFlipTopWinsFooter.forEach(string -> player.sendMessage(PlaceholderAPI.setPlaceholders(player, string)));
            }
        }.runTaskAsynchronously(instance);
    }
}
