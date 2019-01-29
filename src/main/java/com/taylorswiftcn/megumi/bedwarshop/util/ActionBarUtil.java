package com.taylorswiftcn.megumi.bedwarshop.util;

import com.taylorswiftcn.megumi.bedwarshop.BedwarShop;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ActionBarUtil {

    private static BedwarShop plugin = BedwarShop.getInstance();

    private static String nmsVersion;
    public static void sendActionBar(Player p, String message){
        nmsVersion = plugin.getVersion();

        if (!p.isOnline()){
            return;
        }

        if (nmsVersion.startsWith("v1_12_")){
            sendActionBarPost112(p,message);
        }
        else{
            sendActionBarPre112(p,message);
        }
    }
    private static void sendActionBarPost112(Player player, String message)
    {
        if (!player.isOnline()) {
            return;
        }
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Object ppoc;
            Class<?> c4 = Class.forName("net.minecraft.server." + nmsVersion + ".PacketPlayOutChat");
            Class<?> c5 = Class.forName("net.minecraft.server." + nmsVersion + ".Packet");
            Class<?> c2 = Class.forName("net.minecraft.server." + nmsVersion + ".ChatComponentText");
            Class<?> c3 = Class.forName("net.minecraft.server." + nmsVersion + ".IChatBaseComponent");
            Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + nmsVersion + ".ChatMessageType");
            Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
            Object chatMessageType = null;
            for (Object obj : chatMessageTypes) {
                if (obj.toString().equals("GAME_INFO")) {
                    chatMessageType = obj;
                }
            }
            Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(message);
            ppoc = c4.getConstructor(new Class<?>[]{c3, chatMessageTypeClass}).newInstance(o, chatMessageType);
            Method m1 = craftPlayerClass.getDeclaredMethod("getHandle");
            Object h = m1.invoke(craftPlayer);
            Field f1 = h.getClass().getDeclaredField("playerConnection");
            Object pc = f1.get(h);
            Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
            m5.invoke(pc, ppoc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void sendActionBarPre112(Player player, String message)
    {
        if (!player.isOnline()) {
            return;
        }
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Object ppoc;
            Class<?> c4 = Class.forName("net.minecraft.server." + nmsVersion + ".PacketPlayOutChat");
            Class<?> c5 = Class.forName("net.minecraft.server." + nmsVersion + ".Packet");
            if (nmsVersion.equalsIgnoreCase("v1_8_R1") || nmsVersion.startsWith("v1_7_")) {
                Class<?> c2 = Class.forName("net.minecraft.server." + nmsVersion + ".ChatSerializer");
                Class<?> c3 = Class.forName("net.minecraft.server." + nmsVersion + ".IChatBaseComponent");
                Method m3 = c2.getDeclaredMethod("a", String.class);
                Object cbc = c3.cast(m3.invoke(c2, "{\"text\": \"" + message + "\"}"));
                ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(cbc, (byte) 2);
            } else {
                Class<?> c2 = Class.forName("net.minecraft.server." + nmsVersion + ".ChatComponentText");
                Class<?> c3 = Class.forName("net.minecraft.server." + nmsVersion + ".IChatBaseComponent");
                Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(message);
                ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(o, (byte) 2);
            }
            Method m1 = craftPlayerClass.getDeclaredMethod("getHandle");
            Object h = m1.invoke(craftPlayer);
            Field f1 = h.getClass().getDeclaredField("playerConnection");
            Object pc = f1.get(h);
            Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
            m5.invoke(pc, ppoc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
