package CustomsCoinFlip.Commands.CoinFlipCommands.Sub;

import CustomsCoinFlip.CustomsCoinFlipPlugin;
import CustomsCoinFlip.Objects.SubCommand;
import CustomsCoinFlip.Objects.TopHologram;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class DeleteTopHologram extends SubCommand {

    private final CustomsCoinFlipPlugin instance = CustomsCoinFlipPlugin.getInstance();

    public DeleteTopHologram() {
        super("DeleteTopHologram", "Delete a top hologram", "<hologramid>", "customscoinflip.admin.deletetophologram", true);
    }

    @Override
    public void run(CommandSender cs, String[] args) {
        if(!cs.hasPermission(getPermission())){
            instance.getFileUtil().commandCoinFlipDeleteTopHologramNoPermission.forEach(cs::sendMessage);
            return;
        }
        if(args.length == 0){
            instance.getFileUtil().commandCoinFlipDeleteTopHologramInsufficientArguments.forEach(cs::sendMessage);
            return;
        }
        int id;
        try{
            id = Integer.parseInt(args[0]);
        }catch(NumberFormatException e){
            instance.getFileUtil().commandCoinFlipDeleteTopHologramInvalidHologramId.forEach(cs::sendMessage);
            return;
        }
        Optional<TopHologram> topHologramOptional = instance.getTopHolograms().stream().filter(topHologram -> topHologram.getId() == id).findFirst();
        if(!topHologramOptional.isPresent()){
            instance.getFileUtil().commandCoinFlipDeleteTopHologramInvalidHologramId.forEach(cs::sendMessage);
            return;
        }
        topHologramOptional.get().destroy();
        instance.getHologramFileUtil().deleteHologram(topHologramOptional.get());
        instance.getTopHolograms().remove(topHologramOptional.get());
        instance.getFileUtil().commandCoinFlipDeleteTopHologramDeleted.stream().map(string -> string.replace("%id%", "" + id)).forEach(cs::sendMessage);
    }

}
