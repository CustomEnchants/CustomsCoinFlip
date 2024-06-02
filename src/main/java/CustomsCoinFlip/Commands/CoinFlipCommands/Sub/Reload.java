package CustomsCoinFlip.Commands.CoinFlipCommands.Sub;

import CustomsCoinFlip.Objects.SubCommand;
import org.bukkit.command.CommandSender;

public class Reload extends SubCommand {

    public Reload() {
        super("Reload", "Reload the config", "", "customscoinflip.admin.reload", true);
    }

    @Override
    public void run(CommandSender cs, String[] args) {
        if(!cs.hasPermission(getPermission())){
            instance.getFileUtil().commandCoinFlipReloadNoPermission.forEach(cs::sendMessage);
            return;
        }
        instance.getFileUtil().loadValues(true);
        instance.getFileUtil().commandCoinFlipReloadReloaded.forEach(cs::sendMessage);
    }
}
