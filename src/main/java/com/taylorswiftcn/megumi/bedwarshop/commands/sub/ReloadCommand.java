package com.taylorswiftcn.megumi.bedwarshop.commands.sub;

import com.taylorswiftcn.megumi.bedwarshop.BedwarShop;
import com.taylorswiftcn.megumi.bedwarshop.commands.WeiCommand;
import com.taylorswiftcn.megumi.bedwarshop.file.Config;
import com.taylorswiftcn.megumi.bedwarshop.file.Message;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends WeiCommand {

    private BedwarShop plugin = BedwarShop.getInstance();

    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        plugin.getFileManager().init();
        Config.init();
        Message.init();

        plugin.getShopManager().reload();

        CommandSender.sendMessage(Config.Prefix + "§a重载成功");
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return "bedwarshop.admin";
    }
}
