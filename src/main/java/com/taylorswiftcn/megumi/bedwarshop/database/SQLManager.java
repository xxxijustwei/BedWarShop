package com.taylorswiftcn.megumi.bedwarshop.database;

import com.taylorswiftcn.megumi.bedwarshop.BedwarShop;
import com.taylorswiftcn.megumi.bedwarshop.database.mysql.MysqlHandler;
import com.taylorswiftcn.megumi.bedwarshop.database.mysql.MysqlPool;
import com.taylorswiftcn.megumi.bedwarshop.shop.constructor.QuickShop;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class SQLManager {
    private static BedwarShop plugin = BedwarShop.getInstance();
    private static MysqlPool pool = new MysqlPool(plugin);

    public static void init() {
        pool.init();

        String quickShop = "CREATE TABLE IF NOT EXISTS quickshop (uuid VARCHAR(50) NOT NULL PRIMARY KEY, player VARCHAR(20) NOT NULL, quick TEXT NOT NULL) ENGINE = InnoDB";

        MysqlHandler sql = pool.getHandler();
        sql.openConnection();
        sql.updateSQL(quickShop);
        sql.closeConnection();
    }

    public static QuickShop getPlayer(Player p) {
        if (!existsPlayer(p)) return null;

        String select = String.format("SELECT * FROM quickshop WHERE uuid = '%s'", p.getUniqueId().toString());

        MysqlHandler sql = pool.getHandler();
        try {
            sql.openConnection();
            ResultSet set = sql.querySQL(select);
            if (set.next()) {
                String quick = set.getString("quick");
                if (quick == null || quick.equals("")) return null;
                JSONArray array = (JSONArray) JSONValue.parse(quick);
                List<String> list = new ArrayList<>();
                for (Object obj : array) {
                    if (obj == null) {
                        list.add(null);
                        continue;
                    }
                    list.add(obj.toString());
                }
                return new QuickShop(list);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            sql.closeConnection();
            pool.recover(sql);
        }
        return null;
    }

    public static void savePlayer(Player p) {
        QuickShop quickShop = plugin.getShopManager().getQuickShop().get(p.getUniqueId());
        if (quickShop == null) return;

        List<String> shop = quickShop.getShop();
        String quick = JSONValue.toJSONString(shop);

        String update = String.format("UPDATE quickshop SET quick = '%s' WHERE uuid = '%s'", quick, p.getUniqueId().toString());
        String insert = String.format("INSERT INTO quickshop VALUES ('%s', '%s', '%s')", p.getUniqueId().toString(), p.getName(), quick);

        MysqlHandler sql = pool.getHandler();
        if (existsPlayer(p)) {
            sql.openConnection();
            sql.updateSQL(update);
        }
        else {
            sql.openConnection();
            sql.updateSQL(insert);
        }
        sql.closeConnection();
        pool.recover(sql);
    }

    private static Boolean existsPlayer(Player p) {
        String select = String.format("SELECT * FROM quickshop WHERE uuid = '%s'", p.getUniqueId().toString());

        MysqlHandler sql = pool.getHandler();
        try {
            sql.openConnection();
            ResultSet set = sql.querySQL(select);
            return set.next();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            sql.openConnection();
            pool.recover(sql);
        }
        return false;
    }
}
