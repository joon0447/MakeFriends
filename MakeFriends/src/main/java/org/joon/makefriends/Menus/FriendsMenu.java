package org.joon.makefriends.Menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class FriendsMenu {

    // 친구 메인 메뉴 - 친구 목록, 친구 추가, 친구 삭제
    public static void FriendsMainMenu(Player player){
        Inventory inv = Bukkit.createInventory(player, 9, ChatColor.BLUE.toString() + "친구 메뉴");

        //친구 목록
        ItemStack friendsList = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta friendsListMeta = friendsList.getItemMeta();
        friendsListMeta.setDisplayName(ChatColor.GREEN + "친구 목록");
        friendsListMeta.setLore(Arrays.asList(ChatColor.BLUE.toString() + "친구 목록을 확인합니다."));
        friendsList.setItemMeta(friendsListMeta);
        inv.setItem(2, friendsList);

        //친구 추가
        ItemStack addFriends = new ItemStack(Material.GREEN_BANNER);
        ItemMeta addFriendsMeta = addFriends.getItemMeta();
        addFriendsMeta.setDisplayName(ChatColor.GREEN + "친구 추가");
        addFriendsMeta.setLore(Arrays.asList(ChatColor.BLUE.toString() + "친구 추가 요청을 보냅니다."));
        addFriends.setItemMeta(addFriendsMeta);
        inv.setItem(4, addFriends);

        //친구 삭제
        ItemStack removeFriends = new ItemStack(Material.RED_BANNER);
        ItemMeta removeFriendsMeta = removeFriends.getItemMeta();
        removeFriendsMeta.setDisplayName(ChatColor.RED + "친구 삭제");
        removeFriendsMeta.setLore(Arrays.asList(ChatColor.BLUE.toString() + "친구를 삭제합니다."));
        removeFriends.setItemMeta(removeFriendsMeta);
        inv.setItem(6, removeFriends);

        //인벤토리 열기
        player.openInventory(inv);
    }
}
