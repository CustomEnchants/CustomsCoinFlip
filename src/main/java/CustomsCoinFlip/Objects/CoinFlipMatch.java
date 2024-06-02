package CustomsCoinFlip.Objects;

import CustomsCoinFlip.CustomsCoinFlipPlugin;
import CustomsCoinFlip.Enums.Side;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.stream.Collectors;

public class CoinFlipMatch {

    private final CustomsCoinFlipPlugin instance = CustomsCoinFlipPlugin.getInstance();

    private UUID starter;
    private UUID opponent;
    private double wager;
    private boolean isRunning;
    private Side side;
    private ItemStack starterItemStack;
    private ItemStack opponentItemStack;

    private Side finalSide;

    public Inventory inventory;

    public CoinFlipMatch(UUID starter, double wager, Side side, ItemStack starterItemStack) {
        setStarter(starter);
        setWager(wager);
        setSide(side);
        setStarterItemStack(starterItemStack);
    }

    public UUID getStarter() {
        return starter;
    }

    public void setStarter(UUID starter) {
        this.starter = starter;
    }

    public double getWager() {
        return wager;
    }

    public void setWager(double wager) {
        this.wager = wager;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning() {
        isRunning = true;
    }

    public UUID getOpponent() {
        return opponent;
    }

    public void setOpponent(UUID opponent) {
        this.opponent = opponent;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public Side getSide() {
        return side;
    }

    public ItemStack getStarterItemStack() {
        return starterItemStack;
    }

    public void setStarterItemStack(ItemStack itemStack) {
        this.starterItemStack = itemStack;
    }

    public ItemStack getOpponentItemStack() {
        return opponentItemStack;
    }

    public void setOpponentItemStack(ItemStack itemStack) {
        this.opponentItemStack = itemStack;
    }

    public void setFinalSide(Side side) {
        this.finalSide = side;
    }

    public Side getFinalSide() {
        return finalSide;
    }

    public void giveWager(boolean opponentWon) {
        OfflinePlayer offlinePlayer = opponentWon ? Bukkit.getOfflinePlayer(getOpponent()) : Bukkit.getOfflinePlayer(getStarter());
        instance.economy.depositPlayer(offlinePlayer, (getWager() * 2));
    }

    public void addStatistics(boolean opponentWon) {
        UserDAO user1 = instance.getUsers().containsKey(getStarter()) ? instance.getUsers().get(getStarter()) : new UserDAO(getStarter());
        UserDAO user2 = instance.getUsers().containsKey(getOpponent()) ? instance.getUsers().get(getOpponent()) : new UserDAO(getOpponent());
        if (!user1.isOnline()) {
            user1.load();
        }
        if (!user2.isOnline()) {
            user2.load();
        }
        if (opponentWon) {
            user1.addLoss();
            user2.addWin();
        } else {
            user1.addWin();
            user2.addLoss();
        }
        if (!user1.isOnline()) {
            user1.update();
        }
        if (!user2.isOnline()) {
            user2.update();
        }
    }

    public void openGUI() {
        Player opponentPlayer = Bukkit.getPlayer(getOpponent());
        inventory = Bukkit.createInventory(opponentPlayer, instance.getCoinFlipMenuFileUtil().coinFlippingInventory.getSize(), instance.getCoinFlipMenuFileUtil().coinFlippingInventory.getTitle());
        inventory.setContents(instance.getCoinFlipMenuFileUtil().coinFlippingInventory.getContents());
        opponentPlayer.openInventory(inventory);
        Player starterPlayer = Bukkit.getPlayer(getStarter());
        if (starterPlayer != null) {
            starterPlayer.openInventory(inventory);
        }
    }

    public void closeInventories(){
        Player opponentPlayer = Bukkit.getPlayer(getOpponent());
        if(opponentPlayer != null){
            if(opponentPlayer.getOpenInventory().getTitle().equalsIgnoreCase(inventory.getTitle())) {
                opponentPlayer.closeInventory();
            }
        }
        Player starterPlayer = Bukkit.getPlayer(getStarter());
        if (starterPlayer != null) {
            if(starterPlayer.getOpenInventory().getTitle().equalsIgnoreCase(inventory.getTitle())) {
                starterPlayer.closeInventory();
            }
        }
    }

    public void start() {
        ArrayList<Side> sides = new ArrayList<>();
        sides.add(Side.HEADS);
        sides.add(Side.TAILS);
        DoubleAdder doubleAdder = new DoubleAdder();
        new BukkitRunnable() {
            public void run() {
                if (doubleAdder.intValue() == 10) {
                    boolean opponentWon = (getFinalSide() == getSide());
                    giveWager(opponentWon);
                    addStatistics(opponentWon);
                    announceWinner(opponentWon);
                    closeInventories();
                    clear();
                    cancel();
                    return;
                }
                doubleAdder.add(1.0);
                Side next = sides.get(new Random().nextInt(sides.size()));
                setFinalSide(next);
                Collections.shuffle(sides);
                ItemStack itemStack = getFinalSide() == getSide() ? getStarterItemStack() : getOpponentItemStack();
                inventory.setItem(instance.getCoinFlipMenuFileUtil().skullItemSlot, itemStack);
            }
        }.runTaskTimerAsynchronously(instance, 0, 10);
    }

    public void clear() {
        instance.getCoinFlipMatches().remove(this);
    }

    public void announceWinner(boolean opponentWon) {
        OfflinePlayer winner = opponentWon ? Bukkit.getOfflinePlayer(getOpponent()) : Bukkit.getOfflinePlayer(getStarter());
        OfflinePlayer loser = opponentWon ? Bukkit.getOfflinePlayer(getStarter()) : Bukkit.getOfflinePlayer(getOpponent());
        ArrayList<String> messages = instance.getFileUtil().coinFlipMatchWon.stream().map(string -> string.replace("%type%", getSide().name()).replace("%wagerFormatted%", NumberFormat.getInstance(Locale.US).format(getWager())).replace("%wager%", "" + getWager()).replace("%winner%", winner.getName()).replace("%loser%", loser.getName())).collect(Collectors.toCollection(ArrayList::new));
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            UserDAO userDAO = instance.getUsers().get(player.getUniqueId());
            if (!userDAO.hasNotificationsEnabled()) {
                return;
            }
            messages.forEach(player::sendMessage);
        });
        messages.clear();
    }


}
