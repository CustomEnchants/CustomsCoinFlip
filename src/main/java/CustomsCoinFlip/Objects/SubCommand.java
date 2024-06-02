package CustomsCoinFlip.Objects;

import CustomsCoinFlip.CustomsCoinFlipPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {

    private final String name;
    private final String desc;
    private final String args;
    private final String permission;
    private final boolean requirePermission;
    public final CustomsCoinFlipPlugin instance = CustomsCoinFlipPlugin.getInstance();

    protected SubCommand(String name, String desc, String args, String permission, boolean requirePermission) {
        this.name = name;
        this.desc = desc;
        this.args = args;
        this.permission = permission;
        this.requirePermission = requirePermission;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return desc;
    }

    public String getArgs() {
        return args;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isPermissionRequired() {
        return requirePermission;
    }

    public abstract void run(CommandSender cs, String[] args);

    protected String fixColour(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

}
