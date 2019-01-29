package com.taylorswiftcn.megumi.bedwarshop.database.mysql;

import com.taylorswiftcn.megumi.bedwarshop.BedwarShop;
import com.taylorswiftcn.megumi.bedwarshop.file.Config;

import java.util.LinkedList;

public class MysqlPool {
    private BedwarShop plugin;
    private LinkedList<MysqlHandler> pools;
    private int min;
    private int max;

    public MysqlPool(BedwarShop plugin) {
        this.plugin = plugin;
        this.pools = new LinkedList<>();
        this.min = 1;
        this.max = 5;
    }

    public void init() {
        pools.clear();
        for (int i = min; i <= max; i++) {
            pools.add(new MysqlHandler(Config.SQL_Host, Config.SQL_Port, Config.SQL_Database, Config.SQL_Users, Config.SQL_Password));
        }
    }

    public MysqlHandler getHandler() {
        if (pools.size() == 0) return new MysqlHandler(Config.SQL_Host, Config.SQL_Port, Config.SQL_Database, Config.SQL_Users, Config.SQL_Password);
        return pools.remove(0);
    }

    public void recover(MysqlHandler handler) {
        if (pools.size() >= max) return;
        pools.add(handler);
    }
}
