package org.joon.makefriends.Commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.A;
import org.joon.makefriends.Data.DataManager;
import org.joon.makefriends.MakeFriends;
import org.joon.makefriends.Menus.FriendsMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FriendsCommand implements CommandExecutor {
    private final DataManager dataManager = new DataManager();
    private MakeFriends makeFriends;



    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            dataManager.createPlayerFile(player.getUniqueId());
            if(args.length == 0){
                new FriendsMenu().FriendsMainMenu(player); //친구 메인 메뉴
            }
            if(args.length == 2){
                if(args[0].equals("초대")){
                    Player target = Bukkit.getPlayer(args[1]);
                    if(target == null){
                        player.sendMessage("해당 유저가 존재하지 않습니다!");
                        return false;
                    }else{
                        TextComponent inviteMessage =
                                new net.md_5.bungee.api.chat.TextComponent("하이");
                        TextComponent agree = new TextComponent(ChatColor.GREEN + "[수락]");
                        TextComponent degree = new TextComponent(ChatColor.RED + "[거절]");
                        agree.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/친구 수락 " + player.getName()));
                        degree.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/친구 거절 " + player.getName()));
                        inviteMessage.addExtra(agree);
                        inviteMessage.addExtra(degree);
                        target.spigot().sendMessage(inviteMessage);
                    }
                }else if(args[0].equals("수락")){
                    Player target = Bukkit.getPlayer(args[1]);
                    player.sendMessage("친구 수락 테스트");
                    UUID targetUUID = target.getUniqueId();
                    UUID playerUUID = player.getUniqueId();
                    File targetFile = new File(MakeFriends.getInstance().getDataFolder(), "PlayerList/" + targetUUID + ".yml");
                    File playerFile = new File(MakeFriends.getInstance().getDataFolder(), "PlayerList/" + playerUUID + ".yml");
                    YamlConfiguration targetYML = YamlConfiguration.loadConfiguration(targetFile);
                    YamlConfiguration playerYML = YamlConfiguration.loadConfiguration(playerFile);
                    List<String> frindsList = new ArrayList<>();
                    ConfigurationSection targetFriends = targetYML.getConfigurationSection("friends");
                    ConfigurationSection playerFriends = playerYML.getConfigurationSection("friends");
                    for(String str : targetYML.getStringList("friends")){
                        target.sendMessage(str);
                    }
                    for(String str : playerYML.getStringList("friends")){
                        player.sendMessage(str);
                    }
                }
            }
        }
        return false;
    }
}
