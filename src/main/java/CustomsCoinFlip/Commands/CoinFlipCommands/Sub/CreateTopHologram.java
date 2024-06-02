package CustomsCoinFlip.Commands.CoinFlipCommands.Sub;

import CustomsCoinFlip.CustomsCoinFlipPlugin;
import CustomsCoinFlip.Enums.HologramType;
import CustomsCoinFlip.Objects.SubCommand;
import CustomsCoinFlip.Objects.TopHologram;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;

public class CreateTopHologram extends SubCommand {

    private final CustomsCoinFlipPlugin instance = CustomsCoinFlipPlugin.getInstance();

    public CreateTopHologram() {
        super("CreateTopHologram", "Create a top hologram", "<hologramtype>", "customscoinflip.admin.createtophologram", true);
    }

    @Override
    public void run(CommandSender cs, String[] args) {
        if(!(cs instanceof Player)){
            instance.getFileUtil().commandCoinFlipCreateTopHologramConsoleSender.forEach(cs::sendMessage);
            return;
        }
        if(!cs.hasPermission(getPermission())){
            instance.getFileUtil().commandCoinFlipCreateTopHologramNoPermission.forEach(cs::sendMessage);
            return;
        }
        if(args.length == 0){
            instance.getFileUtil().commandCoinFlipCreateTopHologramInsufficientArguments.forEach(cs::sendMessage);
            return;
        }
        Optional<HologramType> hologramTypeOptional = Arrays.stream(HologramType.values()).filter(hologramType -> hologramType.name().equalsIgnoreCase(args[0])).findFirst();
        if(!hologramTypeOptional.isPresent()){
            instance.getFileUtil().commandCoinFlipCreateTopHologramInvalidHologramType.forEach(cs::sendMessage);
            return;
        }
        Player player = (Player) cs;
        TopHologram topHologram = new TopHologram(hologramTypeOptional.get(),player.getLocation());
        instance.getHologramFileUtil().saveTopHologram(topHologram);
        topHologram.spawnHologram();
        topHologram.update();
        instance.getFileUtil().commandCoinFlipCreateTopHologramCreated.stream().map(string -> string.replace("%hologramtype%", hologramTypeOptional.get().name())).forEach(player::sendMessage);
        instance.getTopHolograms().add(topHologram);
    }
}
