package org.joon.makefriends;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.joon.makefriends.Commands.FriendsCommand;
import org.joon.makefriends.Data.DataManager;
import org.joon.makefriends.Listeners.FriendsMenuListener;

public final class MakeFriends extends JavaPlugin {

    private final DataManager dataManager = new DataManager();

    @Override
    public void onEnable() {
        getCommand("친구").setExecutor(new FriendsCommand());

        Bukkit.getPluginManager().registerEvents(new FriendsMenuListener(), this);

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
