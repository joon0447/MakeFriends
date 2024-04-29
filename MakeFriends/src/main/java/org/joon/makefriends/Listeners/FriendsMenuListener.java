package org.joon.makefriends.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.joon.makefriends.Data.DataManager;
import org.joon.makefriends.Data.FriendsManager;
import org.joon.makefriends.Data.MailManager;
import org.joon.makefriends.MakeFriends;
import org.joon.makefriends.Menus.FriendsMenu;
import org.joon.makefriends.Utils.GetUUID;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;

public class FriendsMenuListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) throws IOException {
        if(ChatColor.translateAlternateColorCodes('&',
                e.getView().getTitle()).equals(ChatColor.BLUE + "친구 메뉴")
                && e.getCurrentItem()!=null){
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            switch (e.getRawSlot()){
                case 1: // 친구 목록
                    new FriendsMenu().friendsListMenu(player, 1, "친구 목록");
                    break;
                case 3: // 친구 관리
                    player.closeInventory();
                    new FriendsMenu().friendsManagerMenu(player);
                    break;
                case 5: // 수신함
                    new FriendsMenu().friendsMailMenu(player);
                    break;
                case 7: // 선물
                    new FriendsMenu().friendsGiftMenu(player);
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
                    new FriendsMenu().friendsRemoveMenu(player, 1);
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
            new MailManager().addMail(target, player);
            player.closeInventory();
        }

        else if(ChatColor.translateAlternateColorCodes('&',
                e.getView().getTitle()).equals(ChatColor.BLUE + "친구 목록")
                && e.getCurrentItem()!=null) {
            int page = Integer.parseInt(e.getInventory().getItem(48).getItemMeta().getLocalizedName());
            if (e.getRawSlot()==48 && e.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE)){
                new FriendsMenu().friendsListMenu((Player) e.getWhoClicked(), page-1, "친구 목록");
            }else if(e.getRawSlot() == 50 && e.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE)){
                new FriendsMenu().friendsListMenu((Player) e.getWhoClicked(), page+1, "친구 목록");
            }
            e.setCancelled(true);
        }

        else if(ChatColor.translateAlternateColorCodes('&',
                e.getView().getTitle()).equals(ChatColor.BLUE + "수신함")
                && e.getCurrentItem()!=null){
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            Player target;
            if(e.getClick().isLeftClick()){ // 친구 수락
                target = Bukkit.getPlayer(e.getCurrentItem().getItemMeta().getDisplayName().toString());
                new MailManager().acceptMail(player, target);
                player.closeInventory();
                player.sendMessage("친구 요청을 수락하셨습니다.");
                target.sendMessage("상대방이 친구 요청을 수락하셨습니다.");
            }else if(e.getClick().isRightClick()){ // 친구 거절
                target = Bukkit.getPlayer(e.getCurrentItem().getItemMeta().getDisplayName().toString());
                new MailManager().refuseMail(player, target);
                player.sendMessage("친구 요청을 거절하셨습니다.");
                player.closeInventory();
            }


        }

        else if(ChatColor.translateAlternateColorCodes('&',
                e.getView().getTitle()).equals(ChatColor.BLUE + "친구 삭제")
                && e.getCurrentItem()!=null){
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            UUID uuid = player.getUniqueId();
            String str = new GetUUID().getUUID(e.getCurrentItem().getItemMeta().getDisplayName().toString());
            UUID targetUUID = UUID.fromString(str);

            List<String> friendsList = new FriendsManager().loadFriends(player);
            friendsList.remove(str);

            File file = new DataManager().loadPlayerFile(uuid); // 자기 자신
            YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);
            yc.set("friends", friendsList);

            File file2 = new DataManager().loadPlayerFile(targetUUID); // 상대방
            YamlConfiguration yc2 = YamlConfiguration.loadConfiguration(file2);
            List<String> friendsList2 = new FriendsManager().loadFriends(str);
            friendsList2.remove(uuid.toString());
            yc2.set("friends", friendsList2);
            try{
                yc.save(file);
                yc2.save(file2);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            player.sendMessage("친구 삭제가 완료되었습니다.");
            player.closeInventory();
        }

        else if(ChatColor.translateAlternateColorCodes('&',
                e.getView().getTitle()).equals(ChatColor.BLUE + "선물")
                && e.getCurrentItem()!=null){
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            switch (e.getRawSlot()) {
                case 3: // 선물 보내기
                    new FriendsMenu().friendsListMenu(player, 1, "선물 보내기");
                    break;
                case 5: // 선물함
                    new FriendsMenu().friendsCheckGiftMenu(player);
                    break;
                default:
                    break;
            }
        }

        else if(ChatColor.translateAlternateColorCodes('&',
                e.getView().getTitle()).equals(ChatColor.BLUE + "선물 보내기")
                && e.getCurrentItem()!=null){
            {
                e.setCancelled(true);
                Player player = (Player) e.getWhoClicked();
                String s = new GetUUID().getUUID(e.getCurrentItem().getItemMeta().getDisplayName().toString().substring(2));
                UUID targetUUID = UUID.fromString(s);
                Player target = Bukkit.getPlayer(targetUUID);
                MakeFriends.giftMap.put(player, target);
                File file = new DataManager().loadPlayerFile(s);
                YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);
                List<ItemStack> giftList = (List<ItemStack>) yc.getList("gift");
                player.sendMessage(target.getName() + " 님에게 선물을 보냅니다.");
                player.sendMessage("선물을 보낼 아이템을 손에 들고 우클릭을 해주세요.");
                player.sendMessage("손에 든 아이템의 개수 모두 선물로 보내집니다!");
                player.closeInventory();
            }
        }

        else if(ChatColor.translateAlternateColorCodes('&',
                e.getView().getTitle()).equals(ChatColor.BLUE + "선물함")
                && e.getCurrentItem()!=null){
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            int slot = e.getSlot();
            UUID uuid = player.getUniqueId();
            File file = new DataManager().loadPlayerFile(uuid);
            YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);
            List<ItemStack> giftList = (List<ItemStack>) yc.getList("gift");
            giftList.remove(slot);
            yc.set("gift", giftList);
            try{
                yc.save(file);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            ItemStack item = e.getClickedInventory().getItem(e.getSlot());
        }
    }
}
