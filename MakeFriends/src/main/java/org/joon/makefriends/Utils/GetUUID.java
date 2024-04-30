package org.joon.makefriends.Utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URL;

public class GetUUID {

    public String getUUID(String name) throws IOException {
        try {
            String apiUrl = "https://api.mojang.com/users/profiles/minecraft/" + name;

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(new URL(apiUrl));

            String uuid = jsonNode.get("id").asText();
            String result = uuid.substring(0, 8);
            result += "-" + uuid.substring(8, 12);
            result += "-" + uuid.substring(12, 16);
            result += "-" + uuid.substring(16, 20);
            result += "-" + uuid.substring(20, 32);
            return result;
        } catch (IOException e) {
            return null;
        }
    }
}
