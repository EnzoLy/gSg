package live.ghostly.survivalgames.hotbar;

import live.ghostly.survivalgames.game.menu.VoteMenu;
import live.ghostly.survivalgames.profile.Profile;
import live.ghostly.survivalgames.profile.ProfileState;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Getter
public enum Hotbar {

    SPAWN(profile ->
        profile.getState() == ProfileState.LOBBY,
        new HotbarItem(0, new ItemStack(Material.BOOK), Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK).addInteract((player) -> new VoteMenu().openMenu(player))
    );


    private Predicate<Profile> filter;
    private List<HotbarItem> items;

    Hotbar(Predicate<Profile> filter, HotbarItem... items) {
        this.filter = filter;
        this.items = Arrays.asList(items);
    }

    public void giveItems(Player player){
        items.forEach(hotbarItem -> {
            ItemStack itemStack = hotbarItem.getItem();
            Integer slot = hotbarItem.getSlot();
            player.getInventory().setItem(slot, itemStack);
        });

        player.updateInventory();
    }


    public static List<Hotbar> getHotbars(){
        return Arrays.asList(values());
    }

    public HotbarItem getItem(ItemStack itemStack){
        return items.stream().filter(hotbarItem -> hotbarItem.getItem().isSimilar(itemStack)).findFirst().orElse(null);
    }

}
