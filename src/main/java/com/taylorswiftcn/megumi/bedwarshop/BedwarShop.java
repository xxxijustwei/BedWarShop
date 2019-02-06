package com.taylorswiftcn.megumi.bedwarshop;

import com.taylorswiftcn.megumi.bedwarshop.commands.MainCommand;
import com.taylorswiftcn.megumi.bedwarshop.database.SQLManager;
import com.taylorswiftcn.megumi.bedwarshop.exp.ExpManager;
import com.taylorswiftcn.megumi.bedwarshop.file.FileManager;
import com.taylorswiftcn.megumi.bedwarshop.file.Config;
import com.taylorswiftcn.megumi.bedwarshop.file.Message;
import com.taylorswiftcn.megumi.bedwarshop.listener.BedwarGameListener;
import com.taylorswiftcn.megumi.bedwarshop.listener.ResourcePickupListener;
import com.taylorswiftcn.megumi.bedwarshop.listener.gui.EditGuiListener;
import com.taylorswiftcn.megumi.bedwarshop.listener.PlayerListener;
import com.taylorswiftcn.megumi.bedwarshop.listener.gui.ShopGuiListener;
import com.taylorswiftcn.megumi.bedwarshop.shop.ShopManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BedwarShop extends JavaPlugin {

    @Getter private static BedwarShop instance;
    @Getter private FileManager fileManager;
    @Getter private ShopManager shopManager;
    @Getter private ExpManager expManager;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();

        if (Bukkit.getPluginManager().getPlugin("BedwarsRel") == null) {
            getLogger().info("Hook: 关联 BedwarsRel 失败,插件已自动卸载");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        else getLogger().info("Hook: 关联 BedwarsRel 成功");

        instance = this;
        fileManager = new FileManager(this);
        shopManager = new ShopManager(this);
        expManager = new ExpManager(this);

        fileManager.init();
        expManager.init();
        Config.init();
        Message.init();

        shopManager.init();

        if (Config.SQL_Enable) SQLManager.init();
        else {
            getLogger().info("Error: 未启用数据库,插件已自动卸载");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("bws").setExecutor(new MainCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new ResourcePickupListener(), this);
        Bukkit.getPluginManager().registerEvents(new BedwarGameListener(), this);
        Bukkit.getPluginManager().registerEvents(new ShopGuiListener(), this);
        Bukkit.getPluginManager().registerEvents(new EditGuiListener(), this);

        for (Player p : Bukkit.getOnlinePlayers()) {
            getShopManager().loadPlayer(p);
        }

        long end = System.currentTimeMillis();
        getLogger().info("加载成功! 用时 %time% ms".replace("%time%", String.valueOf(end - start)));
    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            getShopManager().savePlayer(p);
        }

        closeInventory();

        getLogger().info("卸载成功!");
    }

    public void closeInventory() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getOpenInventory() == null) continue;
            if (p.getOpenInventory().getTitle().contains(Config.Shop_Title) || p.getOpenInventory().getTitle().equals(Config.Edit_Title)) {
                p.closeInventory();
                shopManager.getEditGui().clearGlass(p);
            }
        }
    }

    public String getVersion() {
        String packet = Bukkit.getServer().getClass().getPackage().getName();
        return packet.substring(packet.lastIndexOf('.') + 1);
    }
}
