package live.ghostly.survivalgames.game.schematic;

import com.boydti.fawe.object.clipboard.FaweClipboard;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.ClipboardFormats;
import live.ghostly.survivalgames.SG;
import live.ghostly.survivalgames.game.SGGame;
import live.ghostly.survivalgames.utils.Utils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Getter
public class Schematic {

    @Getter private static Map<String, Schematic> schematics = Maps.newHashMap();

    private BlockArrayClipboard clipboard;
    private String name;
    private File dir;
    private int chance = 50;
    private int yOffset = 0;
    EditSession editSession;
    private Location location;
    Vector pastePos;
    com.boydti.fawe.object.schematic.Schematic schematic;
    List<Location> chestList = Lists.newArrayList();

    public Schematic(String name){
        this.name = name.replace(".schematic", "");
        System.out.println("Schematic loading: " + name);
        schematics.put(this.name, this);
        dir = new File(SG.get().getDataFolder() + "/schematics/" + name);
        load();
    }

    public void load() {
        try {
            schematic = ClipboardFormats.findByFile(dir).load(dir);
            clipboard = (BlockArrayClipboard) schematic.getClipboard();

            System.out.println("Schematic loaded: " + name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pasteSchematic(int x, int y, int z) {
        pastePos = new Vector(x, y, z);
        editSession = schematic
            .paste(new BukkitWorld(Bukkit.getWorld("world")),
                pastePos,
                true,
                false,
                null);
        System.out.println("Schematic pasted: " + name);
        System.out.println("Schematic pasted at: " + x + ", "  + y + ", " + z);
        loadChests();
    }

    public void refillChest() {
        SGGame game = SG.get().getSgGame();
        this.chestList.forEach(location -> {
            if(location.getBlock().getType() == Material.CHEST){
                Chest chest = (Chest) location.getBlock().getState();
                game.getItems().forEach(itemChest ->  {
                    if(Utils.RANDOM.nextInt(100) >= itemChest.getChance())
                        chest.getBlockInventory().addItem(itemChest.getItemStack());
                });
            }
        });
    }

    public void loadChests(){
        final int entityOffsetX = pastePos.getBlockX() + clipboard.getMinimumPoint().getBlockX() - clipboard.getOrigin().getBlockX();
        final int entityOffsetY = pastePos.getBlockY() + clipboard.getMinimumPoint().getBlockY() - clipboard.getOrigin().getBlockY();
        final int entityOffsetZ = pastePos.getBlockZ() + clipboard.getMinimumPoint().getBlockZ() - clipboard.getOrigin().getBlockZ();
        clipboard.IMP.forEach(new FaweClipboard.BlockReader() {
            public void run(int x, int y, int z, BaseBlock block) {
                Location location = new Location(Bukkit.getWorld("world"), x + entityOffsetX, y + entityOffsetY, z + entityOffsetZ);

                if(location.getBlock().getType() == Material.CHEST){
                    System.out.println("Chest found for: " + name);
                    chestList.add(location);
                }
            }
        }, false);
        System.out.println("Chests loaded for: " + name);
    }
}
