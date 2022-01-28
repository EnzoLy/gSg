package live.ghostly.survivalgames.game.chest.commands;

import live.ghostly.survivalgames.game.GameType;
import live.ghostly.survivalgames.game.chest.ItemChest;
import live.ghostly.survivalgames.setup.PlayerSetup;
import me.joeleoli.nucleus.command.Command;
import me.joeleoli.nucleus.command.param.Parameter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class ItemChestCommands {

    @Command(names = "chestpot add")
    public static void addItem(Player player, @Parameter(name = "chance") int chance){
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, "Inventory of chance " + chance);
        List<ItemChest> items = ItemChest.getItemsByChance(chance, GameType.POT);

        items.forEach(itemChest -> inventory.addItem(itemChest.getItemStack()));
        player.openInventory(inventory);
        PlayerSetup playerSetup = PlayerSetup.getByPlayer(player);
        playerSetup.setChance(chance);
        playerSetup.setType(GameType.POT);
    }

}
