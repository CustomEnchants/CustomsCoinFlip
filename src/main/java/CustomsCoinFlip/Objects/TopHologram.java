package CustomsCoinFlip.Objects;

import CustomsCoinFlip.CustomsCoinFlipPlugin;
import CustomsCoinFlip.Enums.HologramType;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import eu.decentsoftware.holograms.api.DHAPI;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class TopHologram {

    private final CustomsCoinFlipPlugin instance = CustomsCoinFlipPlugin.getInstance();
    private double x,y,z;
    private World world;
    private int id;
    private HologramType hologramType;
    private Object hologram;

    public TopHologram(int id, HologramType hologramType, World world, double x, double y, double z){
        setId(id);
        setHologramType(hologramType);
        setWorld(world);
        setX(x);
        setY(y);
        setZ(z);
    }

    public TopHologram(HologramType hologramType, Location location){
        setHologramType(hologramType);
        setWorld(location.getWorld());
        setX(location.getX());
        setY(location.getY());
        setZ(location.getZ());
    }

    public HologramType getHologramType() {
        return hologramType;
    }

    public void setHologramType(HologramType hologramType) {
        this.hologramType = hologramType;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public int getId() {
        return id;
    }

    public World getWorld(){
        return world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Location getLocation(){
        return new Location(getWorld(),getX(),getY(),getZ());
    }

    public void spawnHologram(){
        if(!instance.hookedDecentHolograms && !instance.hookedHolographicDisplays){
            return;
        }
        if(instance.hookedHolographicDisplays) {
            hologram = HologramsAPI.createHologram(instance, getLocation());
            return;
        }
        hologram = DHAPI.createHologram(getHologramType().name()+id,getLocation(),false,new ArrayList<>());
    }

    public void update(){
        if(!instance.hookedDecentHolograms && !instance.hookedHolographicDisplays){
            return;
        }

        ArrayList<String> lines = new ArrayList<>();
        switch(getHologramType()){
            case WINS:{
                ArrayList<String> users = instance.getFileUtil().database.getTop10Wins();
                lines.addAll(instance.getFileUtil().hologramLinesTopWinsHeader);
                IntStream.range(0, 10).forEach(i -> {
                    if (users.size() <= i) {
                        lines.add(instance.getFileUtil().hologramLinesTopWinsEmptyLine.replace("%rank%", "" + (i + 1)));
                    } else {
                        String[] split = users.get(i).split(" ");
                        int amount = Integer.parseInt(split[1]);
                        String amountFormatted = instance.getUtil().formatInt(amount);
                        lines.add(instance.getFileUtil().hologramLinesTopWinsLine.replace("%player%", split[0]).replace("%winsformatted%", amountFormatted).replace("%wins%",""+amount).replace("%rank%", "" + (i + 1)));
                    }
                });
                lines.addAll(instance.getFileUtil().hologramLinesTopWinsFooter);
                break;
            }
            case LOSSES:{
                ArrayList<String> users = instance.getFileUtil().database.getTop10Losses();
                lines.addAll(instance.getFileUtil().hologramLinesTopLossesHeader);
                IntStream.range(0, 10).forEach(i -> {
                    if (users.size() <= i) {
                        lines.add(instance.getFileUtil().hologramLinesTopLossesEmptyLine.replace("%rank%", "" + (i + 1)));
                    } else {
                        String[] split = users.get(i).split(" ");
                        int amount = Integer.parseInt(split[1]);
                        String amountFormatted = instance.getUtil().formatInt(amount);
                        lines.add(instance.getFileUtil().hologramLinesTopLossesLine.replace("%player%", split[0]).replace("%lossesformatted%", amountFormatted).replace("%losses%",""+amount).replace("%rank%", "" + (i + 1)));
                    }
                });
                lines.addAll(instance.getFileUtil().hologramLinesTopLossesFooter);
                break;
            }
            default:{
                break;
            }
        }
        new BukkitRunnable(){
            public void run(){
                if(instance.hookedHolographicDisplays) {
                    ((Hologram) hologram).clearLines();
                    lines.forEach(((Hologram) hologram)::appendTextLine);
                    return;
                }
                ((eu.decentsoftware.holograms.api.holograms.Hologram)hologram).destroy();
                hologram = DHAPI.createHologram(getHologramType().name()+id,getLocation(),false,lines);
            }
        }.runTask(instance);
    }

    public void destroy(){
        if(!instance.hookedDecentHolograms && !instance.hookedHolographicDisplays){
            return;
        }
        if(instance.hookedHolographicDisplays) {
            ((Hologram)hologram).delete();
            return;
        }
        ((eu.decentsoftware.holograms.api.holograms.Hologram)hologram).destroy();
    }
}
