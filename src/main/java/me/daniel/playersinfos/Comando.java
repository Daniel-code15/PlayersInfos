package me.daniel.playersinfos;

import java.util.List;
import static me.daniel.playersinfos.PlayersInfos.plugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Comando implements CommandExecutor {
    
    public static T_Config configs = new T_Config(PlayersInfos.getPlugin(PlayersInfos.class), "config.yml");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {
        
        if(!(sender instanceof Player)) {
            if(args.length < 1) {
                send(sender, "§cUso: /pinfos reload");
                return true;
            }
            
            if(args[0].equalsIgnoreCase("reload")) {
                Bukkit.getServer().getPluginManager().disablePlugin(plugin);
                Bukkit.getServer().getPluginManager().getPlugin("PlayersInfos").reloadConfig();
                Bukkit.getPluginManager().getPlugin("PlayersInfos").reloadConfig();
                
                Comando.configs.reloadConfig();
                Events.configs.reloadConfig();
                PlayersInfos.plugin.reloadConfig();
                
                Bukkit.getPluginManager().getPlugin(PlayersInfos.plugin.getName()).reloadConfig();
                Bukkit.getServer().getPluginManager().enablePlugin(plugin);
                send(sender, "§aConfigurações recarregadas com sucesso!");
            } else {
                send(sender, "§cUso: /pinfos reload");
            }
        } else {
            Player p = (Player) sender;
            if(args.length < 1) {
                send(sender, "§cUso: /pinfos reload");
                return true;
            }
            
            if(args[0].equalsIgnoreCase("reload")) {
                if(!p.hasPermission("playersinfos.reload")) {
                    send(sender, "§cVocê não tem permissões suficientes para isso!");
                    return true;
                }
                
                Bukkit.getServer().getPluginManager().disablePlugin(plugin);
                Bukkit.getServer().getPluginManager().getPlugin("PlayersInfos").reloadConfig();
                Bukkit.getPluginManager().getPlugin("PlayersInfos").reloadConfig();
                
                Comando.configs.reloadConfig();
                Events.configs.reloadConfig();
                PlayersInfos.plugin.reloadConfig();
                
                Bukkit.getPluginManager().getPlugin(PlayersInfos.plugin.getName()).reloadConfig();
                Bukkit.getServer().getPluginManager().enablePlugin(plugin);
                send(sender, "§aConfigurações recarregadas com sucesso!");
            } else {
                send(sender, "§cUso: /pinfos reload");
            }
        }
        
        
        
        return false;
    }
    
    private void send(CommandSender sender, String txt) {
        if(!(sender instanceof Player)) {
            txt = txt.replaceAll("&", "§");
            Bukkit.getConsoleSender().sendMessage(txt);
        } else {
            txt = txt.replaceAll("&", "§");
            Player p = (Player) sender;
            p.sendMessage(txt);
        }
    }
}
