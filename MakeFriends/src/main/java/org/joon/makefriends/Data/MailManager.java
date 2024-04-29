package org.joon.makefriends.Data;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.joon.makefriends.MakeFriends;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MailManager {

    public void addMail(Player target, Player sender){
        UUID uuid = target.getUniqueId();
        File file = new File(MakeFriends.getInstance().getDataFolder(), "PlayerList/" + uuid + ".yml");
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()){
            List<String> friendsList = new ArrayList<String>();
            List<String> mailList = new ArrayList<>();
            yml.set("player", uuid.toString());
            yml.set("friends", friendsList);
            yml.set("mail", mailList);
            try{
                yml.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            UUID senderUUID = sender.getUniqueId();
            List<String> mailList = new ArrayList<>(yml.getStringList("mail"));
            if(mailList.size()==54){
                sender.sendMessage("상대방의 수신함이 가득 차 요청을 보낼 수 없습니다.");
                return;
            }
            if(mailList.contains(senderUUID.toString())){
                sender.sendMessage("이미 친구를 요청했습니다.");
                return;
            }
            mailList.add(senderUUID.toString());
            yml.set("mail", mailList);
            try{
                yml.save(file);
                target.sendMessage("친구 초대가 왔습니다. 수신함을 확인해주세요.");
                sender.sendMessage(target.getName() + "님에게 친구 요청을 보냈습니다.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<String> loadMail(Player player){
        UUID uuid = player.getUniqueId();
        File file = new File(MakeFriends.getInstance().getDataFolder(), "PlayerList/" + uuid + ".yml");
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        return yml.getStringList("mail");
    }

    public void refuseMail(Player player, Player target){  // 메일 (친구 신청) 거절
        UUID targetUUID = target.getUniqueId();
        UUID playerUUID = player.getUniqueId();
        List<String> mailList = loadMail(player);
        mailList.remove(targetUUID.toString());

        File file = new File(MakeFriends.getInstance().getDataFolder(), "PlayerList/" + playerUUID + ".yml");
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        yml.set("mail", mailList);
        try{
            yml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void acceptMail(Player player, Player target){
        refuseMail(player, target);
        UUID targetUUID = target.getUniqueId();
        UUID playerUUID = player.getUniqueId();

        File file1 = new File(MakeFriends.getInstance().getDataFolder(), "PlayerList/" + playerUUID + ".yml");
        File file2 = new File(MakeFriends.getInstance().getDataFolder(), "PlayerList/" + targetUUID + ".yml");
        YamlConfiguration yml1 = YamlConfiguration.loadConfiguration(file1);
        YamlConfiguration yml2 = YamlConfiguration.loadConfiguration(file2);

        List<String> friendsList = yml1.getStringList("friends");
        friendsList.add(targetUUID.toString());

        List<String> friendsList2 = yml2.getStringList("friends");
        friendsList2.add(playerUUID.toString());

        yml1.set("friends", friendsList);
        yml2.set("friends", friendsList2);

        try{
            yml1.save(file1);
            yml2.save(file2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
