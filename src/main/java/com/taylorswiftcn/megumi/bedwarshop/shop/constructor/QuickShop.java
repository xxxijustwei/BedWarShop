package com.taylorswiftcn.megumi.bedwarshop.shop.constructor;

import lombok.Data;

import java.util.List;

@Data
public class QuickShop {
    private List<String> shop;
    private boolean change;

    public QuickShop(List<String> shop) {
        this.shop = shop;
        this.change = false;
    }
}
