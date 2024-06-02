package CustomsCoinFlip.Commands.CoinFlipCommands;

import CustomsCoinFlip.Commands.CoinFlipCommands.Sub.*;
import CustomsCoinFlip.CustomsCoinFlipPlugin;
import CustomsCoinFlip.Objects.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

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

    String fixColour(String input){
        return ChatColor.translateAlternateColorCodes('&',input);
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
            Optional<SubCommand> subCommandOptional = subCommands.stream().filter(subCommand -> subCommand.getName().equalsIgnoreCase(args[0])).findFirst();
            if(!subCommandOptional.isPresent()){
                sendHelp(cs, cmd, 1);
            }else{
                subCommandOptional.get().run(cs, a.toArray(new String[0]));
            }
            return false;
        }
        return true;
    }

    private void sendHelp(CommandSender cs, Command cmd,int page) {
        new BukkitRunnable() {
            public void run() {
                cs.sendMessage(fixColour("&6_________.[ &2CoinFlip Help Page &c%page% &6]._________".replace("%page%", "" + page)));
                int perPage = 10;
                int maxPage = subCommands.isEmpty() ? 1 : Math.max((int) Math.ceil((double) subCommands.size() / perPage), 1);
                int actualPage = Math.min(page, maxPage);
                int min = actualPage == 1 ? 0 : actualPage * perPage - perPage;
                int max = actualPage == 1 ? perPage : min + perPage;
                ArrayList<SubCommand> subCommandsForPlayer = subCommands.stream().filter(subCommand -> subCommand.hasAccess(cs)).collect(Collectors.toCollection(ArrayList::new));
                for (int i = min; i < max; i++) {
                    if (subCommandsForPlayer.size() <= i) break;
                    SubCommand c = subCommandsForPlayer.get(i);
                    cs.sendMessage(fixColour("&b/" + cmd.getName() + " &7" + c.getName() + " &7" + c.getArgs()));
                }
                cancel();
            }
        }.runTaskAsynchronously(instance);
    }

    @Override
    public java.util.List<String> onTabComplete(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("CoinFlip")) {
            return subCommands.stream().filter(subCommand -> subCommand.hasAccess(cs)).map(SubCommand::getName).collect(Collectors.toCollection(ArrayList::new));
        }
        return null;
    }

}
