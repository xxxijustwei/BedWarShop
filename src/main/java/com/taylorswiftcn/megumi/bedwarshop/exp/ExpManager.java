package com.taylorswiftcn.megumi.bedwarshop.exp;

import com.taylorswiftcn.megumi.bedwarshop.BedwarShop;
import com.taylorswiftcn.megumi.bedwarshop.file.Message;
import com.taylorswiftcn.megumi.bedwarshop.util.ActionBarUtil;
import com.taylorswiftcn.megumi.bedwarshop.util.WeiUtil;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class ExpManager {

    private BedwarShop plugin;

    @Getter private HashMap<Double, String> additionMap;
    @Getter private HashMap<UUID, Double> remaining;

    public ExpManager(BedwarShop plugin) {
        this.plugin = plugin;
        this.additionMap = new HashMap<>();
        this.remaining = new HashMap<>();
    }

    public void init() {
        ConfigurationSection section = plugin.getFileManager().getConfig().getConfigurationSection("Config.ExpAddition");
        for (String s : section.getKeys(false)) {

            double rate = section.getDouble(s + ".Rate");
            String permission = section.getString(s + ".Permission");

            WeiUtil.log(rate + " " + permission);

            additionMap.put(rate, permission);
        }
    }

    public void giveExp(Player p, int exp, boolean b) {
        if (!b) {
            addExp(p, exp, 0);
            return;
        }

        double addition = getAddition(p);

        if (addition == 0d) {
            addExp(p, exp, 0);
        }

        double finalExp = exp * addition;

        int obtainExp = (int) finalExp;
        double remainingExp = finalExp - obtainExp;

        if (remainingExp == 0d) {
            addExp(p, obtainExp, obtainExp - exp);
        }
        else {
            if (!remaining.containsKey(p.getUniqueId())) {
                remaining.put(p.getUniqueId(), remainingExp);
                addExp(p, obtainExp, obtainExp - exp);
            }
            else {
                double before = remaining.get(p.getUniqueId());
                remainingExp += before;

                int add = (int) remainingExp;
                double value = remainingExp - add;

                obtainExp += add;

                if (value == 0d) {
                    remaining.remove(p.getUniqueId());
                }
                else {
                    remaining.put(p.getUniqueId(), value);
                }

                addExp(p, obtainExp, obtainExp - exp);
            }
        }
    }

    private void addExp(Player p, int exp, int extra) {
        p.setLevel(p.getLevel() + exp);
        p.playSound(p.getLocation(), Sound.valueOf("ENTITY_EXPERIENCE_ORB_PICKUP"), 10F, 10F);
        ActionBarUtil.sendActionBar(p, Message.GainExp
                .replace("%exp%", String.valueOf(exp - extra))
                .replace("%addition%", String.valueOf(extra))
        );
    }

    private double getAddition(Player p) {
        List<Double> keys = new ArrayList<>(additionMap.keySet());

        Collections.sort(keys);
        Collections.reverse(keys);

        for (double d : keys) {
            String permission = additionMap.get(d);
            if (p.hasPermission(permission)) return d;
        }
        return 0;
    }
}
