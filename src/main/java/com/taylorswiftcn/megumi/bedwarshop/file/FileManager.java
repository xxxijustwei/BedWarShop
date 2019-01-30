package com.taylorswiftcn.megumi.bedwarshop.file;

import com.taylorswiftcn.megumi.bedwarshop.BedwarShop;
import com.taylorswiftcn.megumi.bedwarshop.util.WeiUtil;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileManager {
    private BedwarShop plugin;

    @Getter
    private YamlConfiguration config;
    @Getter
    private YamlConfiguration message;
    @Getter
    private YamlConfiguration shop;
    @Getter
    private YamlConfiguration item;

    public FileManager(BedwarShop plugin) {
        this.plugin = plugin;
    }

    public void init() {
        config = new YamlConfiguration();
        message = new YamlConfiguration();
        shop = new YamlConfiguration();
        item = new YamlConfiguration();

        config = initFile("config.yml");
        message = initFile("message.yml");
        shop = initFile("shop.yml");
        item = initFile("item.yml");
    }

    private YamlConfiguration initFile(String name) {
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            WeiUtil.copyFile(plugin.getResource(name), file);
            WeiUtil.log(String.format("File: 已生成 %s 文件", name));
        }
        else WeiUtil.log(String.format("File: 已加载 %s 文件", name));
        return YamlConfiguration.loadConfiguration(file);
    }
}
