package CustomsCoinFlip.Objects;

import CustomsCoinFlip.CustomsCoinFlipPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UserDAO {

    private final CustomsCoinFlipPlugin instance = CustomsCoinFlipPlugin.getInstance();
    private UUID uuid;
    private int wins;
    private int losses;
    private String name;
    private boolean isOnline;
    private boolean notificationsEnabled;

    public UserDAO(UUID uuid) {
        setUUID(uuid);
    }

    public void load() {
        name = instance.getFileUtil().database.getUsername(getUUID());
        wins = instance.getFileUtil().database.getWins(getUUID());
        losses = instance.getFileUtil().database.getLosses(getUUID());
        notificationsEnabled = instance.getFileUtil().database.getNotificationsEnabled(getUUID());
    }

    public void addWin(){
        wins += 1;
    }

    public void addLoss(){
        losses += 1;
    }

    public int getWins(){
        return wins;
    }

    public int getLosses(){
        return losses;
    }

    public void register() {
        instance.getFileUtil().database.registerUser(this);
    }

    public void update() {
        instance.getFileUtil().database.updateUser(this);
    }

    public boolean isRegistered() {
        return instance.getFileUtil().database.isUserRegistered(getUUID());
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(getUUID());
    }

    public OfflinePlayer getOfflinePlayer(){
        return Bukkit.getOfflinePlayer(getUUID());
    }

    public void setOnline(boolean state){
        this.isOnline = state;
    }

    public boolean isOnline(){
        return isOnline;
    }

    public boolean hasNotificationsEnabled(){
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean state){
        this.notificationsEnabled = state;
    }

}
