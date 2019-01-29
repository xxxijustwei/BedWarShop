package com.taylorswiftcn.megumi.bedwarshop.commands.sub;

import com.taylorswiftcn.megumi.bedwarshop.commands.WeiCommand;
import com.taylorswiftcn.megumi.bedwarshop.file.Config;
import com.taylorswiftcn.megumi.bedwarshop.file.Message;
import com.taylorswiftcn.megumi.bedwarshop.util.WeiUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddCommand extends WeiCommand {
    @Override
    public void perform(CommandSender CommandSender, String[] Strings) {
        if (Strings.length != 3) return;
        String player = Strings[1];
        String value = Strings[2];

        if (Bukkit.getPlayerExact(player) == null) {
            CommandSender.sendMessage(Config.Prefix + "&c玩家不在线");
            return;
        }

        if (!WeiUtil.isNumber(value)) {
            CommandSender.sendMessage(Config.Prefix + "&c无效的经验值");
            return;
        }

        Player p = Bukkit.getPlayerExact(player);
        int exp = Integer.parseInt(value);

        p.setLevel(p.getLevel() + exp);

        p.sendMessage(Config.Prefix + Message.AddExp
                .replace("%player%", player)
                .replace("%count%", String.valueOf(exp))
                .replace("%exp%", String.valueOf(p.getLevel()))
        );
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
