package live.ghostly.survivalgames.listeners;

import live.ghostly.survivalgames.SG;
import live.ghostly.survivalgames.game.GameState;
import live.ghostly.survivalgames.game.SGGame;
import live.ghostly.survivalgames.hotbar.Hotbar;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SGGame game = SG.get().getSgGame();
        player.setGameMode(GameMode.CREATIVE);
        if(game.getGameState() == GameState.VOTING) Hotbar.SPAWN.giveItems(player);
    }
}
