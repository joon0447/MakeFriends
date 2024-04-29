package org.joon.makefriends.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.joon.makefriends.Data.DataManager;
import org.joon.makefriends.MakeFriends;
import org.joon.makefriends.Utils.GetUUID;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FriendGiftListener implements Listener {
    
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) throws IOException {
        Player player = e.getPlayer();
        if(MakeFriends.giftMap.containsKey(player)) {
            Player target = MakeFriends.giftMap.get(player);
            String targetString = new GetUUID().getUUID(target.getName());
            Action action = e.getAction();
            ItemStack gift = player.getInventory().getItemInMainHand();
            if(action == Action.RIGHT_CLICK_AIR) {
                if(player.isSneaking() && gift.getType() != Material.AIR){
                    File file = new DataManager().loadPlayerFile(targetString);
                    YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);
                    List<ItemStack> gifts = (List<ItemStack>) yc.getList("gift");
                    if(gifts.size() > 53) {
                        player.sendMessage("상대방의 선물함이 가득찼습니다!");
                        return;
                    }
                    ItemStack empty = new ItemStack(Material.AIR);
                    player.getInventory().setItemInMainHand(empty);
                    ItemMeta giftMeta = gift.getItemMeta();
                    List<String> newLore = giftMeta.getLore();
                    if(newLore != null){
                        newLore.addAll(giftMeta.getLore());
                        newLore.add("보낸 사람 : " + player.getName());
                        giftMeta.setLore(newLore);
                    }else{
                        giftMeta.setLore(Collections.singletonList(ChatColor.BLUE + "보낸 사람 : " + player.getName()));
                    }
                    gift.setItemMeta(giftMeta);
                    gifts.add(gift);
                    yc.set("gift", gifts);
                    try{
                        yc.save(file);
                    }catch(IOException ex){
                        ex.printStackTrace();
                    }
                    player.sendMessage("선물을 성공적으로 보냈습니다.");
                }
            }
        }
    }
}
