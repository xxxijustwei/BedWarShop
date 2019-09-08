package com.taylorswiftcn.megumi.bedwarshop.listener;

import com.taylorswiftcn.megumi.bedwarshop.BedwarShop;
import com.taylorswiftcn.megumi.bedwarshop.file.Config;
import io.github.bedwarsrel.events.BedwarsGameStartEvent;
import io.github.bedwarsrel.events.BedwarsOpenShopEvent;
import io.github.bedwarsrel.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;


public class BedwarGameListener implements Listener {

    private BedwarShop plugin = BedwarShop.getInstance();

    @EventHandler
    public void onGameStart(BedwarsGameStartEvent e) {
        Game game = e.getGame();
        List<Player> players = game.getPlayers();

        for (Player p : players) {
            if (p.getOpenInventory() != null) {
                if (p.getOpenInventory().getTitle().equals(Config.Edit_Title)) {
                    p.closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onOpenShop(BedwarsOpenShopEvent e) {
        Player p = (Player) e.getPlayer();
        e.setCancelled(true);
        plugin.getShopManager().getShopGui().open(p);
    }
}
