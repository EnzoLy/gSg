package live.ghostly.survivalgames.game.listeners;

import live.ghostly.survivalgames.SG;
import live.ghostly.survivalgames.game.GameState;
import live.ghostly.survivalgames.game.SGGame;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class PotSGListeners implements Listener {

    private SGGame game = SG.get().getSgGame();

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event){
        Player player = event.getPlayer();
        if(player.hasPermission("gsg.bypass") && player.getGameMode() == GameMode.CREATIVE
            && game.getGameState() != GameState.STARTED ) return;
        event.setCancelled(true);
    }
    @EventHandler
    public void onBreakBlock(BlockPlaceEvent event){
        Player player = event.getPlayer();
        if(player.hasPermission("gsg.bypass") && player.getGameMode() == GameMode.CREATIVE
            && game.getGameState() != GameState.STARTED ) return;
        event.setCancelled(true);
    }

}