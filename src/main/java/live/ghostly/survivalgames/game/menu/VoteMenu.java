package live.ghostly.survivalgames.game.menu;

import com.google.common.collect.Maps;
import live.ghostly.survivalgames.game.GameType;
import live.ghostly.survivalgames.game.vote.Vote;
import live.ghostly.survivalgames.profile.Profile;
import lombok.AllArgsConstructor;
import me.joeleoli.nucleus.menu.Button;
import me.joeleoli.nucleus.menu.Menu;
import me.joeleoli.nucleus.util.ItemBuilder;
import me.joeleoli.nucleus.util.Style;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class VoteMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return Style.translate("&bVote GameType");
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        int i = 12;
        for (GameType type : GameType.values()){
            if(type != GameType.COMBO){ //Coming soon
                buttons.put(i++, new VoteButton(type));
            }
        }

        return buttons;
    }

    @AllArgsConstructor
    public class VoteButton extends Button{

        private GameType type;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.PAPER)
                    .name("&6Vote for &f" + type.getName())
                    .lore(Style.translate("&aVotes&7:&e " + Vote.getVotesType(type)))
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            Profile profile = Profile.getByPlayer(player);
            if(profile.getVoteType() != null){
                if(profile.getVoteType() == type){
                    Vote.removeVote(type);
                    profile.setVoteType(null);
                }else{
                    Vote.removeVote(profile.getVoteType());
                    Vote.addVote(type);
                    profile.setVoteType(type);
                }
            }else{
                Vote.addVote(type);
                profile.setVoteType(type);
            }
            player.closeInventory();
            new VoteMenu().openMenu(player);
        }
    }
}
