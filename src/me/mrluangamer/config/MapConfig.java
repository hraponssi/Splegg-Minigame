package me.mrluangamer.config;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import me.mrluangamer.Splegg;
import me.mrluangamer.managers.Game;
import me.mrluangamer.managers.Status;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MapConfig {
   public FileConfiguration maps;
   public File f;

   public void setup() {
      this.f = new File(Splegg.getSplegg().getDataFolder(), "maps.yml");

      try {
         if (!this.f.exists()) {
            this.f.createNewFile();
         }
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      this.loadMaps();
      this.saveMaps();
      Splegg.getSplegg().maps.MAPS.clear();
      Iterator var2 = this.getEnabledMaps().iterator();

      while(var2.hasNext()) {
         String maps = (String)var2.next();
         Splegg.getSplegg().maps.addMap(maps);
         Map map = Splegg.getSplegg().maps.getMap(maps);
         Game game = new Game(Splegg.getSplegg(), map);
         Splegg.getSplegg().games.addGame(map.getName(), game);
         if (!map.isUsable()) {
            game.setStatus(Status.DISABLED);
         }
      }

   }

   private void loadMaps() {
      this.maps = YamlConfiguration.loadConfiguration(this.f);
   }

   public void saveMaps() {
      try {
         this.maps.save(this.f);
      } catch (IOException var2) {
      }

   }

   public List getEnabledMaps() {
      return this.maps.getStringList("maps");
   }

   public void addSign(String map, String loc) {
      List signs = this.maps.getStringList("Signs." + map + ".lobby");
      signs.add(loc);
      this.maps.set("Signs." + map + ".lobby", signs);
      this.saveMaps();
   }

   public void delSign(String map, String loc) {
      List signs = this.maps.getStringList("Signs." + map + ".lobby");
      signs.remove(loc);
      this.maps.set("Signs." + map + ".lobby", signs);
      this.saveMaps();
   }

   public void addMap(String name) {
      List maps = this.maps.getStringList("maps");
      maps.add(name);
      this.maps.set("maps", maps);
      this.saveMaps();
   }

   public void removeMap(String name) {
      List maps = this.maps.getStringList("maps");
      maps.remove(name);
      this.maps.set("maps", maps);
      this.saveMaps();
   }
}
