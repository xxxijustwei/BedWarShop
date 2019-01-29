package com.taylorswiftcn.megumi.bedwarshop.shop.constructor;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

@Data
public class Commodity {

    private String name;
    private ItemStack item;
    private Integer price;

    public Commodity(String name, ItemStack item, Integer price) {
        this.name = name;
        this.item = item;
        this.price = price;
    }
}
