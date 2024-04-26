package org.joon.makefriends.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class FriendsMainMenuListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(ChatColor.translateAlternateColorCodes('&',
                e.getView().getTitle()).equals(ChatColor.BLUE.toString() + "친구 메뉴")
                && e.getCurrentItem()!=null){
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();

        }
    }
}
