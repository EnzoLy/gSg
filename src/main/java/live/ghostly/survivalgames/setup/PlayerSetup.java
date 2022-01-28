package live.ghostly.survivalgames.setup;

import com.google.common.collect.Maps;
import live.ghostly.survivalgames.game.GameType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

@Getter
public class PlayerSetup {

    private static Map<UUID, PlayerSetup> playerSetupMap = Maps.newHashMap();

    private UUID uuid;
    @Setter private int chance;
    @Setter private GameType type;

    public PlayerSetup(UUID uuid){
        this.uuid = uuid;
        playerSetupMap.put(uuid, this);
    }

    public static PlayerSetup getByPlayer(Player player){
        return getByUUID(player.getUniqueId());
    }

    private static PlayerSetup getByUUID(UUID uuid){
        if(playerSetupMap.containsKey(uuid)) return playerSetupMap.get(uuid);
        return new PlayerSetup(uuid);
    }
}
