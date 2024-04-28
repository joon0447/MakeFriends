package org.joon.makefriends.Listeners;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.checkerframework.checker.units.qual.C;
import org.joon.makefriends.Menus.FriendsMenu;

import java.util.Objects;

public class FriendsMenuListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(ChatColor.translateAlternateColorCodes('&',
                e.getView().getTitle()).equals(ChatColor.BLUE + "친구 메뉴")
                && e.getCurrentItem()!=null){
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            switch (e.getRawSlot()){
                case 1: // 친구 목록
                    new FriendsMenu().friendsListMenu(player, 1);
                    break;
                case 3: // 친구 관리
                    player.closeInventory();
                    new FriendsMenu().friendsManagerMenu(player);
                    break;
                case 5: // 수신함
                    player.sendMessage("친구삭제");
                    break;
                case 7: // 선물함
                    player.sendMessage("선물함");
                    break;
                default:
                    break;
            }
        }

        else if(ChatColor.translateAlternateColorCodes('&',
                e.getView().getTitle()).equals(ChatColor.BLUE + "친구 관리")
                && e.getCurrentItem()!=null) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            switch (e.getRawSlot()) {
                case 3: // 친구 추가
                    new FriendsMenu().friendsAddMenu(player, 1);
                    break;
                case 5: // 친구 삭제
                    break;
                default:
                    break;
            }
        }

        else if(ChatColor.translateAlternateColorCodes('&',
                e.getView().getTitle()).equals(ChatColor.BLUE + "친구 초대")
                && e.getCurrentItem()!=null) {
            int page = Integer.parseInt(e.getInventory().getItem(48).getItemMeta().getLocalizedName());
            if (e.getRawSlot()==48 && e.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE)){
                new FriendsMenu().friendsAddMenu((Player) e.getWhoClicked(), page-1);
            }else if(e.getRawSlot() == 50 && e.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE)){
                new FriendsMenu().friendsAddMenu((Player) e.getWhoClicked(), page+1);
            }
            e.setCancelled(true);
            Player target = Bukkit.getPlayer(e.getCurrentItem().getItemMeta().getDisplayName().toString());
            Player player = (Player) e.getWhoClicked();
            TextComponent inviteMessage = new net.md_5.bungee.api.chat.TextComponent("하이");
            TextComponent agree = new TextComponent(ChatColor.GREEN + "[수락]");
            TextComponent degree = new TextComponent(ChatColor.RED + "[거절]");
            agree.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/친구 수락 " + player.getName()));
            degree.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/친구 거절 " + player.getName()));
            inviteMessage.addExtra(agree);
            inviteMessage.addExtra(degree);
            target.spigot().sendMessage(inviteMessage);
        }

        else if(ChatColor.translateAlternateColorCodes('&',
                e.getView().getTitle()).equals(ChatColor.BLUE + "친구 목록")
                && e.getCurrentItem()!=null) {
            int page = Integer.parseInt(e.getInventory().getItem(48).getItemMeta().getLocalizedName());
            if (e.getRawSlot()==48 && e.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE)){
                new FriendsMenu().friendsListMenu((Player) e.getWhoClicked(), page-1);
            }else if(e.getRawSlot() == 50 && e.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE)){
                new FriendsMenu().friendsListMenu((Player) e.getWhoClicked(), page+1);
            }
            e.setCancelled(true);
        }
    }
}
