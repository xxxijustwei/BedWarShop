package com.taylorswiftcn.megumi.bedwarshop.listener;

import com.taylorswiftcn.megumi.bedwarshop.BedwarShop;
import com.taylorswiftcn.megumi.bedwarshop.file.Config;
import com.taylorswiftcn.megumi.bedwarshop.util.WeiUtil;
import io.github.bedwarsrel.BedwarsRel;
import io.github.bedwarsrel.game.Game;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ResourcePickupListener implements Listener {

    private BedwarShop plugin = BedwarShop.getInstance();

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onPickup(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();
        Game game = BedwarsRel.getInstance().getGameManager().getGameOfPlayer(p);

        if (game == null) return;

        if (game.isSpectator(p)) {
            e.setCancelled(true);
            return;
        }

        Item entityItem = e.getItem();
        ItemStack item = entityItem.getItemStack();

        int exp = getExp(item);
        if (exp == 0) return;

        boolean addition = true;

        if (plugin.getShopManager().getLimitAddition().contains(WeiUtil.getSignItem(item))) {
            addition = false;
        }

        Location location = entityItem.getLocation();
        Player player = getRandomPlayer(game, location);
        if (player == null) {
            e.setCancelled(true);
            return;
        }

        e.setCancelled(true);
        entityItem.remove();

        plugin.getExpManager().giveExp(player, exp, addition);
    }

    private Player getRandomPlayer(Game game, Location location) {
        List<Entity> entities = new ArrayList<>(location.getWorld().getNearbyEntities(location, 1,1,1));
        List<Player> players = new ArrayList<>();

        for (Entity entity : entities) {
            if (entity instanceof Player) {
                Player p = (Player) entity;
                if (game.isSpectator(p)) continue;
                players.add(p);
            }
        }

        if (players.size() <= 0) return null;

        Random random = new Random();
        int index = random.nextInt(players.size());

        return players.get(index);
    }

    private int getExp(ItemStack item) {
        for (Map.Entry<ItemStack, Integer> map : Config.Resource.entrySet()) {
            ItemStack key = map.getKey();
            int value = map.getValue();

            if (item.getType() == key.getType()) {
                return item.getAmount() * value;
            }
        }
        return 0;
    }
}
