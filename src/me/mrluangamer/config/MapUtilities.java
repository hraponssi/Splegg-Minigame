package me.mrluangamer.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import me.mrluangamer.Splegg;
import me.mrluangamer.managers.Game;
import me.mrluangamer.managers.Status;

public class MapUtilities {
   public MapConfig c = new MapConfig();
   public HashMap MAPS = new HashMap();

   public void addMap(String name) {
      Map map = new Map(Splegg.getSplegg(), name);
      this.MAPS.put(name, map);
      map.load();
   }

   public void deleteMap(String name) {
      Map m = this.getMap(name);
      Game game = Splegg.getSplegg().games.getGame(name);
      if (game.getStatus() == Status.INGAME) {
         Splegg.getSplegg().game.stopGame(game, 0);
      }

      Splegg.getSplegg().games.GAMES.remove(m.getName());
      this.MAPS.remove(m);
      this.c.removeMap(name);
      m.delete();
   }

   public boolean mapExists(String name) {
      return this.MAPS.containsKey(name);
   }

   public Collection getMaps() {
      return this.MAPS.values();
   }

   public Map getMap(String name) {
      return (Map)this.MAPS.get(name);
   }

   private Map getRandomMap() {
      ArrayList all = new ArrayList();
      Iterator var3 = this.MAPS.values().iterator();

      while(var3.hasNext()) {
         Map map = (Map)var3.next();
         if (map.isUsable()) {
            all.add(map);
         }
      }

      Object[] mapsa = all.toArray();
      Map randomMap = (Map)mapsa[(new Random()).nextInt(mapsa.length)];
      return randomMap;
   }
}
