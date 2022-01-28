package live.ghostly.survivalgames.game.listeners;

import live.ghostly.survivalgames.SG;
import live.ghostly.survivalgames.game.GameState;
import live.ghostly.survivalgames.game.SGGame;
import live.ghostly.survivalgames.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class GameListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        SGGame game = SG.get().getSgGame();
        game.getPlayers().add(Profile.getByPlayer(player));
        if(game.getPlayersToStart() == Bukkit.getOnlinePlayers().size() && game.getGameState() == GameState.WAITING){
            game.preStart();
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        SGGame game = SG.get().getSgGame();
        if(game.getGameState() != GameState.STARTED){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        block.breakNaturally();
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event){
        if (!(event.getEntity() instanceof Item)) return;
        Item item = (Item) event.getEntity();
        if(item.getItemStack().getType() == Material.CHEST) event.setCancelled(true);
    }
}
