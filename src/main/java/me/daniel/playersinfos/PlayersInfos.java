package me.daniel.playersinfos;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayersInfos extends JavaPlugin {
    public static PlayersInfos plugin;
    
    @Override
    public void onEnable() {
        plugin = this;
        
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        
        
        Bukkit.getConsoleSender().sendMessage("§a=================================");
        Bukkit.getConsoleSender().sendMessage("§bPlayersInfos §eAtivado §bcom Sucesso!");
        Bukkit.getConsoleSender().sendMessage("§a=================================");
    }
    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§c====================================");
        Bukkit.getConsoleSender().sendMessage("§dPlayersInfos §cDesativado §dcom Sucesso!");
        Bukkit.getConsoleSender().sendMessage("§c====================================");
    }
}