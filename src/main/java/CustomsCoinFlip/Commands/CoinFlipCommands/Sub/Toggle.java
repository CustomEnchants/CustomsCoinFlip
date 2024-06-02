package CustomsCoinFlip.Commands.CoinFlipCommands.Sub;

import CustomsCoinFlip.Objects.SubCommand;
import CustomsCoinFlip.Objects.UserDAO;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Toggle extends SubCommand {

    public Toggle() {
        super("Toggle", "Toggle notifications", "", "customscoinflip.toggle", false);
    }

    @Override
    public void run(CommandSender cs, String[] args) {
        if(!(cs instanceof Player)){
            instance.getFileUtil().commandCoinFlipToggleConsoleSender.forEach(cs::sendMessage);
            return;
        }
        Player player = (Player) cs;
        UserDAO userDAO = instance.getUsers().get(player.getUniqueId());
        if(!userDAO.hasNotificationsEnabled()){
            instance.getFileUtil().commandCoinFlipToggleEnabled.forEach(player::sendMessage);
        }else{
            instance.getFileUtil().commandCoinFlipToggleDisabled.forEach(player::sendMessage);
        }
        userDAO.setNotificationsEnabled(!userDAO.hasNotificationsEnabled());
    }
}
