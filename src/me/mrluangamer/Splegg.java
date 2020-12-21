package me.mrluangamer;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import me.mrluangamer.commands.SpleggCommand;
import me.mrluangamer.config.MapUtilities;
import me.mrluangamer.events.Listeners;
import me.mrluangamer.events.MapListener;
import me.mrluangamer.events.PlayerListener;
import me.mrluangamer.events.SignListener;
import me.mrluangamer.events.SpleggEvents;
import me.mrluangamer.managers.Game;
import me.mrluangamer.managers.GameManager;
import me.mrluangamer.managers.GameUtilities;
import me.mrluangamer.managers.Status;
import me.mrluangamer.utils.UtilPlayer;
import me.mrluangamer.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Splegg extends JavaPlugin {
   public Economy econ = null;
   public Utils chat;
   public MapUtilities maps;
   public GameUtilities games;
   public GameManager game;
   public Utils pm;
   public Utils utils;
   public Utils config;
   public boolean updateOut = false;
   public String newVer = "";
   public File updateFile = this.getFile();
   public List special = Arrays.asList("Hraponssi, MrLuangamer, > Theluangamer9416");
   public boolean disabling = false;
   boolean economy = true;

   public static Splegg getSplegg() {
      return (Splegg)Bukkit.getPluginManager().getPlugin("Splegg");
   }

   private boolean setupEconomy() {
      if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
         return false;
      } else {
         RegisteredServiceProvider rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
         if (rsp == null) {
            return false;
         } else {
            this.econ = (Economy)rsp.getProvider();
            return this.econ != null;
         }
      }
   }

   public void onEnable() {
      this.chat = new Utils(this);
      if (this.getServer().getPluginManager().getPlugin("WorldEdit") == null) {
         this.chat.log("WorldEdit not found! Please download it from http://dev.bukkit.org/bukkit-plugins/worldedit");
         Bukkit.getPluginManager().disablePlugin(this);
      } else {
         this.maps = new MapUtilities();
         this.games = new GameUtilities();
         this.game = new GameManager(this);
         this.pm = new Utils(this);
         this.utils = new Utils(this);
         this.config = new Utils(this);
         this.maps.c.setup();
         this.config.setup();
         this.getConfig().options().copyDefaults(true);
         this.saveConfig();
         this.getServer().getPluginManager().registerEvents(new MapListener(this), this);
         this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
         this.getServer().getPluginManager().registerEvents(new SpleggEvents(), this);
         this.getServer().getPluginManager().registerEvents(new SignListener(), this);
         this.getServer().getPluginManager().registerEvents(new Listeners(), this);
         this.getCommand("splegg").setExecutor(new SpleggCommand());
         if (!this.setupEconomy()) {
            this.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", this.getDescription().getName()));
            this.getServer().getPluginManager().disablePlugin(this);
         } else {
            for(Player p : Bukkit.getOnlinePlayers()) {
            	 UtilPlayer u = new UtilPlayer(p);
                 this.pm.PLAYERS.put(p.getName(), u);
            }
         }
      }
   }

   public void onDisable() {
      this.disabling = true;
      HandlerList.unregisterAll(new MapListener(this));
      HandlerList.unregisterAll(new PlayerListener());
      HandlerList.unregisterAll(new SpleggEvents());
      HandlerList.unregisterAll(new SignListener());
      HandlerList.unregisterAll(new Listeners());
      Iterator var2 = this.games.GAMES.values().iterator();

      while(var2.hasNext()) {
         Game game = (Game)var2.next();
         if (game.getStatus() == Status.INGAME) {
            this.game.stopGame(game, 1);
         }
      }

   }

   public WorldEditPlugin getWorldEdit() {
      Plugin worldEdit = this.getServer().getPluginManager().getPlugin("WorldEdit");
      return worldEdit instanceof WorldEditPlugin ? (WorldEditPlugin)worldEdit : null;
   }
}
