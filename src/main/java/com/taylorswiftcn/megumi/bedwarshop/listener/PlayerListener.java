package com.taylorswiftcn.megumi.bedwarshop.listener;

import com.taylorswiftcn.megumi.bedwarshop.BedwarShop;
import com.taylorswiftcn.megumi.bedwarshop.file.Config;
import com.taylorswiftcn.megumi.bedwarshop.file.Message;
import com.taylorswiftcn.megumi.bedwarshop.util.WeiUtil;
import io.github.bedwarsrel.BedwarsRel;
import io.github.bedwarsrel.events.BedwarsPlayerKilledEvent;
import io.github.bedwarsrel.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;


public class PlayerListener implements Listener {

    private BedwarShop plugin = BedwarShop.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getShopManager().loadPlayer(p);
            }
        }.runTaskLater(plugin, 30);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        plugin.getShopManager().savePlayer(p);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();

        Game game = BedwarsRel.getInstance().getGameManager().getGameOfPlayer(p);

        if (game == null) return;

        e.setKeepLevel(true);
    }

    @EventHandler
    public void onKill(BedwarsPlayerKilledEvent e) {
        Player player = e.getPlayer();
        Player killer = e.getKiller();

        int playerLevel = player.getLevel();

        String deathLost = Config.DeathLost;
        int deathValue = getValue(deathLost);
        boolean deathRate = isRate(deathLost);

        if (deathValue == 0) return;

        if (deathRate) {
            if (deathValue >= 100) deathValue = 100;
            int level = (int) (playerLevel * (deathValue / 100d));
            player.setLevel(playerLevel - level);
        }
        else {
            if (deathValue >= playerLevel) player.setLevel(0);
            else player.setLevel(playerLevel - deathValue);
        }

        if (killer == null) return;
        String killObtain = Config.KillObtain;
        int killValue = getValue(killObtain);
        boolean killRate = isRate(killObtain);

        if (killValue == 0) return;

        if (killRate) {
            if (killValue >= 100) killValue = 100;
            killValue = (int) (playerLevel * (killValue / 100d));
            killer.setLevel(killer.getLevel() + killValue);
        }
        else {
            if (killValue >= playerLevel) killValue = playerLevel;
            killer.setLevel(killer.getLevel() + killValue);
        }

        killer.sendMessage(Config.Prefix + Message.KillObtianExp
                .replace("%player%", player.getName())
                .replace("%exp%", String.valueOf(killValue))
        );
    }

    private Integer getValue(String s) {
        if (s.endsWith("%")) {
            s = s.replace("%", "");

        }

        if (!WeiUtil.isNumber(s)) return 0;

        return Integer.parseInt(s);
    }

    private boolean isRate(String s) {
        return s.endsWith("%");
    }
}
