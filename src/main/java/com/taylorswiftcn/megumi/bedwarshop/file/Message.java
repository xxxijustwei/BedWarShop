package com.taylorswiftcn.megumi.bedwarshop.file;

import com.taylorswiftcn.megumi.bedwarshop.BedwarShop;
import com.taylorswiftcn.megumi.bedwarshop.util.WeiUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class Message {
    private static YamlConfiguration message;
    public static String NoPermission;
    public static String BuyFailure;
    public static String BuySuccess;
    public static String AddQuickFailure;
    public static String AddQuickSuccess;
    public static String DelQuickSuccess;
    public static String OpenFailure;
    public static String SaveSuccess;
    public static String AddExp;
    public static String TakeExp;
    public static String SetExp;
    public static String KillObtianExp;
    public static String FullInventory;
    public static String AutoEquip;

    public static String GainExp;

    public static List<String> Help;
    public static List<String> AdminHelp;

    public static void init() {
        message = BedwarShop.getInstance().getFileManager().getMessage();
        NoPermission = getString("Message.NoPermission");
        BuyFailure = getString("Message.BuyFailure");
        BuySuccess = getString("Message.BuySuccess");
        AddQuickFailure = getString("Message.AddQuickFailure");
        AddQuickSuccess = getString("Message.AddQuickSuccess");
        DelQuickSuccess = getString("Message.DelQuickSuccess");
        OpenFailure = getString("Message.OpenFailure");
        SaveSuccess = getString("Message.SaveSuccess");
        AddExp = getString("Message.AddExp");
        TakeExp = getString("Message.TakeExp");
        SetExp = getString("Message.SetExp");
        KillObtianExp = getString("Message.KillObtainExp");
        FullInventory = getString("Message.FullInventory");
        AutoEquip = getString("Message.AutoEquip");

        GainExp = getString("ActionBar.GainExp");

        Help = getStringList("Help");
        AdminHelp = getStringList("AdminHelp");
    }

    private static String getString(String path) {
        return WeiUtil.onReplace(message.getString(path));
    }

    private static List<String> getStringList(String path) {
        return WeiUtil.onReplace(message.getStringList(path));
    }


}
