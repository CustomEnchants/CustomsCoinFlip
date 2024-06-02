package CustomsCoinFlip.Commands.CoinFlipCommands.Sub;

import CustomsCoinFlip.Objects.CoinFlipMatch;
import CustomsCoinFlip.Objects.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class Cancel extends SubCommand {

    public Cancel() {
        super("Cancel", "Cancel your coinflip", "", "customscoinflip.cancel", false);
    }

    @Override
    public void run(CommandSender cs, String[] args) {
        if(!(cs instanceof Player)){
            instance.getFileUtil().commandCoinFlipCancelConsoleSender.forEach(cs::sendMessage);
            return;
        }
        Player player = (Player) cs;
        Optional<CoinFlipMatch> coinFlipMatchOptional = instance.getCoinFlipMatches().stream().filter(coinFlipMatch -> coinFlipMatch.getStarter().toString().equalsIgnoreCase(player.getUniqueId().toString())).findFirst();
        if(!coinFlipMatchOptional.isPresent()){
            instance.getFileUtil().commandCoinFlipCancelNoMatch.forEach(player::sendMessage);
            return;
        }
        CoinFlipMatch coinFlipMatch = coinFlipMatchOptional.get();
        if(coinFlipMatch.isRunning()){
            instance.getFileUtil().commandCoinFlipCancelAlreadyRunning.forEach(player::sendMessage);
            return;
        }
        instance.getCoinFlipMenuFileUtil().coinFlipInventory.remove(coinFlipMatch.getStarterItemStack());
        instance.getCoinFlipMatches().remove(coinFlipMatch);
        instance.getFileUtil().commandCoinFlipCancelCancelled.forEach(player::sendMessage);
    }
}
