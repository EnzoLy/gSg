package live.ghostly.survivalgames.profile;

import com.google.common.collect.Maps;
import live.ghostly.survivalgames.game.GameType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Profile {

    @Getter private static Map<UUID, Profile> profiles = Maps.newHashMap();

    private final UUID uuid;
    @Setter private ProfileState state = ProfileState.LOBBY;
    @Setter private GameType voteType;

    public void spectate(Player player){

    }

    public static Profile getByPlayer(Player player){
        if(profiles.containsKey(player.getUniqueId())) return profiles.get(player.getUniqueId());
        return new Profile(player.getUniqueId());
    }

    public Player toPlayer(){
        return Bukkit.getPlayer(uuid);
    }

}
