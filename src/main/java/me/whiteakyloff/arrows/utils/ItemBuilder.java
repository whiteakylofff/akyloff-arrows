package me.whiteakyloff.arrows.utils;

import lombok.var;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public class ItemBuilder
{
    public static ItemBuilder loadItemBuilder(HashMap<String, Object> section) {
        // не знаю зачем...
        var material = section.containsKey("type") ? Material.valueOf(String.valueOf(section.get("type")).toUpperCase()) : Material.TIPPED_ARROW;
        var itemBuilder = new ItemBuilder(material);

        if (section.containsKey("title")) {
            itemBuilder.setTitle(String.valueOf(section.get("title")));
        }
        if (section.containsKey("lore")) {
            itemBuilder.setLore((List<String>) section.get("lore"));
        }
        if (section.containsKey("color")) {
            itemBuilder.setPotionColor(String.valueOf(section.get("color")));
        }
        itemBuilder.setAmount(section.containsKey("amount") ? (int) section.get("amount") : 1);
        if (section.containsKey("flags")) {
            ((List<String>) section.get("flags")).forEach(flag -> itemBuilder.addFlag(ItemFlag.valueOf(flag.toUpperCase())));
        }
        if (section.containsKey("enchants")) {
            for (var enchant : ((List<String>) section.get("enchants"))) {
                var args = enchant.split(":");

                itemBuilder.addEnchant(Enchantment.getByName(args[0].toUpperCase()), Integer.parseInt(args[1]));
            }
        }
        if (section.containsKey("effects")) {
            for (var effect : ((List<String>) section.get("effects"))) {
                var args = effect.split(":");

                var potionType = PotionEffectType.getByName(args[0].toUpperCase());
                var level = Integer.parseInt(args[1]) - 1;
                var duration = Integer.parseInt(args[2]) * 20;

                itemBuilder.addPotionEffect(new PotionEffect(potionType, duration, level));
            }
        }
        return itemBuilder;
    }

    private final ItemStack item;

    public ItemBuilder(ItemStack item) {
        this.item = item;
    }

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
    }

    public static ItemBuilder of(ItemStack item) {
        return new ItemBuilder(item);
    }

    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }

    public ItemStack build() {
        return item;
    }

    public ItemBuilder setTitle(String title) {
        if (title == null) {
            return this;
        }
        var itemMeta = this.item.getItemMeta();

        itemMeta.setDisplayName(this.translateColor(title));
        this.item.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        if (lore == null) {
            return this;
        }
        var itemMeta = this.item.getItemMeta();

        itemMeta.setLore(this.translateColor(lore));
        this.item.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.item.setAmount(amount);

        return this;
    }

    public ItemBuilder addFlag(ItemFlag flag) {
        var itemMeta = this.item.getItemMeta();

        itemMeta.addItemFlags(flag);
        this.item.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        var itemMeta = this.item.getItemMeta();

        itemMeta.addEnchant(enchantment, level, true);
        this.item.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder setPotionColor(String color) {
        if (!(this.item.getItemMeta() instanceof PotionMeta)) {
            return this;
        }
        var itemMeta = (PotionMeta) this.item.getItemMeta();

        itemMeta.setColor(this.getColor(color));
        this.item.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder addPotionEffect(PotionEffect effect) {
        if(!(this.item.getItemMeta() instanceof PotionMeta)) return this;

        PotionMeta meta = (PotionMeta) this.item.getItemMeta();
        meta.addCustomEffect(effect, true);
        this.item.setItemMeta(meta);

        return this;
    }

    private String translateColor(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    private List<String> translateColor(List<String> list) {
        return list.stream().map(this::translateColor).collect(Collectors.toList());
    }

    private Color getColor(String hexString) {
        return Color.fromRGB(Integer.valueOf(hexString.substring(1, 3), 16),
                Integer.valueOf(hexString.substring(3, 5), 16),
                Integer.valueOf(hexString.substring(5, 7), 16));
    }
}
