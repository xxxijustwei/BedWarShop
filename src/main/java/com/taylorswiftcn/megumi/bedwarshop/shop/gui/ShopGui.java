package com.taylorswiftcn.megumi.bedwarshop.shop.gui;

import com.taylorswiftcn.megumi.bedwarshop.BedwarShop;
import com.taylorswiftcn.megumi.bedwarshop.file.Config;
import com.taylorswiftcn.megumi.bedwarshop.shop.constructor.Commodity;
import com.taylorswiftcn.megumi.bedwarshop.shop.constructor.MegumiShop;
import com.taylorswiftcn.megumi.bedwarshop.shop.constructor.QuickShop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class ShopGui {
    private BedwarShop plugin;
    private Inventory gui;

    public ShopGui(BedwarShop plugin) {
        this.plugin = plugin;
    }

    public void open(Player p) {
        gui = Bukkit.createInventory(null, 54, Config.Shop_Title + Config.Exp_Suffix.replace("%exp%", String.valueOf(p.getLevel())));
        addContent(p);
        addQuick(p);
        gui.setItem(49, ShopHandler.addEnchants(Config.getItem("Quick")));
        p.openInventory(gui);

        plugin.getShopManager().getPage().put(p.getUniqueId(), 1);
    }

    private void addContent(Player p) {
        int[] glass = {9, 10, 11, 12, 13, 14, 15, 16, 17, 45, 46, 47, 48, 49, 50, 51, 52, 53};
        for (int i : glass) gui.setItem(i, plugin.getShopManager().getGlass());

        gui.setItem(47, Config.getItem("Info"));
        gui.setItem(49, Config.getItem("Quick"));
        gui.setItem(51, Config.getItem("Edit"));

        List<MegumiShop> shops = ShopHandler.getPlayerShop(p);
        if (shops.size() != 0) {
            for (int i = 0; i < 9; i++) {
                if (shops.size() >= i + 1) {
                    MegumiShop shop = shops.get(i);
                    gui.setItem(i, shop.getItem());
                }
            }
        }

        if (shops.size() > 9) gui.setItem(53, Config.getItem("NextPage"));
    }

    private void addQuick(Player p) {
        QuickShop quick = plugin.getShopManager().getQuickShop().get(p.getUniqueId());

        if (quick != null) {

            HashMap<String, Commodity> commodity = plugin.getShopManager().getCommodity();
            List<String> shop = quick.getShop();

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
    }

}
