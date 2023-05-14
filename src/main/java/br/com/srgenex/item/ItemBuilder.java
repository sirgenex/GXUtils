package br.com.srgenex.item;

import br.com.srgenex.placeholder.ReplacementPlaceholder;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
@Setter
@SuppressWarnings({"unused"})
public class ItemBuilder implements Cloneable {

    public ItemStack item;
    public List<Integer> slots = new ArrayList<>();

    public ItemBuilder(ItemStack item) {
        this.item = item.clone();
    }

    public ItemBuilder(String type) {
        this(new ItemStack(Objects.requireNonNull(Material.getMaterial(type))));
    }

    public ItemBuilder(String type, int amount) {
        this(new ItemStack(Objects.requireNonNull(Material.getMaterial(type)), amount));
    }

    public ItemBuilder(String type, int amount, short data) {
        this(new ItemStack(Objects.requireNonNull(Material.getMaterial(type)), amount, data));
    }

    public ItemBuilder(Material type) {
        this(new ItemStack(type));
    }

    public ItemBuilder(Material type, int amount) {
        this(new ItemStack(type, amount));
    }

    public ItemBuilder(Material type, int amount, short data) {
        this(new ItemStack(type, amount, data));
    }

    public ItemBuilder(FileConfiguration c, String path, String... replacements) {
        String itemName = c.getString(path + ".item", "BEDROCK");
        int data = c.getInt(path + ".data", 0);
        item = new ItemStack(Objects.requireNonNull(Material.getMaterial(itemName)), 1, (short) data);
        if (c.getString(path + ".color") != null) {
            java.awt.Color color = java.awt.Color.decode(Objects.requireNonNull(c.getString(path + ".color")));
            color(Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue()));
        }
        String name = c.getString(path + ".name");
        List<String> lore = null;
        if (c.getString(path + ".lore") != null) {
            List<String> list = new ArrayList<>();
            c.getStringList(path + ".lore").forEach(line -> {
                for (String replacement : replacements) {
                    String previous = replacement.split(";")[0];
                    String next = replacement.split(";")[1];
                    line = line.replace(previous, next);
                }
                list.add(line.replace("&", "§"));
            });
            lore = list;
        }
        if (name != null) {
            for (String replacement : replacements) {
                String previous = replacement.split(";")[0];
                String next = replacement.split(";")[1];
                name = name.replace(previous, next);
            }
            setName(name);
        }
        if (lore != null) setLore(lore);
        if (c.getString(path + ".slot") != null)
            setSlot(c.getInt(path + ".slot", -1));
        else setSlot(-1);

        String skull = c.getString(path + ".skull");
        if (skull != null)
            texture(skull);

        if(c.getString(path+".glow") != null)
            glowIf(c.getBoolean(path+".glow"));

    }

    public ItemBuilder(FileConfiguration c, String path, List<ReplacementPlaceholder> replacements){
        this(c, path, ReplacementPlaceholder.asStringArray(replacements));
    }

    public ItemBuilder(FileConfiguration c, String path, String player, String... replacements) {
        this(c, path, replacements);
        head(player);
    }

    public ItemBuilder updateItem(ItemStack item) {
        this.item = item;
        return this;
    }

    public ItemBuilder setSlot(int slot) {
        List<Integer> l = new ArrayList<>();
        l.add(slot);
        l.addAll(slots);
        slots = l;
        return this;
    }

    public ItemBuilder changeItemMeta(Consumer<ItemMeta> consumer) {
        ItemMeta itemMeta = item.getItemMeta();
        consumer.accept(itemMeta);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder changeItem(Consumer<ItemStack> consumer) {
        consumer.accept(item);
        return this;
    }

    public ItemBuilder setName(String name, String... replacements) {
        for (String replacement : replacements) {
            try {
                String previous = replacement.split(", ")[0];
                String next = replacement.split(", ")[1];
                assert name != null;
                name = name.replace(previous, next.replace("null", ""));
            } catch (Exception ignored) {
            }
        }
        String finalName = name;
        return changeItemMeta(it -> it.setDisplayName(colored(finalName)));
    }

    public ItemBuilder setName(String name, List<ReplacementPlaceholder> replacements) {
        if (name == null) System.out.println("Tried to update a name that is null");
        for (ReplacementPlaceholder replacement : replacements) {
            try {
                name = replacement.replace(name);
            } catch (Exception ignored) {}
        }
        String finalName = name;
        return changeItemMeta(it -> it.setDisplayName(colored(finalName)));
    }

    public ItemBuilder setName(List<String> name, String... replacements) {
        AtomicReference<String> s = new AtomicReference<>();
        name.forEach(s::set);
        return setName(s.get(), replacements);
    }

    public String getName(){
        return item.hasItemMeta() ?
                (item.getItemMeta().hasDisplayName() ?
                item.getItemMeta().getDisplayName() : item.getType().name()) : item.getType().name();
    }

    public ItemBuilder color(Color color) {
        return changeItem(item -> {
            LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
            meta.setColor(color);
            item.setItemMeta(meta);
        });
    }

    public ItemBuilder setLore(String... lore) {
        return changeItemMeta(it -> it.setLore(Arrays.asList(colored(lore))));
    }

    public ItemBuilder setAmount(int amount) {
        return changeItem(i -> i.setAmount(amount));
    }

    public ItemBuilder setLore(List<String> lore, String... replacements) {
        return changeItemMeta(it -> it.setLore(null)).addLore(lore, replacements);
    }

    public ItemBuilder setLore(List<String> lore, List<ReplacementPlaceholder> replacements) {
        return changeItemMeta(it -> it.setLore(null)).addLore(lore, replacements);
    }

    public ItemBuilder addLore(List<String> lore, String... replacements) {
        if (lore == null || lore.isEmpty()) return this;
        List<String> list = new ArrayList<>();
        lore.forEach(line -> {
            for (String replacement : replacements) {
                try {
                    String previous = replacement.split(", ")[0];
                    String next = replacement.split(", ")[1];
                    line = line.replace(previous, next.replace("null", ""));
                } catch (Exception ignored) {
                }
            }
            list.add(line);
        });
        return changeItemMeta(meta -> {
            List<String> originalLore = meta.getLore() == null ? Lists.newArrayList() : meta.getLore();
            originalLore.addAll(list);
            meta.setLore(colored(originalLore));
        });
    }

    public ItemBuilder addLore(List<String> lore, List<ReplacementPlaceholder> replacements) {
        if (lore == null || lore.isEmpty()) return this;
        List<String> list = new ArrayList<>();
        lore.forEach(line -> {
            for (ReplacementPlaceholder replacement : replacements) {
                try {
                    line = replacement.replace(line);
                } catch (Exception ignored) {
                }
            }
            list.add(line);
        });
        return changeItemMeta(meta -> {
            List<String> originalLore = meta.getLore() == null ? Lists.newArrayList() : meta.getLore();
            originalLore.addAll(list);
            meta.setLore(colored(originalLore));
        });
    }

    public ItemBuilder append(ItemBuilder item){
        return append(item.wrap());
    }

    public ItemBuilder append(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        ItemBuilder i = clone();
        if(!hasName()) {
            if(meta.hasDisplayName()) {
                i.setName(meta.getDisplayName());
            }
        }
        if(meta.hasLore()) {
            if (!hasLore()) i.setLore(meta.getLore());
            else i.addLore(meta.getLore());
        }
        if(item.getType() != Material.BEDROCK) {
            i.changeItem(it -> {
                it.setType(item.getType());
                it.setData(item.getData());
            });
        }
        return i;
    }

    public ItemBuilder texture(String textureValue) {
        item.setType(Material.SKULL_ITEM);
        item.setDurability((short) 3);

        SkullMeta headMeta = (SkullMeta) item.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", textureValue));
        Field profileField;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ignored) {}

        item.setItemMeta(headMeta);
        return this;
    }

    public static GameProfile getNonPlayerProfile(String skinURL, boolean randomName) {
        GameProfile newSkinProfile = new GameProfile(UUID.randomUUID(), null);
        newSkinProfile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString("{textures:{SKIN:{url:\"" + skinURL + "\"}}}")));
        return newSkinProfile;
    }

    public ItemBuilder head(String player) {
        return changeItem(item -> {
            item.setType(Material.SKULL_ITEM);
            item.setDurability((short) 3);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwner(player);
            item.setItemMeta(meta);
        });
    }

    public void enchant(Enchantment enchantment, int level) {
        changeItemMeta(itemMeta -> itemMeta.addEnchant(enchantment, level, true));
    }

    public ItemBuilder glow() {
        enchant(Enchantment.LUCK, 1);
        return changeItemMeta(itemMeta -> itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS));
    }

    public ItemBuilder glowIf(Boolean condition) {
        return condition ? glow() : this;
    }

    public ItemBuilder addFlag(ItemFlag... itemFlag) {
        return changeItemMeta(itemMeta -> itemMeta.addItemFlags(itemFlag));
    }

    private static String colored(String message) {
        if(message == null) return null;
        if(message.isEmpty()) return message;
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private static String[] colored(String... messages) {
        for (int i = 0; i < messages.length; i++) {
            messages[i] = colored(messages[i]);
        }

        return messages;
    }

    private static List<String> colored(List<String> description) {

        return description.stream()
                .map(ItemBuilder::colored)
                .collect(Collectors.toList());

    }

    public boolean hasName(){
        if(!getItem().hasItemMeta()) return false;
        return getItem().getItemMeta().hasDisplayName();
    }

    public boolean hasLore(){
        if(!getItem().hasItemMeta()) return false;
        return getItem().getItemMeta().hasLore();
    }

    public ItemStack wrap() {
        return wrap("NULL,  ");
    }

    public ItemStack wrap(String... replacement) {
        ItemStack real = this.item.clone();
        if (getItem().hasItemMeta()) {
            String name = getItem().getItemMeta().getDisplayName();
            setName(name, replacement);
            List<String> lore = getItem().getItemMeta().getLore();
            if (lore != null) setLore(lore, replacement);
        }
        ItemStack item = this.item.clone();
        this.item = real;
        return item;
    }

    public ItemStack wrap(List<ReplacementPlaceholder> placeholders) {
        ItemStack real = this.item.clone();
        if (getItem().hasItemMeta()) {
            String name = getItem().getItemMeta().getDisplayName();
            setName(name, placeholders);
            List<String> lore = getItem().getItemMeta().getLore();
            if (lore != null) setLore(lore, placeholders);
        }
        ItemStack item = this.item.clone();
        this.item = real;
        return item;
    }

    @Override
    public ItemBuilder clone() {
        try {
            ItemBuilder clone = (ItemBuilder) super.clone();
            clone.setItem(this.item.clone());
            clone.setSlots(this.slots);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}
