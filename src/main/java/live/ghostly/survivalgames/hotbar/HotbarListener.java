package live.ghostly.survivalgames.hotbar;

import live.ghostly.survivalgames.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class HotbarListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Profile profile = Profile.getByPlayer(player);

        Hotbar hotbar = Hotbar.getHotbars().stream()
                .filter(it -> it.getFilter().test(profile))
                .findFirst().orElse(null);

        if(hotbar == null) return;

        HotbarItem item = hotbar.getItem(player.getItemInHand());

        if(item == null || !item.getActions().contains(event.getAction())) return;

        item.onInteract(player);
    }

}
