package live.ghostly.survivalgames;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import live.ghostly.survivalgames.board.SGBoardProvided;
import live.ghostly.survivalgames.game.SGGame;
import live.ghostly.survivalgames.game.chest.ItemChest;
import live.ghostly.survivalgames.game.chest.listeners.ItemChestListeners;
import live.ghostly.survivalgames.game.listeners.GameListener;
import live.ghostly.survivalgames.hotbar.HotbarListener;
import live.ghostly.survivalgames.listeners.PlayerListeners;
import live.ghostly.survivalgames.listeners.WorldListeners;
import live.ghostly.survivalgames.profile.ProfileListener;
import lombok.Getter;
import me.joeleoli.nucleus.command.CommandHandler;
import me.joeleoli.nucleus.config.ConfigCursor;
import me.joeleoli.nucleus.config.FileConfig;
import me.joeleoli.nucleus.scoreboard.NucleusScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collections;

@Getter
public class SG extends JavaPlugin {

    private SGGame sgGame;

    private MongoDatabase mongoDatabase;
    private FileConfig mainFileConfig;

    @Override
    public void onEnable() {
        this.mainFileConfig = new FileConfig(this, "config.yml");
        loadMongo();
        ItemChest.loadItems();
        sgGame = new SGGame();
        sgGame.init();
        Arrays.asList(
            new PlayerListeners(),
            new HotbarListener(),
            new WorldListeners(),
            new ItemChestListeners(),
            new GameListener(),
            new ProfileListener()
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
        NucleusScoreboard.setConfiguration(SGBoardProvided.create());
        CommandHandler.loadCommandsFromPackage(this, "live.ghostly.survivalgames.game.chest.commands");
    }


    private void loadMongo() {
        ConfigCursor cursor = new ConfigCursor(this.getMainFileConfig(), "MONGO");
        if (cursor.getBoolean("AUTHENTICATION.ENABLED")) {
            ServerAddress serverAddress = new ServerAddress(cursor.getString("HOST"),
                    cursor.getInt("PORT"));

            MongoCredential credential = MongoCredential.createCredential(
                    cursor.getString("AUTHENTICATION.USERNAME"), "admin",
                    cursor.getString("AUTHENTICATION.PASSWORD").toCharArray());

            MongoClient mongoClient = new MongoClient(serverAddress, Collections.singletonList(credential));
            mongoDatabase = mongoClient.getDatabase("test");
        } else {
            mongoDatabase = new MongoClient(cursor.getString("HOST"),
                    cursor.getInt("PORT")).getDatabase(cursor.getString("DATABASE"));
        }
    }

    public static SG get(){
        return SG.getPlugin(SG.class);
    }
}
