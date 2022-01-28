package live.ghostly.survivalgames.hotbar;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class HotbarItem {

    private ItemStack item;
    private Integer slot;
    private Consumer<Player> interact;
    private List<Action> actions;

    public HotbarItem(Integer slot, ItemStack item, Action... actions){
        this.item = item;
        this.slot = slot;
        this.actions = Arrays.asList(actions);
    }

    public void onInteract(Player player){
        interact.accept(player);
    }

    public HotbarItem addInteract(Consumer<Player> consumer){
        this.interact = consumer;
        return this;
    }

}
