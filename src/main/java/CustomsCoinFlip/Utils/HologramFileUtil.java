package CustomsCoinFlip.Utils;

import CustomsCoinFlip.CustomsCoinFlipPlugin;
import CustomsCoinFlip.Enums.HologramType;
import CustomsCoinFlip.Objects.TopHologram;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class HologramFileUtil {

    private final CustomsCoinFlipPlugin instance = CustomsCoinFlipPlugin.getInstance();

    private File holograms;

    public void setup(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        holograms = new File(dir + File.separator + "Holograms.yml");
        if(!holograms.exists()){
            try{
                holograms.createNewFile();
            }catch(IOException e){
                System.out.println(e.getLocalizedMessage());
            }
        }
        loadHolograms(false);
    }

    public void deleteHologram(TopHologram topHologram){
        FileConfiguration config = YamlConfiguration.loadConfiguration(holograms);
        int keys = topHologram.getId();
        config.set(keys + ".id",null);
        config.set(keys + ".world",null);
        config.set(keys + ".x",null);
        config.set(keys + ".y",null);
        config.set(keys + ".z",null);
        config.set(keys + ".hologramType",null);
        config.set(""+keys,null);
        try{
            config.save(holograms);
        }catch(IOException e){
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void saveTopHologram(TopHologram topHologram){
        FileConfiguration config = YamlConfiguration.loadConfiguration(holograms);
        int keys = config.getKeys(false).size() + 1;
        topHologram.setId(keys);
        config.set(keys + ".id",keys);
        config.set(keys + ".world",topHologram.getWorld().getName());
        config.set(keys + ".x",topHologram.getX());
        config.set(keys + ".y",topHologram.getY());
        config.set(keys + ".z",topHologram.getZ());
        config.set(keys + ".hologramType",topHologram.getHologramType().name());
        try{
            config.save(holograms);
        }catch(IOException e){
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void loadHolograms(boolean reload){
        if(reload){
            instance.getTopHolograms().clear();
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(holograms);
        for(String string : config.getKeys(false)){
            ConfigurationSection section = config.getConfigurationSection(string);
            Optional<World> worldOptional = Bukkit.getServer().getWorlds().stream().filter(world -> world.getName().equalsIgnoreCase(section.getString("world"))).findFirst();
            if(!worldOptional.isPresent()){
                System.out.println("Invalid world");
                continue;
            }
            Optional <HologramType> hologramTypeOptional = Arrays.stream(HologramType.values()).filter(hologramType -> hologramType.name().equalsIgnoreCase(section.getString("hologramType"))).findFirst();
            if(!hologramTypeOptional.isPresent()){
                System.out.println("Invalid hologramType");
                continue;
            }
            TopHologram topHologram = new TopHologram(section.getInt("id"), hologramTypeOptional.get(),worldOptional.get(),section.getDouble("x"),section.getDouble("y"),section.getDouble("z"));
            topHologram.spawnHologram();
            instance.getTopHolograms().add(topHologram);
        }
    }

}
