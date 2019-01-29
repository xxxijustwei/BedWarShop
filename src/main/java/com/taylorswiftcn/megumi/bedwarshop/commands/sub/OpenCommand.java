package com.taylorswiftcn.megumi.bedwarshop.commands.sub;

import com.taylorswiftcn.megumi.bedwarshop.BedwarShop;
import com.taylorswiftcn.megumi.bedwarshop.commands.WeiCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenCommand extends WeiCommand {

    private BedwarShop plugin = BedwarShop.getInstance();

    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        Player p = getPlayer();
        plugin.getShopManager().getShopGui().open(p);
    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String getPermission() {
        return "bedwarshop.use";
    }
}
