package org.joon.makefriends;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.joon.makefriends.Commands.FriendsCommand;
import org.joon.makefriends.Listeners.FriendsMainMenuListener;

public final class MakeFriends extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("친구").setExecutor(new FriendsCommand());

        Bukkit.getPluginManager().registerEvents(new FriendsMainMenuListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
