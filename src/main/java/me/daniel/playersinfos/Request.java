package me.daniel.playersinfos;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.bukkit.Bukkit;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import java.net.URLEncoder;

public class Request { // d = StringBuilder()
    public static void post(String name, JSONObject rest, String url) throws IOException {
        
        String username = "username="+rest.getString("username");
        String avatar_url = "avatar_url="+rest.getString("avatar_url");
        String content = "content="+filter(rest.getString("content"));
        
        String urlParams = username+"&"+avatar_url+"&"+content;
        
        
        HttpsURLConnection httpClient = (HttpsURLConnection) new URL(url).openConnection();
        
        httpClient.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpClient.setRequestMethod("POST");
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
        //httpClient.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        
        
        httpClient.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
            wr.writeBytes(urlParams);
            wr.flush();
        }
        
        // n sei pq, mas sem isso n funciona \/
        int responseCode = httpClient.getResponseCode();
    }
    
    
    public static String filter(String txt) {
        String content = txt;
        content = content.replaceAll("§a", "");
        content = content.replaceAll("§b", "");
        content = content.replaceAll("§c", "");
        content = content.replaceAll("§d", "");
        content = content.replaceAll("§e", "");
        content = content.replaceAll("§f", "");
        content = content.replaceAll("§1", "");
        content = content.replaceAll("§2", "");
        content = content.replaceAll("§3", "");
        content = content.replaceAll("§4", "");
        content = content.replaceAll("§5", "");
        content = content.replaceAll("§6", "");
        content = content.replaceAll("§7", "");
        content = content.replaceAll("§8", "");
        content = content.replaceAll("§9", "");
        content = content.replaceAll("§0", "");
        content = content.replaceAll("§m", "");
        content = content.replaceAll("§n", "");
        content = content.replaceAll("§o", "");
        content = content.replaceAll("§l", "");
        content = content.replaceAll("§i", "");
        
        return content;
    }
}