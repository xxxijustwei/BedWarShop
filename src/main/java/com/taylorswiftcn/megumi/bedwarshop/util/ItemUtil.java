package com.taylorswiftcn.megumi.bedwarshop.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemUtil {

    public static boolean isFull(Player player) {
        int i = 0;
        for (ItemStack item : player.getInventory().getStorageContents()) {
            if (item == null || item.getItemMeta() == null) i++;
        }

        return i == 0;
    }

    public static boolean isArmor(ItemStack item) {
        return (isLeatherArmor(item)) || (isGoldArmor(item)) || (isIronArmor(item)) || (isDiamondArmor(item)) || (isChainmailArmor(item));
    }

    public static boolean isLeatherArmor(ItemStack item) {
        switch (item.getType()) {
            case LEATHER_BOOTS:
            case LEATHER_CHESTPLATE:
            case LEATHER_HELMET:
            case LEATHER_LEGGINGS:
                return true;
        }
        return false;
    }

    public static boolean isGoldArmor(ItemStack item) {
        switch (item.getType()) {
            case GOLD_BOOTS:
            case GOLD_CHESTPLATE:
            case GOLD_HELMET:
            case GOLD_LEGGINGS:
                return true;
        }
        return false;
    }

    public static boolean isIronArmor(ItemStack item) {
        switch (item.getType()) {
            case IRON_BOOTS:
            case IRON_CHESTPLATE:
            case IRON_HELMET:
            case IRON_LEGGINGS:
                return true;
        }
        return false;
    }

    public static boolean isDiamondArmor(ItemStack item) {
        switch (item.getType()) {
            case DIAMOND_BOOTS:
            case DIAMOND_CHESTPLATE:
            case DIAMOND_HELMET:
            case DIAMOND_LEGGINGS:
                return true;
        }
        return false;
    }

    public static boolean isChainmailArmor(ItemStack item) {
        switch (item.getType()) {
            case CHAINMAIL_BOOTS:
            case CHAINMAIL_CHESTPLATE:
            case CHAINMAIL_HELMET:
            case CHAINMAIL_LEGGINGS:
                return true;
        }
        return false;
    }

    public static boolean isBoots(ItemStack item) {
        switch (item.getType()) {
            case LEATHER_BOOTS:
            case CHAINMAIL_BOOTS:
            case DIAMOND_BOOTS:
            case GOLD_BOOTS:
            case IRON_BOOTS:
                return true;
        }
        return false;
    }

    public static boolean isChestplate(ItemStack item) {
        switch (item.getType()) {
            case LEATHER_CHESTPLATE:
            case CHAINMAIL_CHESTPLATE:
            case DIAMOND_CHESTPLATE:
            case GOLD_CHESTPLATE:
            case IRON_CHESTPLATE:
                return true;
        }
        return false;
    }

    public static boolean isHelmet(ItemStack item) {
        switch (item.getType()) {
            case LEATHER_HELMET:
            case CHAINMAIL_HELMET:
            case DIAMOND_HELMET:
            case GOLD_HELMET:
            case IRON_HELMET:
                return true;
        }
        return false;
    }

    public static boolean isLeggings(ItemStack item) {
        switch (item.getType()) {
            case LEATHER_LEGGINGS:
            case CHAINMAIL_LEGGINGS:
            case DIAMOND_LEGGINGS:
            case GOLD_LEGGINGS:
            case IRON_LEGGINGS:
                return true;
        }
        return false;
    }
}
