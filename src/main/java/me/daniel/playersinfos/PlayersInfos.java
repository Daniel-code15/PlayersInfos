package me.daniel.playersinfos;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayersInfos extends JavaPlugin {
    public static PlayersInfos plugin;
    
    @Override
    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(new Events(), this);
        
        Cmds();
        
        Bukkit.getConsoleSender().sendMessage("§6[PlayersInfos] §a=================================");
        Bukkit.getConsoleSender().sendMessage("§6[PlayersInfos] §bPlayersInfos §eAtivado §bcom Sucesso!");
        Bukkit.getConsoleSender().sendMessage("§6[PlayersInfos] §a=================================");
    }
    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§6[PlayersInfos] §c====================================");
        Bukkit.getConsoleSender().sendMessage("§6[PlayersInfos] §dPlayersInfos §cDesativado §dcom Sucesso!");
        Bukkit.getConsoleSender().sendMessage("§6[PlayersInfos] §c====================================");
    }
    
    private void Cmds() {
        getCommand("pinfos").setExecutor(new Comando());
    }
}