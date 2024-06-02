package CustomsCoinFlip.Listeners;

import CustomsCoinFlip.CustomsCoinFlipPlugin;
import CustomsCoinFlip.Objects.CoinFlipMatch;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import static CustomsCoinFlip.Enums.Side.HEADS;
import static CustomsCoinFlip.Enums.Side.TAILS;

public class CoinFlipMenuListener implements Listener {

    private final CustomsCoinFlipPlugin instance = CustomsCoinFlipPlugin.getInstance();

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event){
        if(event.getView().getTitle().equalsIgnoreCase(instance.getCoinFlipMenuFileUtil().coinFlippingInventory.getTitle())){
            event.setCancelled(true);
            return;
        }
        if (!event.getView().getTitle().equalsIgnoreCase(instance.getCoinFlipMenuFileUtil().coinFlipInventory.getTitle())) {
            return;
        }
        event.setCancelled(true);

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(event.getClick() == null) return;
        if(event.getClickedInventory() == null) return;
        if(event.getCurrentItem() == null) return;
        if(event.getView().getTitle().equalsIgnoreCase(instance.getCoinFlipMenuFileUtil().coinFlippingInventory.getTitle())){
            event.setCancelled(true);
            return;
        }
        if (!event.getView().getTitle().equalsIgnoreCase(instance.getCoinFlipMenuFileUtil().coinFlipInventory.getTitle())) {
            return;
        }
        event.setCancelled(true);
        if(instance.getCoinFlipMatches().stream().noneMatch(coinFlipMatch -> coinFlipMatch.getStarterItemStack().isSimilar(event.getCurrentItem()))){
            return;
        }
        CoinFlipMatch coinFlipMatch = instance.getCoinFlipMatches().stream().filter(coinFlipMatch1 -> coinFlipMatch1.getStarterItemStack().isSimilar(event.getCurrentItem())).findFirst().orElse(null);
        Player player = (Player) event.getWhoClicked();
        if(coinFlipMatch.getStarter().toString().equalsIgnoreCase(player.getUniqueId().toString())){
            instance.getFileUtil().coinFlipMenuCanNotCoinFlipAgainstSelf.forEach(player::sendMessage);
            return;
        }
        EconomyResponse economyResponse = instance.economy.withdrawPlayer(player,coinFlipMatch.getWager());
        if(!economyResponse.transactionSuccess()){
            instance.getFileUtil().coinFlipMenuInsufficientFunds.forEach(player::sendMessage);
            return;
        }
        instance.getCoinFlipMenuFileUtil().coinFlipInventory.remove(event.getCurrentItem());
        coinFlipMatch.setOpponent(player.getUniqueId());
        coinFlipMatch.setOpponentItemStack(instance.getUtil().createHead(player,coinFlipMatch.getWager(),coinFlipMatch.getSide() == HEADS ? TAILS : HEADS));
        coinFlipMatch.setRunning();
        coinFlipMatch.start();
        player.closeInventory();
        coinFlipMatch.openGUI();
    }

}
