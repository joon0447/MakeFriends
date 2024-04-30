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



    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            dataManager.createPlayerFile(player.getUniqueId());
            if(args.length == 0){
                new FriendsMenu().FriendsMainMenu(player); //친구 메인 메뉴
            }
            else if(args.length == 1){
                if(args[0].equals("선물종료")){
                    if(MakeFriends.giftMap.containsKey(player)){
                        MakeFriends.giftMap.remove(player);
                        player.sendMessage(MakeFriends.prefix + "선물 보내기 모드가 종료되었습니다.");
                    }else{
                        player.sendMessage(MakeFriends.prefix + "선물 보내기 모드가 아닙니다!");
                    }
                }else {
                    player.sendMessage(MakeFriends.prefix + "친구 기능을 사용하려면 " +
                            ChatColor.GREEN + "/친구 " + ChatColor.RESET + "를 입력해주세요.");
                }
            }
            else if(args.length == 2) {
                if (args[0].equals("초대")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        player.sendMessage(MakeFriends.prefix + "해당 유저가 존재하지 않습니다!");
                        return false;
                    } else {
                        target.sendMessage(MakeFriends.prefix + "친구 요청이 왔습니다. 수신함을 확인해주세요.");
                    }
                } else {
                    player.sendMessage(MakeFriends.prefix + "친구 기능을 사용하려면 " +
                            ChatColor.GREEN + "/친구 " + ChatColor.RESET + "를 입력해주세요.");
                }
            } else {
                player.sendMessage(MakeFriends.prefix + "친구 기능을 사용하려면 " +
                        ChatColor.GREEN + "/친구 " + ChatColor.RESET + "를 입력해주세요.");
            }
        }
        return false;
    }
}
