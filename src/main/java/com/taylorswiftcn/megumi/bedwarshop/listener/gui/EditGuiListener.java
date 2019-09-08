package com.taylorswiftcn.megumi.bedwarshop.listener.gui;

import com.taylorswiftcn.megumi.bedwarshop.BedwarShop;
import com.taylorswiftcn.megumi.bedwarshop.file.Config;
import com.taylorswiftcn.megumi.bedwarshop.file.Message;
import com.taylorswiftcn.megumi.bedwarshop.shop.gui.ShopHandler;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EditGuiListener implements Listener {

    private BedwarShop plugin = BedwarShop.getInstance();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Inventory gui = e.getInventory();
        if (!gui.getTitle().equals(Config.Edit_Title)) return;

        Player p = (Player) e.getWhoClicked();

        if (e.getClick() == ClickType.NUMBER_KEY) {
            e.setCancelled(true);
            return;
        }

        ItemStack item = e.getCurrentItem();
        if (item == null || item.getItemMeta() == null) return;

        int rawSlot = e.getRawSlot();
        if (rawSlot < 18 || rawSlot > 44) {
            e.setCancelled(true);
        }

        if (rawSlot >= 18 && rawSlot < 45) {
            if (e.getClick() == ClickType.RIGHT) {
                ShopHandler.removeQuick(p, item);
                gui.setItem(rawSlot, new ItemStack(Material.AIR));
                return;
            }
        }

        if (item.equals(Config.getItem("Save"))) {
            ShopHandler.saveQuick(p, gui);

            e.setCursor(new ItemStack(Material.AIR));

            p.closeInventory();
            ShopHandler.updateInventory(p);

            plugin.getShopManager().getShopGui().open(p);

            p.sendMessage(Config.Prefix + Message.SaveSuccess);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (player.getOpenInventory() == null) return;
        if (player.getOpenInventory().getTitle().equals(Config.Edit_Title)) {

            Item entity = e.getItemDrop();
            ItemStack item = entity.getItemStack();
            ShopHandler.removeQuick(player, item);
            e.getItemDrop().remove();

            ShopHandler.updateInventory(player);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) return;
        Inventory gui = e.getInventory();
        if (!gui.getTitle().equals(Config.Edit_Title)) return;

        Player p = (Player) e.getPlayer();

        plugin.getShopManager().getEditGui().clearGlass(p);

        ShopHandler.updateInventory(p);
    }
}
