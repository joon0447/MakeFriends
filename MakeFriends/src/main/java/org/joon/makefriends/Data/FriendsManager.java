package org.joon.makefriends.Data;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.joon.makefriends.MakeFriends;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class FriendsManager {

    public List<String> loadFriends(Player player){
        UUID uuid = player.getUniqueId();
        File file = new File(MakeFriends.getInstance().getDataFolder(), "PlayerList/" + uuid + ".yml");
        YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);
        return yc.getStringList("friends");
    }

    public List<String> loadFriends(String s){
        File file = new File(MakeFriends.getInstance().getDataFolder(), "PlayerList/" + s + ".yml");
        YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);
        return yc.getStringList("friends");
    }
}
