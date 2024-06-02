package CustomsCoinFlip.Commands.CoinFlipCommands.Sub;

import CustomsCoinFlip.Enums.Side;
import CustomsCoinFlip.Objects.CoinFlipMatch;
import CustomsCoinFlip.Objects.SubCommand;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public class Start extends SubCommand {

    public Start() {
        super("Start", "Start a CoinFlip match", "<amount> <heads/tails>", "customscoinflip.start", false);
    }

    @Override
    public void run(CommandSender cs, String[] args) {
        if(!(cs instanceof Player)){
            instance.getFileUtil().commandCoinFlipStartConsoleSender.forEach(cs::sendMessage);
            return;
        }
        if(instance.getCoinFlipMenuFileUtil().coinFlipInventory.firstEmpty() == -1){
            cs.sendMessage("no space for a new coinflip");
            return;
        }

        if(args.length < 2){
            instance.getFileUtil().commandCoinFlipStartInsufficientArguments.forEach(cs::sendMessage);
            return;
        }
        double amount;
        try{
            amount = Double.parseDouble(args[0]);
        }catch(NumberFormatException e){
            instance.getFileUtil().commandCoinFlipStartInvalidAmount.forEach(cs::sendMessage);
            return;
        }
        Optional<Side> sideOptional = Arrays.stream(Side.values()).filter(side -> side.name().equalsIgnoreCase(args[1])).findFirst();
        if(!sideOptional.isPresent()){
            instance.getFileUtil().commandCoinFlipStartInvalidSide.forEach(cs::sendMessage);
            return;
        }
        Player player = (Player) cs;
        EconomyResponse economyResponse = instance.economy.withdrawPlayer(player,amount);
        if(!economyResponse.transactionSuccess()){
            instance.getFileUtil().commandCoinFlipStartInsufficientBalance.forEach(cs::sendMessage);
            return;
        }
        if(instance.getCoinFlipMatches().stream().anyMatch(coinFlipMatch -> coinFlipMatch.getStarter().toString().equalsIgnoreCase(player.getUniqueId().toString()))){
            instance.getFileUtil().commandCoinFlipStartAlreadyHasAMatch.forEach(player::sendMessage);
            return;
        }
        ItemStack itemStack = instance.getUtil().createHead(player,amount,sideOptional.get());
        CoinFlipMatch coinFlipMatch = new CoinFlipMatch(player.getUniqueId(),amount,sideOptional.get(),itemStack);
        instance.getCoinFlipMatches().add(coinFlipMatch);
        instance.getCoinFlipMenuFileUtil().coinFlipInventory.setItem(instance.getCoinFlipMenuFileUtil().coinFlipInventory.firstEmpty(),itemStack);
        instance.getFileUtil().commandCoinFlipStartCreated.stream().map(string -> string.replace("%type%", sideOptional.get().name()).replace("%wagerFormatted%", NumberFormat.getInstance(Locale.US).format(amount)).replace("%wager%", String.valueOf(amount))).forEach(player::sendMessage);
    }


}
