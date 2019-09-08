package com.taylorswiftcn.megumi.bedwarshop.shop.constructor;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
public class MegumiShop {

    private String name;
    private ItemStack item;
    private String permission;
    private List<Commodity> commodities;

    public MegumiShop(String name, ItemStack item, String permission, List<Commodity> commodities) {
        this.name = name;
        this.item = item;
        this.permission = permission;
        this.commodities = commodities;
    }

}
