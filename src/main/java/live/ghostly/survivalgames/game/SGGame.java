package live.ghostly.survivalgames.game;

import com.google.common.collect.Lists;
import live.ghostly.survivalgames.SG;
import live.ghostly.survivalgames.game.chest.ItemChest;
import live.ghostly.survivalgames.game.listeners.BuildSGListeners;
import live.ghostly.survivalgames.game.schematic.Schematic;
import live.ghostly.survivalgames.game.vote.Vote;
import live.ghostly.survivalgames.hotbar.Hotbar;
import live.ghostly.survivalgames.profile.Profile;
import live.ghostly.survivalgames.profile.ProfileState;
import lombok.Getter;
import lombok.Setter;
import me.joeleoli.nucleus.util.PlayerUtil;
import me.joeleoli.nucleus.util.Style;
import me.joeleoli.nucleus.util.countdown.Countdown;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Getter
public class SGGame {

    private List<Profile> players = Lists.newArrayList();
    @Setter private Location spawn;
    //private Location lobby =
    private List<Location> chests = Lists.newArrayList();
    private List<ItemChest> items = Lists.newArrayList();
    private GameType type = GameType.POT;
    private int playersToStart = 1;
    @Setter private GameState gameState = GameState.WAITING;

    public void init() {
        loadSchematics();
        for (GameType value : GameType.values()) {
            new Vote(value);
        }
    }

    public void preStart(){
        SG.get().getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb shape square");
        SG.get().getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb world set 300 0 0");
        SG.get().getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb world fill 5000");
        SG.get().getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb fill confirm");
        gameState = GameState.VOTING;
        Bukkit.getOnlinePlayers().forEach(Hotbar.SPAWN::giveItems);
        Countdown.of(2, TimeUnit.MINUTES)
            .broadcastAt(2, TimeUnit.MINUTES)
            .broadcastAt(100, TimeUnit.SECONDS)
            .broadcastAt(80, TimeUnit.SECONDS)
            .broadcastAt(60, TimeUnit.SECONDS)
            .broadcastAt(60, TimeUnit.SECONDS)
            .broadcastAt(50, TimeUnit.SECONDS)
            .broadcastAt(40, TimeUnit.SECONDS)
            .broadcastAt(30, TimeUnit.SECONDS)
            .broadcastAt(20, TimeUnit.SECONDS)
            .broadcastAt(10, TimeUnit.SECONDS)
            .broadcastAt(5, TimeUnit.SECONDS)
            .broadcastAt(4, TimeUnit.SECONDS)
            .broadcastAt(3, TimeUnit.SECONDS)
            .broadcastAt(2, TimeUnit.SECONDS)
            .broadcastAt(1, TimeUnit.SECONDS)
            .withMessage("&c[gSG] &fGame start in&7: &c{time}")
            .onFinish(() -> {
                gameState = GameState.STARTED;
                Vote vote = Vote.getVoteList().values().stream().max(Comparator.comparingInt(Vote::getVotes)).get();
                type = vote.getType();
                Bukkit.broadcastMessage(Style.translate("&c[gSG] &fGame type selected is&7: &c" + type.getName()));
                start();
            })
            .start();
    }

    public void registerListeners() {
        if(type == GameType.BUILD){
            Bukkit.getPluginManager().registerEvents(new BuildSGListeners(), SG.get());
        }
    }

    public void loadItems() {
        items = ItemChest.getItemsByType(type);
        Schematic.getSchematics().values().forEach(Schematic::refillChest);
    }

    public void loadSchematics(){
        try {
            List<String> files = Files.walk(Paths.get(SG.get().getDataFolder() + "/schematics"))
                    .map(Path::toString)
                    .filter(file -> file.endsWith(".schematic"))
                    .map(file -> file.replace("plugins\\gSG\\schematics\\", ""))
                    .collect(Collectors.toList());
            files.forEach(Schematic::new);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        registerListeners();
        loadItems();
        gameState = GameState.STARTED;
        players.forEach(profile -> {
            Player player = profile.toPlayer();
            Profile.getByPlayer(player).setState(ProfileState.PLAYING);
            PlayerUtil.reset(player);
            player.teleport(spawn);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60, 1));
        });
    }

    public void end(){
        gameState = GameState.ENDED;
    }
}
