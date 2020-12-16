package me.daniel.playersinfos;

import java.io.IOException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.DateFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerChatEvent;


public class Events implements Listener {
    
    public static T_Config configs = new T_Config(PlayersInfos.getPlugin(PlayersInfos.class), "config.yml");
    
    @EventHandler
    public void onChat(PlayerChatEvent e) throws IOException {
        Player p = (Player) e.getPlayer();
        String content = e.getMessage();
        JSONObject json = new JSONObject();
        json.put("content", content);
        json.put("username", p.getName());
        json.put("avatar_url", "https://mc-heads.net/head/"+p.getName());
        
        
        if(configs.getConfig().getBoolean("discord.logs-chat")) {
            if(configs.getConfig().getString("discord.chat-webhook") != null) {
                String hook = configs.getConfig().getString("discord.chat-webhook");
                
                if(hook.contains("https://discord.com/api/webhooks/")) {
                    Request.post(p.getName(), json, hook);
                }
            }
        }
        
        
        
    }
    
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws IOException {
        
        Player p = e.getPlayer();
        
        String prefix = "§6[PlayersInfos] ";
        String region = RegionFinder(p.getAddress().getHostName(), configs.getConfig().getString("region.template"));
        
        List<String> consoleinfos = clear(p, configs.getConfig().getStringList("template.console"), region);
        List<String> hookinfos = clear(p, configs.getConfig().getStringList("template.webhook"), region);
        List<String> staffinfos = clear(p, configs.getConfig().getStringList("template.staff"), region);
        
        
        if(configs.getConfig().getBoolean("show-console")) {
            for(String line : consoleinfos) {
                Bukkit.getConsoleSender().sendMessage(line);
            }
        }
        
        if(configs.getConfig().getBoolean("show-staff")) {
            for(Player player : Bukkit.getOnlinePlayers()) {
                if(player.hasPermission("playersinfos.staff")) {
                    for(String line : staffinfos) {
                        player.sendMessage(line);
                    }
                }
            }
        }
        
        if(configs.getConfig().getBoolean("discord.logs-infos")) {
            if(configs.getConfig().getString("discord.logs-webhook") != null) {
                String hook = configs.getConfig().getString("discord.logs-webhook");
                
                if(hook.contains("https://discord.com/api/webhooks/")) {
                    JSONObject json = new JSONObject();
                    String content = "";
                    
                    for(String line : hookinfos) {
                        content += line+"\n";
                    }
                    
                    //Bukkit.getConsoleSender().sendMessage(hook);
                    
                    json.put("content", content);
                    json.put("username", p.getName());
                    json.put("avatar_url", "https://mc-heads.net/head/"+p.getName());
                    
                    //String params = "content="+content+"&username="+p.getName()+"&avatar_url="+"https://mc-heads.net/head/"+p.getName();
                    
                    //Request.class(json, url);
                    Request.post(p.getName(), json, hook);
                } else {
                    Bukkit.getConsoleSender().sendMessage(prefix+"§cErro, Webhook invalido!");
                }
            } else {
                Bukkit.getConsoleSender().sendMessage(prefix+"§cErro ao enviar mensagem ao discord! Faltando Webhook");
            }
        }
        
        
    }
    private List clear(Player p, List<String> lista,String region) throws IOException {
        List<String> base = new ArrayList();
        
        
        String lastplayed = lastOnline(p);
        String op = "Não";
        String whitelisted = "Não";
        
        if(p.isOp()) {
            op = "Sim";
        }
        if(p.isWhitelisted()) {
            whitelisted = "Sim";
        }
        
        for(String line : lista) {
            line = line.replaceAll("&", "§");
            line = line.replaceAll("<player_ip>", p.getAddress().getHostName());
            line = line.replaceAll("<region>", region);
            line = line.replaceAll("<player>", p.getName());
            line = line.replaceAll("<player_health>", String.valueOf(p.getHealth()));
            line = line.replaceAll("<player_coords>", p.getLocation().getX()+" / "+ p.getLocation().getY()+" / "+ p.getLocation().getZ());
            line = line.replaceAll("<player_world>", p.getLocation().getWorld().getName());
            line = line.replaceAll("<player_gm>", p.getGameMode().name());
            line = line.replaceAll("<player_uuid>", String.valueOf(p.getUniqueId()));
            line = line.replaceAll("<player_lastplayed>", lastplayed);
            line = line.replaceAll("<player_hasOp>", op);
            line = line.replaceAll("<whitelisted>", whitelisted);
            
            
            base.add(line);
        }
        
        
        return base;
    }
    private String lastOnline (Player p) {
        String lastplayed;
        if(p.getLastPlayed() == 0) {
            lastplayed = "Nunca";
        } else {
            String data = DateFormat.getDateTimeInstance().format(p.getLastPlayed());
            lastplayed = data;
        }
        return lastplayed;
    }
    
    private String RegionFinder (String ip, String region) throws UnknownHostException, IOException {
        
        String url = "http://ip-api.com/json/" + ip;
        
        JSONObject json = JsonReader.readJsonFromUrl(url);
        
        if(!json.getString("status").equals("success")) {
            return "§4Erro";
        }
        
        if(json.has("city")) {
            String cidade = json.getString("city");
            String regiao = json.getString("regionName");
            String pais = json.getString("country");
            String pais_sig = json.getString("countryCode");
            
            region = region.replaceAll("<cidade>", cidade);
            region = region.replaceAll("<pais>", pais);
            region = region.replaceAll("<pais-sig>", pais_sig);
            region = region.replaceAll("<estado>", regiao);
            
        } else if (json.has("region_name")) {
            
            String cidade = "¯\\_(ツ)_/¯";
            String regiao = json.getString("regionName");
            String pais = json.getString("country");
            String pais_sig = json.getString("countryCode");
            
            region = region.replaceAll("<cidade>", cidade);
            region = region.replaceAll("<pais>", pais);
            region = region.replaceAll("<pais-sig>", pais_sig);
            region = region.replaceAll("<estado>", regiao);
        } else if (json.has("country")) {
            String cidade = "¯\\_(ツ)_/¯";
            String regiao = "¯\\_(ツ)_/¯";
            String pais = json.getString("country");
            String pais_sig = json.getString("countryCode");
            
            region = region.replaceAll("<cidade>", cidade);
            region = region.replaceAll("<pais>", pais);
            region = region.replaceAll("<pais-sig>", pais_sig);
            region = region.replaceAll("<estado>", regiao);
        } else {
            region = "Erro";
        }
        
        return region.replaceAll("&", "§");
    }
    
    
    
    /*  Talvez um futuro update, atualmente IMPOSSIVEL
    private boolean isPremium (String USERNAME, String serverHash) throws IOException {
        boolean bl;
        
        String link = "https://sessionserver.mojang.com/session/minecraft/hasJoined?username="+USERNAME+"&serverId="+serverHash;
        
        URL url = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            bl = true;
        } else {
            bl = false;
        }
        
        
        //JSONObject json = JsonReader.readJsonFromUrl(url);
        
        return bl;
    }
    */
    
}
