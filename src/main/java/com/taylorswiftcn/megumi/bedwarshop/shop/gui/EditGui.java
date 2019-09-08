package com.taylorswiftcn.megumi.bedwarshop.shop.gui;

import com.taylorswiftcn.megumi.bedwarshop.BedwarShop;
import com.taylorswiftcn.megumi.bedwarshop.file.Config;
import com.taylorswiftcn.megumi.bedwarshop.shop.constructor.Commodity;
import com.taylorswiftcn.megumi.bedwarshop.shop.constructor.QuickShop;
import com.taylorswiftcn.megumi.bedwarshop.util.WeiUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class EditGui {

    private BedwarShop plugin;
    private Inventory gui;
    private ItemStack glass;

    public EditGui(BedwarShop plugin) {
        this.plugin = plugin;
        this.glass = WeiUtil.createItem("160", 8, 1, " ", null, null, null);
    }

    public void open(Player p) {
        gui = Bukkit.createInventory(null, 54, Config.Edit_Title);
        addContent();
        addQuick(p);

        Inventory inventory = p.getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null) inventory.setItem(i, glass);
        }

        p.openInventory(gui);
    }

    private void addContent() {
        int[] glass = {9, 10, 11, 12, 13, 14, 15, 16, 17, 45, 46, 47, 48, 49, 50, 51, 52, 53};
        for (int i : glass) gui.setItem(i, plugin.getShopManager().getGlass());

        for (int i = 0; i < 9; i++) gui.setItem(i, WeiUtil.createItem("166", 0, 1, null, null, null, null));

        gui.setItem(47, Config.getItem("EditInfo"));
        gui.setItem(49, Config.getItem("Save"));
    }

    private void addQuick(Player p) {
        QuickShop quickShop = plugin.getShopManager().getQuickShop().get(p.getUniqueId());

        HashMap<String, Commodity> commodity = plugin.getShopManager().getCommodity();
        List<String> shop = quickShop.getShop();

        for (int i = 0; i < 27; i++) {
            if (shop.size() >= i + 1) {
                String s = shop.get(i);
                if (s == null) continue;
                if (!commodity.containsKey(s)) continue;
                ItemStack item = commodity.get(s).getItem();

                int index = i + 18;
                gui.setItem(index, item);
            }
        }
    }

    public void clearGlass(Player p) {
        Inventory inventory = p.getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null || item.getItemMeta() == null) continue;
            if (!item.equals(glass)) continue;
            inventory.setItem(i, new ItemStack(Material.AIR));
        }
    }

    public ItemStack getGlass() {
        return glass;
    }

}