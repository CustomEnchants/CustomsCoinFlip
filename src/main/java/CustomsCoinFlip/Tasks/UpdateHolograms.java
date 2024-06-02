package CustomsCoinFlip.Tasks;

import CustomsCoinFlip.CustomsCoinFlipPlugin;
import CustomsCoinFlip.Objects.TopHologram;

public class UpdateHolograms implements Runnable{

    private final CustomsCoinFlipPlugin instance = CustomsCoinFlipPlugin.getInstance();

    public void run(){
        instance.getTopHolograms().forEach(TopHologram::update);
    }
}
