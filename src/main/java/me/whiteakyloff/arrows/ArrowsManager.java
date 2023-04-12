package me.whiteakyloff.arrows;

import lombok.var;
import lombok.Getter;

import me.whiteakyloff.arrows.arrow.CustomArrow;
import me.whiteakyloff.arrows.arrow.CustomArrowType;
import me.whiteakyloff.arrows.utils.ItemBuilder;
import me.whiteakyloff.arrows.utils.protocolapi.Sphere;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

import de.tr7zw.changeme.nbtapi.NBTItem;

import ru.winlocker.utils.config.ConfigManager;

public class ArrowsManager
{
    @Getter
    private final ConfigManager arrowsStorage;

    @Getter
    private final List<CustomArrow> arrows = new ArrayList<>();

    @Getter
    private final Map<Player, Sphere> spheresStorage = new HashMap<>();

    @SuppressWarnings("unchecked")
    public ArrowsManager(AkyloffArrows javaPlugin) {
        this.arrowsStorage = ConfigManager.create(javaPlugin, "arrows.yml");

        for (var section : (List<HashMap<String, Object>>) javaPlugin.getConfig().getList("arrows")) {
            var arrowName = String.valueOf(section.get("id"));
            var arrowUUID = this.arrowsStorage.getConfig().getString("arrows." + arrowName) != null ? UUID.fromString(this.arrowsStorage.getConfig().getString("arrows." + arrowName)) : UUID.randomUUID();

            var itemBuilder = ItemBuilder.loadItemBuilder((HashMap<String, Object>) section.get("item"));
            var itemStack = new NBTItem(itemBuilder.build());

            itemStack.setString("arrow-uuid", arrowUUID.toString());

            var arrowAbilities = Arrays.stream(String.valueOf(section.get("arrow-type")).split(", "))
                    .map(type -> CustomArrowType.valueOf(type).getArrowAbility())
                    .collect(Collectors.toList());
            var arrowData = section.entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith("arrow-settings"))
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> String.valueOf(entry.getValue()), (a, b) -> a, HashMap::new));
            this.arrows.add(new CustomArrow(arrowName, arrowUUID, itemStack.getItem(), arrowData, arrowAbilities));
        }
     }

    public CustomArrow getArrow(ItemStack itemStack) {
        var nbtItem = new NBTItem(itemStack);
        if (!nbtItem.hasTag("arrow-uuid")) {
            return null;
        }
        return this.getArrow(UUID.fromString(nbtItem.getString("arrow-uuid")));
    }

    public CustomArrow getArrow(UUID uuid) {
        return this.arrows.stream().filter(x -> x.getUUID().equals(uuid)).findAny().orElse(null);
    }

    public CustomArrow getArrow(String name) {
        return this.arrows.stream().filter(x -> x.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public void disableManager() {
        this.arrows.forEach(x -> this.arrowsStorage.getConfig().set("arrows." + x.getName(), x.getUUID().toString()));
        this.arrowsStorage.simpleSave();
    }
}
