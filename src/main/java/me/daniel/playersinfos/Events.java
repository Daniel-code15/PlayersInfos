package me.daniel.playersinfos;


import java.io.IOException;
import java.net.HttpURLConnection;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class Events implements Listener {
    
    List<String> ips = new ArrayList();
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws IOException {
        
        Player p = e.getPlayer();
        
        
        String region = RegionFinder(p.getAddress().getHostName());
        //http://freegeoip.net/json/
        
        String lastplayed = "";
        
        if(p.getLastPlayed() == 0) {
            lastplayed = "Nunca";
        } else {
            String data = DateFormat.getDateTimeInstance().format(p.getLastPlayed());
            lastplayed = data;
        }
        
        
        send("§6===========§2[§3PlayersInfos§2]§6===========");
        send("§4IP: §6"+p.getAddress().getHostString());
        send("§4Região: §6"+region);
        send(" ");
        send("§4Jogador: §6"+p.getDisplayName());
        send("§4Vida: §6"+p.getHealth());
        send("§4Coordenadas: §6X"+ p.getLocation().getX()+" Y"+ p.getLocation().getY()+" Z"+ p.getLocation().getZ());
        send("§4Mundo: §6"+p.getWorld().getName());
        send("§4Gamemode: §6"+p.getGameMode().name());
        send("§4UUID: §6"+p.getUniqueId());
        send("§4Visto em: §6"+lastplayed);
        send("§4OP: §6"+ p.isOp());
        
        if(p.isBanned()) {
            String reason = Bukkit.getServer().getBanList(BanList.Type.NAME).getBanEntry(p.getName()).getReason();
            send("§4Banido: §6"+ p.isBanned());
            send("§4Motivo: §6"+ reason);
        }
        
        send("§4Whitelisted: §6"+ p.isWhitelisted());
        send("§6===========§2[§3PlayersInfos§2]§6===========");
    }
    
    private void send (String msg) {
        Bukkit.getConsoleSender().sendMessage(msg);
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
    private String RegionFinder (String ip) throws UnknownHostException, IOException {
        String region = "";
        
        //http://freegeoip.net/json/
        
        //InetAddress addr = InetAddress.getByName(ip);
        //String hostname = addr.getHostName();
        
        String url = "http://ip-api.com/json/" + ip;
        
        JSONObject json = JsonReader.readJsonFromUrl(url);
        
        if(!json.getString("status").equals("success")) {
            return "§4Erro";
        }
        
        if(json.has("city")) {
            region = "§7Cidade: §e"+json.getString("city")+" §7Região §e"+json.getString("regionName")+" §7[§6"+json.getString("country")+"§7("+json.getString("countryCode")+")]";
        } else if (json.has("region_name")) {
            region = "§7Cidade §4Erro §7Região §6"+json.getString("regionName")+" §7[§6"+json.getString("country")+"§7("+json.getString("countryCode")+")]";
        } else if (json.has("country")) {
            region = "§7Cidade §4Erro §7Região §4Erro §7[§6"+json.getString("country")+"§7("+json.getString("countryCode")+")]";
        } else {
            region = "§4Erro";
        }
        
        return region;
    }
    
}
