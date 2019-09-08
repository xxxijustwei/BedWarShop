package com.taylorswiftcn.megumi.bedwarshop.file;

import com.taylorswiftcn.megumi.bedwarshop.BedwarShop;
import com.taylorswiftcn.megumi.bedwarshop.util.WeiUtil;
import io.github.bedwarsrel.BedwarsRel;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    private static YamlConfiguration config;
    public static String Prefix;
    public static Boolean SQL_Enable;
    public static String SQL_Host;
    public static String SQL_Port;
    public static String SQL_Database;
    public static String SQL_Users;
    public static String SQL_Password;

    public static String Shop_Title;
    public static String Exp_Suffix;
    public static String Edit_Title;
    public static List<String> Price_Lore;
    public static HashMap<ItemStack, Integer> Resource;
    public static String DeathLost;
    public static String KillObtain;

    public static void init() {
        config = BedwarShop.getInstance().getFileManager().getConfig();
        Prefix = getString("Prefix");
        SQL_Enable = config.getBoolean("MYSQL.Enable");
        SQL_Host = getString("MYSQL.Host");
        SQL_Port = getString("MYSQL.Port");
        SQL_Database = getString("MYSQL.Database");
        SQL_Users = getString("MYSQL.Users");
        SQL_Password = getString("MYSQL.Password");

        Shop_Title = getString("Config.Shop-Title");
        Exp_Suffix = getString("Config.Exp-Suffix");
        Edit_Title = getString("Config.Edit-Title");
        Price_Lore = getStringList("Config.Price-Lore");

        Resource = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection("Config.Resource");
        for (String s : section.getKeys(false)) {
            ItemStack item = getResItem(s);
            if (item == null) continue;
            Resource.put(item, section.getInt(s));
        }

        DeathLost = getString("Config.DeathLost");
        KillObtain = getString("Config.KillObtain");
    }

    public static ItemStack getItem(String s) {
        String id = config.get(String.format("Item.%s.ID", s)).toString();
        Integer data = config.getInt(String.format("Item.%s.Data", s));
        String name = config.getString(String.format("Item.%s.Name", s));
        List<String> lore = config.getStringList(String.format("Item.%s.Lore", s));

        return WeiUtil.createItem(id, data, 1, name, lore, null, null);
    }

    private static String getString(String path) {
        return WeiUtil.onReplace(config.getString(path));
    }

    private static List<String> getStringList(String path) {
        return WeiUtil.onReplace(config.getStringList(path));
    }

    private static ItemStack getResItem(String name) {
        ItemStack item;
        List<Object> resourceList = (List<Object>) BedwarsRel.getInstance().getConfig().getList("resource." + name + ".item");
        for (Object resource : resourceList) {
            item = ItemStack.deserialize((Map<String, Object>) resource);
            if (item != null) {
                return item;
            }
        }
        return null;
    }
}
