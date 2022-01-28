package live.ghostly.survivalgames.game.listeners;

import com.google.common.collect.Lists;
import live.ghostly.survivalgames.SG;
import live.ghostly.survivalgames.game.GameState;
import live.ghostly.survivalgames.game.SGGame;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.List;

public class BuildSGListeners implements Listener {

    private List<Location> placedBlocks = Lists.newArrayList();

    @EventHandler
    public void onRegen(EntityRegainHealthEvent event) {
        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.EATING || event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED)
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        SGGame game = SG.get().getSgGame();
        if(game.getGameState() != GameState.STARTED) return;
        placedBlocks.add(event.getBlock().getLocation());
    }

    @EventHandler
    public void onPlace(BlockBreakEvent event){
        SGGame game = SG.get().getSgGame();
        if(game.getGameState() != GameState.STARTED) return;
        if(!placedBlocks.contains(event.getBlock().getLocation())) event.setCancelled(true);
        else placedBlocks.remove(event.getBlock().getLocation());
    }

}