package com.taylorswiftcn.megumi.bedwarshop.shop;

import com.taylorswiftcn.megumi.bedwarshop.BedwarShop;
import com.taylorswiftcn.megumi.bedwarshop.database.SQLManager;
import com.taylorswiftcn.megumi.bedwarshop.file.Config;
import com.taylorswiftcn.megumi.bedwarshop.shop.constructor.Commodity;
import com.taylorswiftcn.megumi.bedwarshop.shop.constructor.MegumiShop;
import com.taylorswiftcn.megumi.bedwarshop.shop.constructor.PotionValue;
import com.taylorswiftcn.megumi.bedwarshop.shop.constructor.QuickShop;
import com.taylorswiftcn.megumi.bedwarshop.shop.gui.EditGui;
import com.taylorswiftcn.megumi.bedwarshop.shop.gui.ShopGui;
import com.taylorswiftcn.megumi.bedwarshop.util.WeiUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Getter
public class ShopManager {
    private BedwarShop plugin;
    private ItemStack glass;
    private List<MegumiShop> shops;
    private HashMap<String, Commodity> commodity;
    private HashMap<UUID, QuickShop> quickShop;
    private HashMap<UUID, Integer> page;
    private List<ItemStack> limitAddition;

    private ShopGui shopGui;
    private EditGui editGui;

    public ShopManager(BedwarShop plugin) {
        this.plugin = plugin;
        this.glass = WeiUtil.createItem("160", 7, 1, null, null, null, null);
        this.shops = new ArrayList<>();
        this.commodity = new HashMap<>();
        this.quickShop = new HashMap<>();
        this.page = new HashMap<>();
        this.limitAddition = new ArrayList<>();

        this.shopGui = new ShopGui(plugin);
        this.editGui = new EditGui(plugin);
    }

    public void reload() {
        shops.clear();
        commodity.clear();
        page.clear();

        plugin.closeInventory();

        init();
    }

    public void init() {
        YamlConfiguration shop = plugin.getFileManager().getShop();

        ConfigurationSection section = shop.getConfigurationSection("Shop");
        plugin.getLogger().info("========================");
        for (String s : section.getKeys(false)) {
            String id = section.get(s + ".ID").toString();
            Integer data = section.getInt(s + ".Data");
            String name = section.getString(s + ".Name");
            List<String> lore = section.getStringList(s + ".Lore");
            String permission = section.getString(s + ".Permission");
            List<Commodity> commodities = getCommodities(section.getStringList(s + ".Commodity"));

            ItemStack item = WeiUtil.createItem(id, data, 1, name, lore, null, null);

            shops.add(new MegumiShop(s, item, permission, commodities));
            plugin.getLogger().info(String.format("Shop: 商店 %s 已加载 %s 件商品", s, commodities.size()));
        }
        plugin.getLogger().info("========================");

        for (String s : plugin.getFileManager().getConfig().getStringList("Config.LimitAddition")) {
            ItemStack item = commodity.get(s).getItem();

            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();
            lore = lore.subList(0, lore.size() - Config.Price_Lore.size());
            meta.setLore(lore);
            item.setItemMeta(meta);

            limitAddition.add(item);
        }
    }

    private List<Commodity> getCommodities(List<String> list) {
        YamlConfiguration item = plugin.getFileManager().getItem();
        List<Commodity> commodities = new ArrayList<>();

        for (String s : list) {
            if (!item.contains(s)) continue;

            String id = item.get(s + ".ID").toString();
            Integer data = item.getInt(s + ".Data");
            Integer amount = item.getInt(s + ".Amount");
            String name = item.getString(s + ".Name");
            List<String> lore = item.getStringList(s + ".Lore");
            HashMap<Enchantment, Integer> enchants = null;
            PotionValue potion = null;
            int price = item.getInt(s + ".Price");

            for (String priceLore : Config.Price_Lore) {
                lore.add(priceLore.replace("%price%", String.valueOf(price)));
            }

            if (item.contains(s + ".Enchants")) {
                enchants = new HashMap<>();
                ConfigurationSection section = item.getConfigurationSection(s + ".Enchants");

                for (String enchant : section.getKeys(false)) {
                    Enchantment enchantment = Enchantment.getByName(enchant);
                    if (enchantment == null) continue;
                    enchants.put(enchantment, section.getInt(enchant));
                }
            }

            if ((id.equals("373") || id.equalsIgnoreCase("POTION")) && item.contains(s + ".Custom-Effects")) {
                int effect = item.getInt(s + ".Custom-Effects.Effect");
                int duration = item.getInt(s + ".Custom-Effects.Duration");
                int amplifier = item.getInt(s + ".Custom-Effects.Amplifier");
                boolean ambient = item.getBoolean(s + ".Custom-Effects.Ambient");
                boolean hasParticles = item.getBoolean(s + ".Custom-Effects.Has-Particles");

                potion = new PotionValue(effect, duration, amplifier, ambient, hasParticles);
            }

            ItemStack itemStack = WeiUtil.createItem(id, data, amount, name, lore, enchants, potion);

            commodity.put(s, new Commodity(s, itemStack, price));
            commodities.add(new Commodity(s, itemStack, price));
        }
        return commodities;
    }

    public void loadPlayer(Player p) {
        p.setLevel(0);
        p.setExp(0);
        p.setTotalExperience(0);

        QuickShop quickShop = SQLManager.getPlayer(p);

        if (quickShop != null) {
            List<String> shop = quickShop.getShop();
            List<String> commodities = new ArrayList<>();
            for (String s : shop) {
                Commodity commodity = plugin.getShopManager().getCommodity().get(s);
                if (commodity == null) {
                    commodities.add(null);
                    continue;
                }
                if (hasPermission(p, commodity)) {
                    commodities.add(s);
                }
                else commodities.add(null);
            }
            quickShop.setShop(commodities);
        }

        plugin.getShopManager().getQuickShop().put(p.getUniqueId(), quickShop);
    }

    public void savePlayer(Player p) {
        p.setLevel(0);
        p.setExp(0);
        p.setTotalExperience(0);

        QuickShop shop = plugin.getShopManager().getQuickShop().get(p.getUniqueId());
        if (shop == null) return;
        if (!shop.isChange()) return;
        SQLManager.savePlayer(p);
    }

    private Boolean hasPermission(Player p, Commodity commodity) {
        for (MegumiShop megumiShop : plugin.getShopManager().getShops()) {
            List<Commodity> commodities = megumiShop.getCommodities();
            for (Commodity c : commodities) {
                if (c.equals(commodity)) {
                    return p.hasPermission(megumiShop.getPermission());
                }
            }
        }
        return false;
    }
}
