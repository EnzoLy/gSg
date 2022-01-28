package live.ghostly.survivalgames.game.chest;

import com.google.common.collect.Lists;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import live.ghostly.survivalgames.SG;
import live.ghostly.survivalgames.game.GameType;
import live.ghostly.survivalgames.utils.ItemUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ItemChest {

    @Getter private static MongoCollection<Document> collection = SG.get().getMongoDatabase().getCollection("items");
    @Getter private static List<ItemChest> items = Lists.newArrayList();

    private ItemStack itemStack;
    private int chance;
    private GameType type;

    public static void loadItems(){
        collection.find().forEach((Block<Document>) document -> {
            ItemChest itemChest = new ItemChest();
            itemChest.setItemStack(ItemUtils.deSerialized(document.getString("item")));
            itemChest.setChance(document.getInteger("chance"));
            itemChest.setType(GameType.valueOf(document.getString("type")));
            items.add(itemChest);
        });
    }

    public void save(){
        Document document = new Document();
        document.put("item", ItemUtils.serialize(itemStack));
        document.put("chance", chance);
        document.put("type", type.name());
        collection.replaceOne(Filters.or(
                        Filters.eq("item", ItemUtils.serialize(itemStack)),
                        Filters.eq("chance", chance),
                        Filters.eq("type", type.name())
                ), document,new ReplaceOptions().upsert(true));
    }


    public static void removeItemsByChance(int chance, GameType type){
        items.removeIf(itemChest -> itemChest.getType() == type && itemChest.getChance() == chance);
    }

    public static List<ItemChest> getItemsByChance(int chance, GameType type){
        return items.stream().filter(itemChest -> itemChest.getChance() == chance && itemChest.getType() == type)
                .collect(Collectors.toList());
    }
    public static List<ItemChest> getItemsByType(GameType type){
        return items.stream().filter(itemChest -> itemChest.getType() == type)
                .collect(Collectors.toList());
    }
}
