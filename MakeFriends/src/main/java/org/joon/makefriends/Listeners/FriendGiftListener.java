package org.joon.makefriends.Listeners;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.joon.makefriends.Data.DataManager;
import org.joon.makefriends.MakeFriends;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FriendGiftListener implements Listener {
    
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) throws IOException {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if(MakeFriends.giftMap.containsKey(uuid)) {
            UUID targetUUID = MakeFriends.giftMap.get(uuid);
            OfflinePlayer target = Bukkit.getOfflinePlayer(targetUUID);
            Action action = e.getAction();
            ItemStack gift = player.getInventory().getItemInMainHand();
            if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                if(player.isSneaking() && gift.getType() != Material.AIR){
                    File file = new DataManager().loadPlayerFile(targetUUID);
                    YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);
                    List<ItemStack> gifts = (List<ItemStack>) yc.getList("gift");
                    if(gifts.size() > 53) {
                        player.sendMessage(MakeFriends.prefix + "상대방의 선물함이 가득찼습니다!");
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
                    player.sendMessage(MakeFriends.prefix + "선물을 성공적으로 보냈습니다.");
                    if(target.isOnline()){
                        Player oTarget = Bukkit.getPlayer(targetUUID);
                        oTarget.sendMessage(MakeFriends.prefix + player.getName() + "님으로부터 선물이 도착했습니다.");
                        oTarget.sendMessage(MakeFriends.prefix + "선물함을 확인해주세요!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        Player player = e.getPlayer();
        if(MakeFriends.giftMap.containsKey(player)) {
            e.setCancelled(true);
            net.md_5.bungee.api.chat.TextComponent text =
                    new net.md_5.bungee.api.chat.TextComponent(
                            MakeFriends.prefix + "선물 보내기를 종료하려면 오른쪽 종료 버튼 -> ");
            TextComponent btn = new TextComponent(ChatColor.RED + "[종료] ");
            text.addExtra(btn);
            btn.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/친구 선물종료"));
            player.sendMessage(MakeFriends.prefix + "현재 선물 보내기 모드입니다.");
            player.spigot().sendMessage(text);
            player.sendMessage(MakeFriends.prefix + "또는 " +
                    ChatColor.GREEN + "/친구 선물종료 "
                    + ChatColor.WHITE + "명령어를 입력해주세요.");
        }
    }
}
