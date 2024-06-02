package CustomsCoinFlip.Tasks;

import CustomsCoinFlip.CustomsCoinFlipPlugin;
import org.bukkit.Bukkit;

public class AutoSavePlayerData implements Runnable{

    private final CustomsCoinFlipPlugin instance = CustomsCoinFlipPlugin.getInstance();

    public void run(){
        if(Bukkit.getServer().getOnlinePlayers().isEmpty()){
            return;
        }
        instance.getUsers().forEach((key, value) -> value.update());
    }
}
