package CustomsCoinFlip.Listeners;

import CustomsCoinFlip.CustomsCoinFlipPlugin;
import CustomsCoinFlip.Objects.UserDAO;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {

    private final CustomsCoinFlipPlugin instance = CustomsCoinFlipPlugin.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new BukkitRunnable(){
            public void run(){
                UserDAO user = new UserDAO(event.getPlayer().getUniqueId());
                user.setName(event.getPlayer().getName());
                user.setOnline(true);
                if (!user.isRegistered()) {
                    user.register();
                }
                user.load();
                instance.getUsers().put(event.getPlayer().getUniqueId(), user);
                cancel();
            }
        }.runTaskAsynchronously(instance);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        new BukkitRunnable(){
            public void run(){
                if (!instance.getUsers().containsKey(event.getPlayer().getUniqueId())){
                    cancel();
                    return;
                }
                instance.getUsers().get(event.getPlayer().getUniqueId()).update();
                instance.getUsers().remove(event.getPlayer().getUniqueId());
                cancel();
            }
        }.runTaskAsynchronously(instance);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        new BukkitRunnable(){
            public void run(){
                if (!instance.getUsers().containsKey(event.getPlayer().getUniqueId())){
                    cancel();
                    return;
                }
                instance.getUsers().get(event.getPlayer().getUniqueId()).update();
                instance.getUsers().remove(event.getPlayer().getUniqueId());
                cancel();
            }
        }.runTaskAsynchronously(instance);
    }

}
