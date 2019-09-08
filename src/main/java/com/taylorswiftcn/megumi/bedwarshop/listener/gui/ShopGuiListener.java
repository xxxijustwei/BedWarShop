package com.taylorswiftcn.megumi.bedwarshop.listener.gui;

import com.taylorswiftcn.megumi.bedwarshop.BedwarShop;
import com.taylorswiftcn.megumi.bedwarshop.file.Config;
import com.taylorswiftcn.megumi.bedwarshop.file.Message;
import com.taylorswiftcn.megumi.bedwarshop.shop.constructor.MegumiShop;
import com.taylorswiftcn.megumi.bedwarshop.shop.constructor.QuickShop;
import com.taylorswiftcn.megumi.bedwarshop.shop.gui.ShopHandler;
import io.github.bedwarsrel.BedwarsRel;
import io.github.bedwarsrel.game.Game;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopGuiListener implements Listener {

    private BedwarShop plugin = BedwarShop.getInstance();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();
        Inventory gui = e.getInventory();
        if (!gui.getTitle().contains(Config.Shop_Title)) return;

        if (e.getClick() == ClickType.NUMBER_KEY) {
            e.setCancelled(true);
            return;
        }

        ItemStack item = e.getCurrentItem();
        if (item == null || item.getItemMeta() == null) return;

        e.setCancelled(true);
        checkItem(e, item);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) return;
        Inventory gui = e.getInventory();
        if (!gui.getTitle().contains(Config.Shop_Title)) return;

        Player p = (Player) e.getPlayer();

        ShopHandler.updateInventory(p);
    }

    private void checkItem(InventoryClickEvent e, ItemStack item) {
        Player p = (Player) e.getWhoClicked();
        int rawSlot = e.getRawSlot();

        if (hasEnchants(item) || item.equals(Config.getItem("Info")) || item.equals(plugin.getShopManager().getGlass())) {
            return;
        }

        if (rawSlot >=0 && rawSlot < 9) {
            for (MegumiShop shop : plugin.getShopManager().getShops()) {
                ItemStack shopItem = shop.getItem();

                if (item.equals(shopItem)) {
                    ShopHandler.shop(p, shop);
                    return;
                }
            }
        }

        if (rawSlot >= 18 && rawSlot < 45) {
            if (!p.isOp()) {
                Game game = BedwarsRel.getInstance().getGameManager().getGameOfPlayer(p);
                if (game == null) return;
            }

            ClickType type = e.getClick();

            if (type == ClickType.LEFT) {
                ShopHandler.buy(p, item, item.getAmount());
                return;
            }

            if (type == ClickType.RIGHT) {
                ShopHandler.buy(p, item, item.getAmount() * 10);
                return;
            }

            if (type == ClickType.SHIFT_LEFT) {
                if (item.getAmount() > 64) return;
                ShopHandler.buy(p, item, 64);
                return;
            }

            if (type == ClickType.SHIFT_RIGHT) {
                ShopHandler.addQuick(p, item);
            }
            return;
        }

        if (item.equals(Config.getItem("Quick"))) {
            plugin.getShopManager().getShopGui().open(p);
            return;
        }

        if (item.equals(Config.getItem("NextPage"))) {
            int page = plugin.getShopManager().getPage().get(p.getUniqueId());
            ShopHandler.page(p, page + 1);
            return;
        }

        if (item.equals(Config.getItem("UpPage"))) {
            int page = plugin.getShopManager().getPage().get(p.getUniqueId());
            ShopHandler.page(p, page - 1);
            return;
        }

        if (item.equals(Config.getItem("Edit"))) {
            QuickShop shop = plugin.getShopManager().getQuickShop().get(p.getUniqueId());
            if (shop == null || shop.getShop().size() == 0) {
                p.sendMessage(Config.Prefix + Message.OpenFailure);
                return;
            }
            plugin.getShopManager().getEditGui().open(p);
        }
    }

    private boolean hasEnchants(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta.getEnchants().containsKey(Enchantment.LUCK)) return true;
        else return false;
    }
}
