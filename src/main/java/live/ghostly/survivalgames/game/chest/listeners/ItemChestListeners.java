package live.ghostly.survivalgames.game.chest.listeners;

import live.ghostly.survivalgames.game.chest.ItemChest;
import live.ghostly.survivalgames.setup.PlayerSetup;
import me.joeleoli.nucleus.util.Style;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class ItemChestListeners implements Listener {

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event){
        String title = event.getInventory().getTitle();
        if(!title.contains("Inventory of chance ")){
            return;
        }
        Player player = (Player) event.getPlayer();
        PlayerSetup playerSetup = PlayerSetup.getByPlayer(player);
        if(playerSetup.getChance() == 0 || playerSetup.getType() == null){
            return;
        }
        ItemStack[] items = event.getInventory().getContents();
        ItemChest.removeItemsByChance(playerSetup.getChance(), playerSetup.getType());
        for (ItemStack item : items) {
            if(item != null && item.getType() != Material.AIR){
                ItemChest itemChest = new ItemChest(item, playerSetup.getChance(), playerSetup.getType());
                ItemChest.getItems().add(itemChest);
                itemChest.save();
            }
        }

        player.sendMessage(Style.translate("&aInventory saved of&e " + playerSetup.getType().getName()));
    }

}
