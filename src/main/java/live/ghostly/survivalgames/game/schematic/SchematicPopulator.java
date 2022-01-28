package live.ghostly.survivalgames.game.schematic;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SchematicPopulator extends BlockPopulator {

    private final ImmutableList<Material> allowMaterials = ImmutableList.<Material>builder()
            .add(Material.AIR)
            .add(Material.SNOW)
            .add(Material.SNOW_BLOCK)
            .add(Material.DIRT)
            .add(Material.GRASS)
            .add(Material.DEAD_BUSH)
            .add(Material.RED_ROSE)
            .add(Material.YELLOW_FLOWER)
            .add(Material.LONG_GRASS)
            .add(Material.SAND)
            .build();

    Location lasPaste;

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();

        if (chunkX >= -1 && chunkX <= 1) {
            return;
        }
        if (chunkZ >= -1 && chunkZ <= 1) {
            return;
        }
        int skipZ = 0;
        int skipX = 0;
        for (int x = 0; x < 16; x++) {
            if (skipX > 0) {
                skipX--;
            } else {
                for (int z = 0; z < 16; z++) {
                    if (skipZ > 0) {
                        skipZ--;
                    } else {
                        List<Schematic> schematics = Lists.newArrayList(Schematic.getSchematics().values());
                        Collections.shuffle(schematics);
                        for (Schematic schematic : schematics) {
                            if(random.nextInt(schematic.getChance()) != 0 ){
                                continue;
                            }

                            if (!canPlace(schematic, chunk, x, z)) {
                                continue;
                            }

                            int finalX = chunk.getX() * 16 + x;
                            int finalZ = chunk.getZ() * 16 + z;
                            int finalY = world.getHighestBlockAt(finalX, finalZ).getY() + schematic.getYOffset();
                            /*Location current = new Location(world, finalX, finalZ, finalY);
                            if(lasPaste != null && lasPaste.distance(current) < 16){
                                continue;
                            }*/
                            schematic.pasteSchematic(finalX, finalY, finalZ);
                            skipZ = schematic.getClipboard().getRegion().getLength() + 16;
                            skipX = schematic.getClipboard().getRegion().getWidth() + 16;
                            break;

                        }
                    }
                }
            }
        }
    }

    public boolean canPlace(Schematic schematic, Chunk chunk, int placeX, int placeZ) {
        int foundY = -1;
        for (int x = 0; x < schematic.getClipboard().getRegion().getWidth(); x++) {
            for (int z = 0; z < schematic.getClipboard().getRegion().getLength(); z++) {
                Block block = chunk.getWorld().getHighestBlockAt(chunk.getX() * 16 + placeX + x, chunk.getZ() * 16 + placeZ + z);
                if ((!this.allowMaterials.contains(block.getType())) || (!this.allowMaterials.contains(block.getRelative(BlockFace.DOWN).getType()))) {
                    return false;
                }
                if ((foundY != -1) && (block.getY() != foundY)) {
                    return false;
                }
                foundY = block.getY();
            }
        }
        return true;
    }
}
