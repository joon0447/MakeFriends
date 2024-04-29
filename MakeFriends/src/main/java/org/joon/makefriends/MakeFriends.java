package org.joon.makefriends;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.joon.makefriends.Commands.FriendsCommand;
import org.joon.makefriends.Data.DataManager;
import org.joon.makefriends.Listeners.FriendGiftListener;
import org.joon.makefriends.Listeners.FriendJoinListener;
import org.joon.makefriends.Listeners.FriendsMenuListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MakeFriends extends JavaPlugin {

    private final DataManager dataManager = new DataManager();
    public static Map<Player, Player> giftMap;
    @Override
    public void onEnable() {
        giftMap = new HashMap<>();
        getCommand("친구").setExecutor(new FriendsCommand());

        Bukkit.getPluginManager().registerEvents(new FriendsMenuListener(), this);
        Bukkit.getPluginManager().registerEvents(new FriendJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new FriendGiftListener(),this);

        dataManager.createFile();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MakeFriends getInstance() {
        return JavaPlugin.getPlugin(MakeFriends.class);
    }
}
