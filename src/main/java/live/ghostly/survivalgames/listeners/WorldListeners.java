package live.ghostly.survivalgames.listeners;

import live.ghostly.survivalgames.SG;
import live.ghostly.survivalgames.game.schematic.SchematicPopulator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldListeners implements Listener {

    @EventHandler
    public void onWorldInit(WorldInitEvent event) {
        if(event.getWorld().getEnvironment() == World.Environment.NORMAL){
            event.getWorld().setSpawnLocation(0, 70, 0);
            event.getWorld().getPopulators().add(new SchematicPopulator());
        }
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event){
        if(event.getWorld().getName().equals("world")){
            SG.get().getSgGame().setSpawn(event.getWorld().getSpawnLocation());
        }
    }
}
