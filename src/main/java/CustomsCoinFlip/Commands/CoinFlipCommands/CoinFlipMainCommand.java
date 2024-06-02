package CustomsCoinFlip.Commands.CoinFlipCommands;

import CustomsCoinFlip.Commands.CoinFlipCommands.Sub.*;
import CustomsCoinFlip.CustomsCoinFlipPlugin;
import CustomsCoinFlip.Objects.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;

public class CoinFlipMainCommand implements CommandExecutor, TabCompleter {

    private final ArrayList<SubCommand> subCommands = new ArrayList<>();
    private final CustomsCoinFlipPlugin instance = CustomsCoinFlipPlugin.getInstance();

    public CoinFlipMainCommand(){
        subCommands.add(new Cancel());
        subCommands.add(new CreateTopHologram());
        subCommands.add(new DeleteTopHologram());
        subCommands.add(new Reload());
        subCommands.add(new Start());
        subCommands.add(new Stats());
        subCommands.add(new Toggle());
        subCommands.add(new TopWins());
        subCommands.add(new TopLosses());
    }

    public boolean onCommand(CommandSender cs, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("CoinFlip")) {
            if (args.length == 0) {
                if(!(cs instanceof Player)){
                    sendHelp(cs, cmd, 1);
                }else{
                    ((Player)cs).openInventory(instance.getCoinFlipMenuFileUtil().coinFlipInventory);
                }
                return false;
            }
            String subcommand = args[0];
            if (subcommand.equalsIgnoreCase("help")) {
                int page = 1;
                if (args.length < 2) {
                    sendHelp(cs, cmd, page);
                    return false;
                }
                try {
                    page = Integer.parseInt(args[1]);
                } catch (NumberFormatException ignored) {
                }
                sendHelp(cs, cmd, page);
                return false;
            }
            ArrayList<String> a = new ArrayList<>(Arrays.asList(args));
            a.remove(0);
            if (subCommands.stream().noneMatch(c -> c.getName().equalsIgnoreCase(subcommand))) {
                sendHelp(cs, cmd, 1);
            } else {
                subCommands.stream().filter(c -> c.getName().equalsIgnoreCase(args[0])).findFirst().get().run(cs, a.toArray(new String[0]));
            }
            return false;
        }
        return true;
    }

    private void sendHelp(CommandSender cs, Command cmd, int page) {
        new BukkitRunnable() {
            public void run() {
                cs.sendMessage(instance.getUtil().fixColour("&6_______________.[ &2CoinFlip Help Page &c%page% &6]._______________".replace("%page%", "" + page)));
                int perPage = 7;
                int maxPage = subCommands.size() == 0 ? 1 : Math.max((int) Math.ceil((double) subCommands.size() / perPage), 1);
                int actualPage = Math.min(page, maxPage);
                int min = actualPage == 1 ? 0 : actualPage * perPage - perPage;
                int max = actualPage == 1 ? perPage : min + perPage;
                ArrayList<SubCommand> subCommandsForPlayer = new ArrayList<>();
                for (SubCommand subCommand : subCommands) {
                    boolean hasAccess = !subCommand.isPermissionRequired();
                    if (subCommand.isPermissionRequired() && cs.hasPermission(subCommand.getPermission())) {
                        hasAccess = true;
                    }
                    if (hasAccess) {
                        subCommandsForPlayer.add(subCommand);
                    }
                }
                for (int i = min; i < max; i++) {
                    if (subCommandsForPlayer.size() <= i) break;
                    SubCommand c = subCommandsForPlayer.get(i);
                    cs.sendMessage(instance.getUtil().fixColour("&b/" + cmd.getName() + " &7" + c.getName() + " &7" + c.getArgs()));
                }
                cancel();
            }
        }.runTaskAsynchronously(instance);
    }

    @Override
    public java.util.List<String> onTabComplete(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("CoinFlip")) {
            ArrayList<String> possibleCommands = new ArrayList<>();
            for (SubCommand subCommand : subCommands) {
                boolean hasAccess = !subCommand.isPermissionRequired();
                if (subCommand.isPermissionRequired() && cs.hasPermission(subCommand.getPermission())) {
                    hasAccess = true;
                }
                if (hasAccess) {
                    possibleCommands.add(subCommand.getName());
                }
            }
            return possibleCommands;
        }
        return null;
    }

}
