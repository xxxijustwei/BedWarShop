package com.taylorswiftcn.megumi.bedwarshop.shop.gui;

import com.taylorswiftcn.megumi.bedwarshop.BedwarShop;
import com.taylorswiftcn.megumi.bedwarshop.file.Config;
import com.taylorswiftcn.megumi.bedwarshop.file.Message;
import com.taylorswiftcn.megumi.bedwarshop.shop.constructor.Commodity;
import com.taylorswiftcn.megumi.bedwarshop.shop.constructor.MegumiShop;
import com.taylorswiftcn.megumi.bedwarshop.shop.constructor.QuickShop;
import com.taylorswiftcn.megumi.bedwarshop.util.ItemUtil;
import io.github.bedwarsrel.BedwarsRel;
import io.github.bedwarsrel.game.Game;
import io.github.bedwarsrel.game.Team;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ShopHandler {

    private static BedwarShop plugin = BedwarShop.getInstance();

    public static void page(Player p, int page) {
        Inventory gui = p.getOpenInventory().getTopInventory();

        List<MegumiShop> shops = plugin.getShopManager().getShops();

        gui.setItem(45, plugin.getShopManager().getGlass());
        gui.setItem(53, plugin.getShopManager().getGlass());
        if (page > 1) gui.setItem(45, Config.getItem("UpPage"));
        if (shops.size() > page * 9) gui.setItem(53, Config.getItem("NextPage"));

        shops = getPageShop(p, page);

        for (int i = 0; i < 9; i++) {
            if (shops.size() >= i + 1) {
                gui.setItem(i, shops.get(i).getItem());
                continue;
            }
            gui.setItem(i, new ItemStack(Material.AIR));
        }

        plugin.getShopManager().getPage().put(p.getUniqueId(), page);

        p.updateInventory();
    }

    public static void shop(Player p, MegumiShop megumiShop) {
        Inventory gui = p.getOpenInventory().getTopInventory();

        gui.setItem(49, Config.getItem("Quick"));
        int page = plugin.getShopManager().getPage().get(p.getUniqueId());
        List<MegumiShop> shops = getPageShop(p, page);
        for (int i = 0; i < shops.size(); i++) {
            ItemStack item = shops.get(i).getItem();
            if (item.equals(megumiShop.getItem())) {
                gui.setItem(i, addEnchants(item));
                continue;
            }
            gui.setItem(i, item);
        }

        List<Commodity> commodities = megumiShop.getCommodities();
        for (int i = 0; i < 27; i++) {
            if (commodities.size() >= i + 1) {
                ItemStack item = commodities.get(i).getItem();
                gui.setItem(i + 18, item);
                continue;
            }
            gui.setItem(i + 18, new ItemStack(Material.AIR));
        }

        p.updateInventory();
    }

    public static void buy(Player p, ItemStack item, int amount) {
        Commodity commodity = getCommodity(item);
        if (commodity == null) return;
        buy(p, commodity, amount);
    }

    private static void buy(Player p, Commodity commodity, int amount) {
        if (ItemUtil.isFull(p)) {
            p.sendMessage(Config.Prefix + Message.FullInventory);
            return;
        }

        ItemStack item = commodity.getItem().clone();
        int price = commodity.getPrice();

        price = (amount / item.getAmount()) * price;

        if (p.getLevel() < price && amount == item.getAmount()) {
            p.sendMessage(Config.Prefix + Message.BuyFailure);
            return;
        }

        if (amount != item.getAmount()) {
            if (p.getLevel() < commodity.getPrice()) {
                p.sendMessage(Config.Prefix + Message.BuyFailure);
                return;
            }

            int max = p.getLevel() / commodity.getPrice();

            if (max > amount) max = amount;

            price = max * commodity.getPrice();
            amount = max * item.getAmount();
        }

        p.setLevel(p.getLevel() - price);

        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        lore = lore.subList(0, lore.size() - Config.Price_Lore.size());
        meta.setLore(lore);
        item.setItemMeta(meta);

        divLeather(p, item);

        if (ItemUtil.isArmor(item)) {
            if (ItemUtil.isBoots(item)) equipSlot(p, item, 36);
            if (ItemUtil.isLeggings(item)) equipSlot(p, item, 37);
            if (ItemUtil.isChestplate(item)) equipSlot(p, item, 38);
            if (ItemUtil.isHelmet(item)) equipSlot(p, item, 39);
        }
        else {
            p.getInventory().addItem(item);
        }

        updateTitle(p);

        p.sendMessage(Config.Prefix + Message.BuySuccess);
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 10f);
    }

    private static void divLeather(Player p, ItemStack item) {
        Material material = item.getType();
        if (!(material == Material.LEATHER_BOOTS || material == Material.LEATHER_CHESTPLATE || material == Material.LEATHER_HELMET || material == Material.LEATHER_LEGGINGS)) return;

        Game game = BedwarsRel.getInstance().getGameManager().getGameOfPlayer(p);
        if (game == null) return;
        Team team = game.getPlayerTeam(p);
        if (team == null) return;

        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(team.getColor().getColor());

        item.setItemMeta(meta);
    }

    private static void equipSlot(Player player, ItemStack item, int slot) {
        ItemStack slotItem = player.getInventory().getItem(slot);
        if (slotItem == null || slotItem.getItemMeta() == null) {
            player.getInventory().setItem(slot, item);
        }
        else {
            player.getInventory().addItem(slotItem);
            player.getInventory().setItem(slot, item);
        }

        player.sendMessage(Config.Prefix + Message.AutoEquip);
    }

    private static void updateTitle(Player p) {
        Inventory gui = p.getOpenInventory().getTopInventory();
        Inventory inventory = Bukkit.createInventory(null, 54, Config.Shop_Title + Config.Exp_Suffix.replace("%exp%", String.valueOf(p.getLevel())));
        inventory.setContents(gui.getContents());

        p.openInventory(inventory);
    }

    public static void addQuick(Player p, ItemStack item) {
        Commodity commodity = getCommodity(item);
        if (commodity == null) return;

        QuickShop quickShop = plugin.getShopManager().getQuickShop().get(p.getUniqueId());
        List<String> list = new ArrayList<>();

        if (quickShop != null) {
            list = quickShop.getShop();
        }
        else quickShop = new QuickShop(list);

        int i = 0;
        for (String s : list) {
            if (s != null) i++;
        }
        if (i > 27) {
            p.sendMessage(Config.Prefix + Message.AddQuickFailure);
            return;
        }

        if (list.contains(commodity.getName())) return;

        if (list.size() >= 27 && i < 27) {
            int index = list.indexOf(null);
            list.set(index, commodity.getName());
        }
        else list.add(commodity.getName());

        quickShop.setShop(list);
        quickShop.setChange(true);

        plugin.getShopManager().getQuickShop().put(p.getUniqueId(), quickShop);
        p.sendMessage(Config.Prefix + Message.AddQuickSuccess);
    }

    public static void removeQuick(Player p, ItemStack item) {
        Commodity commodity = getCommodity(item);
        if (commodity == null) return;

        QuickShop quickShop = plugin.getShopManager().getQuickShop().get(p.getUniqueId());
        List<String> shop = quickShop.getShop();
        int index = shop.indexOf(commodity.getName());
        shop.set(index, null);

        quickShop.setShop(shop);
        quickShop.setChange(true);
        plugin.getShopManager().getQuickShop().put(p.getUniqueId(), quickShop);

        p.sendMessage(Config.Prefix + Message.DelQuickSuccess);
    }

    public static void saveQuick(Player p, Inventory gui) {
        List<String> shop = new ArrayList<>();

        for (int i = 18; i < 45; i++) {
            ItemStack item = gui.getItem(i);
            if (item == null || item.getItemMeta() == null) {
                shop.add(null);
                continue;
            }
            Commodity commodity = getCommodity(item);
            if (commodity == null) {
                shop.add(null);
                continue;
            }
            shop.add(commodity.getName());
        }

        QuickShop quickShop = new QuickShop(shop);
        quickShop.setChange(true);

        plugin.getShopManager().getQuickShop().put(p.getUniqueId(), quickShop);
    }

    public static ItemStack addEnchants(ItemStack itemStack) {
        ItemStack item = new ItemStack(itemStack);

        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);

        return item;
    }

    public static List<MegumiShop> getPlayerShop(Player p) {
        List<MegumiShop> shops = plugin.getShopManager().getShops();
        List<MegumiShop> list = new ArrayList<>();

        for (MegumiShop shop : shops) {
            if (p.hasPermission(shop.getPermission())) {
                list.add(shop);
            }
        }
        return list;
    }

    private static List<MegumiShop> getPageShop(Player p, int page) {
        List<MegumiShop> shops = getPlayerShop(p);

        int from = (page - 1) * 9;
        int to =  page * 9;
        if (shops.size() >= page * 9) {
            shops = new ArrayList<>(shops.subList(from, to));
        }
        else shops = new ArrayList<>(shops.subList(from, shops.size()));

        return shops;
    }

    private static Commodity getCommodity(ItemStack item) {
        List<Commodity> commodities = new ArrayList<>(plugin.getShopManager().getCommodity().values());

        for (Commodity commodity : commodities) {
            ItemStack itemStack = commodity.getItem();
            if (item.equals(itemStack)) return commodity;
        }
        return null;
    }

    public static void updateInventory(Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                p.updateInventory();
            }
        }.runTaskLater(plugin, 5);
    }
}
