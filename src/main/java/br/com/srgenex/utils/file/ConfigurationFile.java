package br.com.srgenex.utils.file;

import br.com.srgenex.utils.GXUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings("unused")
@Getter
@Setter
public class ConfigurationFile implements Serializable {

    private Plugin plugin;
    private final String name;
    private File file, parent;
    private FileConfiguration fileConfiguration;
    private YamlConfiguration defaultFile;

    public ConfigurationFile(String name, File parent, String s) {
        this.plugin = GXUtils.getInstance();
        this.name = name+".yml";
        if(!parent.exists())
            if(!parent.mkdirs()) plugin.getLogger().severe("Cannot create the "+name+" directory.");
        this.parent = parent;
        file = new File(parent, this.name);
        if(plugin.getResource(s) != null && !file.exists())
            plugin.saveResource(s, false);
        defaultFile = YamlConfiguration.loadConfiguration(file);
        reloadConfig();
    }

    public void reloadConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
        fileConfiguration.setDefaults(defaultFile);
    }

    public void saveConfig() {
        try {
            fileConfiguration.save(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public ItemStack getItem(String path) {
        return (ItemStack) get(path);
    }

    public boolean exists() {
        return file.exists();
    }

    public void add(String path, Object value) {
        fileConfiguration.addDefault(path, value);
    }

    public Object get(String path) {
        Object obj = fileConfiguration.get(path);
        if (obj instanceof ConfigurationSection) obj = toMap(path);
        return obj;
    }

    public Map<String, Object> toMap(String path) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        Map<String, Object> sec = getSection(path).getValues(false);
        for (Entry<String, Object> entry : sec.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof ConfigurationSection) map.put(name, toMap(path + "." + name));
            else map.put(name, value);
        }
        return map;

    }

    public boolean getBoolean(String path, boolean def) {
        return fileConfiguration.getBoolean(path, def);
    }

    public ConfigurationSection getSection(String path) {
        if (!fileConfiguration.isConfigurationSection(path)) fileConfiguration.createSection(path);
        return fileConfiguration.getConfigurationSection(path);
    }

    public double getDouble(String path) {
        return fileConfiguration.getDouble(path);
    }

    public double getDouble(String path, Double def) {
        return fileConfiguration.getDouble(path, def);
    }

    public int getInt(String path) {
        return fileConfiguration.getInt(path);
    }

    public int getInt(String path, Integer def) {
        return fileConfiguration.getInt(path, def);
    }

    public List<Integer> getIntegerList(String path) {
        return fileConfiguration.getIntegerList(path);
    }

    public String getString(String path) {
        return fileConfiguration.getString(path);
    }

    public String getString(String path, String def) {
        return fileConfiguration.getString(path, def);
    }

    public List<String> getStringList(String path) {
        return fileConfiguration.getStringList(path) == null ? new ArrayList<>() : fileConfiguration.getStringList(path);
    }

    public void set(String path, Object value) {
        fileConfiguration.set(path, value);
    }

}