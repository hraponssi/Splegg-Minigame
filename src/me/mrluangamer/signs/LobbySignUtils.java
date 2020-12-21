package me.mrluangamer.signs;

import java.util.List;
import me.mrluangamer.Splegg;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LobbySignUtils {
   public static LobbySignUtils lsu = new LobbySignUtils();

   public static LobbySignUtils get() {
      return lsu;
   }

   public boolean isLobbySign(Location location, String name) {
      String locString = this.locationToString(location);
      List signLocs = Splegg.getSplegg().maps.c.maps.getStringList("Signs." + name + ".lobby");
      return signLocs.contains(locString);
   }

   public String locationToString(Location l) {
      String w = l.getWorld().getName();
      int x = l.getBlockX();
      int y = l.getBlockY();
      int z = l.getBlockZ();
      return w + "." + x + "." + y + "." + z;
   }

   public Location stringToLocation(String s) {
      String[] str = s.split("\\.");
      World w = Bukkit.getServer().getWorld(str[0]);
      int x = Integer.parseInt(str[1]);
      int y = Integer.parseInt(str[2]);
      int z = Integer.parseInt(str[3]);
      return new Location(w, (double)x, (double)y, (double)z);
   }

   public boolean compareLocations(Location one, Location two) {
      String w = one.getWorld().getName();
      int x = one.getBlockX();
      int y = one.getBlockY();
      int z = one.getBlockZ();
      String checkw = two.getWorld().getName();
      int checkx = two.getBlockX();
      int checky = two.getBlockY();
      int checkz = two.getBlockZ();
      return w.equalsIgnoreCase(checkw) && x == checkx && y == checky && z == checkz;
   }
}
