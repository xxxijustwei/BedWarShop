package com.taylorswiftcn.megumi.bedwarshop.commands.sub;

import com.taylorswiftcn.megumi.bedwarshop.BedwarShop;
import com.taylorswiftcn.megumi.bedwarshop.commands.WeiCommand;
import com.taylorswiftcn.megumi.bedwarshop.file.Message;
import org.bukkit.command.CommandSender;

public class HelpCommand extends WeiCommand {
    private BedwarShop plugin = BedwarShop.getInstance();

    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        Message.Help.forEach(CommandSender::sendMessage);
        if (CommandSender.hasPermission("bedwarshop.admin"))
            Message.AdminHelp.forEach(CommandSender::sendMessage);
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return null;
    }
}
