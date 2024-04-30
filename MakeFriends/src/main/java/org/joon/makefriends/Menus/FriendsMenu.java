package org.joon.makefriends.Menus;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.joon.makefriends.Data.DataManager;
import org.joon.makefriends.Data.FriendsManager;
import org.joon.makefriends.Data.MailManager;
import org.joon.makefriends.Utils.PageUtil;

import java.io.File;
import java.util.*;

public class FriendsMenu {

    // 친구 메인 메뉴 - 친구 목록, 친구 추가, 친구 삭제
    public void FriendsMainMenu(Player player){
        Inventory inv = Bukkit.createInventory(player, 9, ChatColor.BLUE.toString() + "친구 메뉴");

        //친구 목록
        ItemStack friendsList = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta friendsListMeta = friendsList.getItemMeta();
        assert friendsListMeta != null;
        friendsListMeta.setDisplayName(ChatColor.GREEN + "친구 목록");
        friendsListMeta.setLore(Collections.singletonList(ChatColor.BLUE.toString() + "친구 목록을 확인합니다."));
        friendsList.setItemMeta(friendsListMeta);
        inv.setItem(1, friendsList);

        //친구 추가, 삭제 -> 관리
        ItemStack friendsManager = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta friendsManagerMeta = friendsManager.getItemMeta();
        assert friendsManagerMeta != null;
        friendsManagerMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        friendsManagerMeta.setDisplayName(ChatColor.GREEN + "친구 관리");
        friendsManagerMeta.setLore(Collections.singletonList(ChatColor.BLUE.toString() + "친구 요청,삭제를 합니다."));
        friendsManager.setItemMeta(friendsManagerMeta);
        inv.setItem(3, friendsManager);

        //수신함 -> 친구 요청 확인
        ItemStack inviteFriends = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta inviteFriendsMeta = inviteFriends.getItemMeta();
        assert inviteFriendsMeta != null;
        inviteFriendsMeta.setDisplayName(ChatColor.RED + "수신함");
        inviteFriendsMeta.setLore(Collections.singletonList(ChatColor.BLUE.toString() + "친구 요청을 확인합니다."));
        inviteFriends.setItemMeta(inviteFriendsMeta);
        inv.setItem(5, inviteFriends);

        //선물함
        ItemStack present = new ItemStack(Material.BLUE_SHULKER_BOX);
        ItemMeta presentMeta = present.getItemMeta();
        assert presentMeta != null;
        presentMeta.setDisplayName(ChatColor.YELLOW + "선물");
        presentMeta.setLore(Collections.singletonList(ChatColor.BLUE + "선물을 확인하거나 보냅니다."));
        present.setItemMeta(presentMeta);
        inv.setItem(7, present);

        //인벤토리 열기
        player.openInventory(inv);
    }
    
    // 친구 관리 메뉴
    public void friendsManagerMenu(Player player){
        Inventory inv = Bukkit.createInventory(player, 9, ChatColor.BLUE + "친구 관리");

        //친구 추가
        ItemStack addFriends = new ItemStack(Material.SLIME_BALL);
        ItemMeta addFriendsMeta = addFriends.getItemMeta();
        assert addFriendsMeta != null;
        addFriendsMeta.setDisplayName(ChatColor.GREEN + "친구 추가");
        addFriendsMeta.setLore(Collections.singletonList(ChatColor.BLUE + "친구 요청을 보냅니다."));
        addFriends.setItemMeta(addFriendsMeta);
        inv.setItem(3, addFriends);

        //친구 삭제
        ItemStack removeFriends = new ItemStack(Material.MAGMA_CREAM);
        ItemMeta removeFriendsMeta = removeFriends.getItemMeta();
        assert removeFriendsMeta != null;
        removeFriendsMeta.setDisplayName(ChatColor.RED + "친구 삭제");
        removeFriendsMeta.setLore(Collections.singletonList(ChatColor.BLUE + "친구를 삭제합니다."));
        removeFriends.setItemMeta(removeFriendsMeta);
        inv.setItem(5, removeFriends);

        player.openInventory(inv);
    }

    // 친구 추가 메뉴
    public void friendsAddMenu(Player player, int page){
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLUE + "친구 초대");
        List<ItemStack> allItems = new ArrayList<ItemStack>();
        ItemStack line = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta lineMeta = line.getItemMeta();
        lineMeta.setDisplayName(ChatColor.GREEN + "온라인 유저만 표시됩니다.");
        line.setItemMeta(lineMeta);

        for(int i=0; i<9; i++){
            inv.setItem(i, line);
        }
        for(int i=9; i<=45; i+=9){
            inv.setItem(i, line);
        }
        for(int i=17; i<=53; i+=9){
            inv.setItem(i, line);
        }
        for(int i=46; i<=52; i++){
            if (i == 48 || i == 50) {
                continue;
            }
            inv.setItem(i, line);
        }

        ItemStack playerHead;
        SkullMeta playerHeadMeta;
        List<String> friendsList = new FriendsManager().loadFriends(player);
        for(Player p : Bukkit.getOnlinePlayers()){
            UUID uuid = p.getUniqueId();
            if(!p.equals(player) && !friendsList.contains(uuid.toString())){
                playerHead = new ItemStack(Material.PLAYER_HEAD);
                playerHeadMeta = (SkullMeta) playerHead.getItemMeta();
                playerHeadMeta.setOwningPlayer(p);
                playerHeadMeta.setDisplayName(p.getName());
                playerHeadMeta.setLore(Collections.singletonList(ChatColor.GREEN + "좌클릭시 초대 요청을 보냅니다."));
                playerHead.setItemMeta(playerHeadMeta);
                allItems.add(playerHead);
            }
        }


        ItemStack left;
        ItemMeta leftMeta;
        if(PageUtil.isPageValid(allItems, page-1, 28)){
            left = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            leftMeta = left.getItemMeta();
            assert leftMeta != null;
            leftMeta.setDisplayName(ChatColor.GREEN + "이전 페이지");
        }else{
            left = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            leftMeta = left.getItemMeta();
            assert leftMeta != null;
            leftMeta.setDisplayName(ChatColor.RED + "이전 페이지가 없습니다.");
        }
        leftMeta.setLocalizedName(page + "");
        left.setItemMeta(leftMeta);
        inv.setItem(48, left);

        ItemStack right;
        ItemMeta rightMeta;
        if(PageUtil.isPageValid(allItems, page+1, 28)){
            right = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            rightMeta = right.getItemMeta();
            assert rightMeta != null;
            rightMeta.setDisplayName(ChatColor.GREEN + "다음 페이지");
        }else{
            right = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            rightMeta = right.getItemMeta();
            assert rightMeta != null;
            rightMeta.setDisplayName(ChatColor.RED + "다음 페이지가 없습니다.");
        }
        right.setItemMeta(rightMeta);
        inv.setItem(50, right);

        for(ItemStack is : PageUtil.getPageItems(allItems, page, 28)){
            inv.setItem(inv.firstEmpty(), is);
        }
        player.openInventory(inv);
    }

    //친구 삭제 메뉴
    public void friendsRemoveMenu(Player player, int page){
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLUE + "친구 삭제");
        List<ItemStack> allItems = new ArrayList<ItemStack>();
        ItemStack line = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta lineMeta = line.getItemMeta();
        lineMeta.setDisplayName(ChatColor.GREEN + "좌클릭 시 친구를 삭제합니다.");
        line.setItemMeta(lineMeta);

        for(int i=0; i<9; i++){
            inv.setItem(i, line);
        }
        for(int i=9; i<=45; i+=9){
            inv.setItem(i, line);
        }
        for(int i=17; i<=53; i+=9){
            inv.setItem(i, line);
        }
        for(int i=46; i<=52; i++){
            if (i == 48 || i == 50) {
                continue;
            }
            inv.setItem(i, line);
        }

        ItemStack playerHead;
        SkullMeta playerHeadMeta;
        List<String> friends = new FriendsManager().loadFriends(player);
        for(String s: friends){
            OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(s));
            playerHead = new ItemStack(Material.PLAYER_HEAD);
            playerHeadMeta = (SkullMeta) playerHead.getItemMeta();
            playerHeadMeta.setOwningPlayer(p);
            playerHeadMeta.setDisplayName(p.getName());
            playerHeadMeta.setLore(Collections.singletonList(ChatColor.RED + "좌클릭 시 친구를 삭제합니다."));
            playerHead.setItemMeta(playerHeadMeta);
            allItems.add(playerHead);
        }


        ItemStack left;
        ItemMeta leftMeta;
        if(PageUtil.isPageValid(allItems, page-1, 28)){
            left = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            leftMeta = left.getItemMeta();
            assert leftMeta != null;
            leftMeta.setDisplayName(ChatColor.GREEN + "이전 페이지");
        }else{
            left = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            leftMeta = left.getItemMeta();
            assert leftMeta != null;
            leftMeta.setDisplayName(ChatColor.RED + "이전 페이지가 없습니다.");
        }
        leftMeta.setLocalizedName(page + "");
        left.setItemMeta(leftMeta);
        inv.setItem(48, left);

        ItemStack right;
        ItemMeta rightMeta;
        if(PageUtil.isPageValid(allItems, page+1, 28)){
            right = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            rightMeta = right.getItemMeta();
            assert rightMeta != null;
            rightMeta.setDisplayName(ChatColor.GREEN + "다음 페이지");
        }else{
            right = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            rightMeta = right.getItemMeta();
            assert rightMeta != null;
            rightMeta.setDisplayName(ChatColor.RED + "다음 페이지가 없습니다.");
        }
        right.setItemMeta(rightMeta);
        inv.setItem(50, right);

        for(ItemStack is : PageUtil.getPageItems(allItems, page, 28)){
            inv.setItem(inv.firstEmpty(), is);
        }
        player.openInventory(inv);
    }

    // 친구 목록 메뉴
    public void friendsListMenu(Player player, int page, String invName){
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLUE + invName);
        List<ItemStack> allItems = new ArrayList<>();
        File file = new DataManager().loadPlayerFile(player.getUniqueId());
        YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);
        List<String> list = (List<String>) yc.get("friends");
        ItemStack userHead;
        SkullMeta userHeadMeta;
        ItemStack line = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta lineMeta = line.getItemMeta();
        lineMeta.setDisplayName(ChatColor.GREEN + "친구 목록");
        line.setItemMeta(lineMeta);

        for(int i=0; i<9; i++){
            inv.setItem(i, line);
        }
        for(int i=9; i<=45; i+=9){
            inv.setItem(i, line);
        }
        for(int i=17; i<=53; i+=9){
            inv.setItem(i, line);
        }
        for(int i=46; i<=52; i++){
            if (i == 48 || i == 50) {
                continue;
            }
            inv.setItem(i, line);
        }

        for(String s : list){
            Player friends= Bukkit.getPlayer(UUID.fromString(s));
            userHead = new ItemStack(Material.PLAYER_HEAD);
            userHeadMeta = (SkullMeta) userHead.getItemMeta();
            assert userHeadMeta != null;
            userHeadMeta.setOwningPlayer(friends);
            if(Bukkit.getOfflinePlayer(UUID.fromString(s)).isOnline()){
                Location loc = friends.getLocation();
                String lore = "X : " + Math.ceil(loc.getX()) + " Z : " + Math.ceil(loc.getZ());
                userHeadMeta.setDisplayName(ChatColor.GREEN + friends.getName());
                userHeadMeta.setLore(Arrays.asList(ChatColor.GREEN + "온라인", ChatColor.BLUE + lore));
            }else{
                userHeadMeta.setDisplayName(ChatColor.GRAY + Bukkit.getOfflinePlayer(UUID.fromString(s)).getName());
                userHeadMeta.setLore(Collections.singletonList(ChatColor.GRAY + "오프라인"));
            }

            userHead.setItemMeta(userHeadMeta);
            allItems.add(userHead);
        }

        ItemStack left;
        ItemMeta leftMeta;
        if(PageUtil.isPageValid(allItems, page-1, 28)){
            left = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            leftMeta = left.getItemMeta();
            assert leftMeta != null;
            leftMeta.setDisplayName(ChatColor.GREEN + "이전 페이지");
        }else{
            left = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            leftMeta = left.getItemMeta();
            assert leftMeta != null;
            leftMeta.setDisplayName(ChatColor.RED + "이전 페이지가 없습니다.");
        }
        leftMeta.setLocalizedName(page + "");
        left.setItemMeta(leftMeta);
        inv.setItem(48, left);
        
        ItemStack right;
        ItemMeta rightMeta;
        if(PageUtil.isPageValid(allItems, page+1, 28)){
            right = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            rightMeta = right.getItemMeta();
            assert rightMeta != null;
            rightMeta.setDisplayName(ChatColor.GREEN + "다음 페이지");
        }else{
            right = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            rightMeta = right.getItemMeta();
            assert rightMeta != null;
            rightMeta.setDisplayName(ChatColor.RED + "다음 페이지가 없습니다.");
        }
        right.setItemMeta(rightMeta);
        inv.setItem(50, right);

        for(ItemStack is : PageUtil.getPageItems(allItems, page, 28)){
            inv.setItem(inv.firstEmpty(), is);
        }
        player.openInventory(inv);
    }

    // 친구 수신함 메뉴
    public void friendsMailMenu(Player player){
        Inventory inv = Bukkit.createInventory(player, 54, ChatColor.BLUE + "수신함");
        List<String> mailList = new MailManager().loadMail(player);

        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();

        for(String s : mailList){
            playerHeadMeta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(s)));
            playerHeadMeta.setDisplayName( Bukkit.getOfflinePlayer(UUID.fromString(s)).getName());
            playerHeadMeta.setLore(Arrays.asList(ChatColor.BLUE + "좌클릭 - 친구 수락", ChatColor.BLUE + "우클릭 - 친구 거절"));
            playerHead.setItemMeta(playerHeadMeta);
            inv.setItem(inv.firstEmpty(), playerHead);
        }

        player.openInventory(inv);
    }

    // 선물 메뉴
    public void friendsGiftMenu(Player player){
        Inventory inv = Bukkit.createInventory(player, 9, ChatColor.BLUE + "선물");
        
        // 선물 보내기
        ItemStack sendGift = new ItemStack(Material.ENDER_CHEST);
        ItemMeta sendGiftMeta = sendGift.getItemMeta();
        sendGiftMeta.setDisplayName(ChatColor.GREEN + "선물 보내기");
        sendGiftMeta.setLore(Collections.singletonList(ChatColor.BLUE + "친구에게 선물을 보냅니다."));
        sendGift.setItemMeta(sendGiftMeta);
        inv.setItem(3,sendGift);

        // 선물함
        ItemStack checkGift = new ItemStack(Material.CHERRY_CHEST_BOAT);
        ItemMeta checkGiftMeta = checkGift.getItemMeta();
        checkGiftMeta.setDisplayName(ChatColor.GREEN + "선물함");
        checkGiftMeta.setLore(Collections.singletonList(ChatColor.BLUE + "선물을 확인합니다."));
        checkGift.setItemMeta(checkGiftMeta);
        inv.setItem(5, checkGift);

        player.openInventory(inv);
    }


    // 선물함 메뉴
    public void friendsCheckGiftMenu(Player player){
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BLUE + "선물함");
        UUID uuid = player.getUniqueId();

        File file = new DataManager().loadPlayerFile(uuid);
        YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);
        List<ItemStack> giftList = (List<ItemStack>) yc.getList("gift");

        for(ItemStack is : giftList){
            inv.setItem(inv.firstEmpty(), is);
        }

        player.openInventory(inv);
    }
}
