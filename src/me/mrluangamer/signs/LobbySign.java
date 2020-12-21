package me.mrluangamer.signs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import me.mrluangamer.Splegg;
import me.mrluangamer.config.Map;
import me.mrluangamer.managers.Game;
import me.mrluangamer.managers.Status;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;

public class LobbySign {
   Splegg splegg;
   Map map;

   public LobbySign(Map map, Splegg s) {
      this.splegg = s;
      this.map = map;
   }
   
	private static Collection<Material> signMaterials = new ArrayList<>();
	static {
		signMaterials.add(Material.ACACIA_SIGN);
		signMaterials.add(Material.DARK_OAK_SIGN);
		signMaterials.add(Material.OAK_SIGN);
		signMaterials.add(Material.BIRCH_SIGN);
		signMaterials.add(Material.SPRUCE_SIGN);
		signMaterials.add(Material.JUNGLE_SIGN);
		signMaterials.add(Material.CRIMSON_SIGN);
		signMaterials.add(Material.WARPED_SIGN);
		signMaterials.add(Material.ACACIA_WALL_SIGN);
		signMaterials.add(Material.DARK_OAK_WALL_SIGN);
		signMaterials.add(Material.OAK_WALL_SIGN);
		signMaterials.add(Material.BIRCH_WALL_SIGN);
		signMaterials.add(Material.SPRUCE_WALL_SIGN);
		signMaterials.add(Material.JUNGLE_WALL_SIGN);
		signMaterials.add(Material.CRIMSON_WALL_SIGN);
		signMaterials.add(Material.WARPED_WALL_SIGN);
	}

   public void create(Location location, final Map map) {
      String loc = LobbySignUtils.get().locationToString(location);
      this.splegg.maps.c.addSign(map.getName(), loc);
      if (this.map == null) {
         this.map = map;
      }

      Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.splegg, new Runnable() {
         public void run() {
            LobbySign.this.update(map, true);
         }
      }, 5L);
   }

   public void delete(Location location) {
      String loc = LobbySignUtils.get().locationToString(location);
      this.splegg.maps.c.delSign(this.map.getName(), loc);
      this.splegg.maps.c.saveMaps();
      this.map = null;
   }

   public void update(Map map, boolean force) {
      Iterator var4 = this.splegg.maps.c.maps.getStringList("Signs." + map.getName() + ".lobby").iterator();

      while(var4.hasNext()) {
         String loc = (String)var4.next();
         Location location = LobbySignUtils.get().stringToLocation(loc);
         if (!signMaterials.contains(location.getBlock().getType())) {
            location.getBlock().setType(Material.OAK_WALL_SIGN);
         }

         Sign s = (Sign)location.getBlock().getState();
         String[] sign;
         if (force) {
            String[] array = new String[]{Splegg.getSplegg().getConfig().getString("Sings.Restarting.1").replaceAll("&", "§").replaceAll("#", "â–ˆ"), Splegg.getSplegg().getConfig().getString("Sings.Restarting.2").replaceAll("&", "§"), Splegg.getSplegg().getConfig().getString("Sings.Restarting.3").replaceAll("&", "§").replaceAll("%map%", map.getName()), Splegg.getSplegg().getConfig().getString("Sings.Restarting.4").replaceAll("&", "§").replaceAll("#", "â–ˆ")};
            this.setSign(array, s);
            sign = new String[4];
            Game game = this.splegg.games.getGame(map.getName());
            if (game == null) {
               sign[0] = "";
               sign[1] = ChatColor.DARK_RED + "Please remove";
               sign[2] = ChatColor.DARK_RED + "this sign";
               sign[3] = "";
            } else {
               sign[0] = Splegg.getSplegg().getConfig().getString("Sings.Format.1").replaceAll("&", "§").replaceAll("%status%", this.getFancyStatus(game)).replaceAll("%map%", map.getName()).replaceAll("%count%/%maxcount%", this.getPlayers(game));
               sign[1] = Splegg.getSplegg().getConfig().getString("Sings.Format.2").replaceAll("&", "§").replaceAll("%status%", this.getFancyStatus(game)).replaceAll("%map%", map.getName()).replaceAll("%count%/%maxcount%", this.getPlayers(game));
               sign[2] = Splegg.getSplegg().getConfig().getString("Sings.Format.3").replaceAll("&", "§").replaceAll("%map%", map.getName()).replaceAll("%status%", this.getFancyStatus(game)).replaceAll("%count%/%maxcount%", this.getPlayers(game));
               sign[3] = Splegg.getSplegg().getConfig().getString("Sings.Format.4").replaceAll("&", "§").replaceAll("%count%/%maxcount%", this.getPlayers(game)).replaceAll("%status%", this.getFancyStatus(game)).replaceAll("%map%", map.getName());
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(this.splegg, new SignDelay(sign, s), 40L);
         } else {
            Game game = this.splegg.games.getGame(map.getName());
            sign = new String[]{Splegg.getSplegg().getConfig().getString("Sings.Format.1").replaceAll("&", "§").replaceAll("%status%", this.getFancyStatus(game)).replaceAll("%map%", map.getName()).replaceAll("%count%/%maxcount%", this.getPlayers(game)), Splegg.getSplegg().getConfig().getString("Sings.Format.2").replaceAll("&", "§").replaceAll("%status%", this.getFancyStatus(game)).replaceAll("%map%", map.getName()).replaceAll("%count%/%maxcount%", this.getPlayers(game)), Splegg.getSplegg().getConfig().getString("Sings.Format.3").replaceAll("&", "§").replaceAll("%map%", map.getName()).replaceAll("%status%", this.getFancyStatus(game)).replaceAll("%count%/%maxcount%", this.getPlayers(game)), Splegg.getSplegg().getConfig().getString("Sings.Format.4").replaceAll("&", "§").replaceAll("%count%/%maxcount%", this.getPlayers(game)).replaceAll("%status%", this.getFancyStatus(game)).replaceAll("%map%", map.getName())};
            this.setSign(sign, s);
         }
      }

   }

   private String getPlayers(Game game) {
      String players = "";
      if (game.getStatus() == Status.DISABLED) {
         players = "";
      } else if (game.getMap().getSpawnCount() <= 1) {
         players = "Players: " + game.getPlayers().size();
      } else {
         players = game.getPlayers().size() + "/" + game.getMap().getSpawnCount();
      }

      return players;
   }

   private void setSign(String[] lines, Sign s) {
      for(int i = 0; i < lines.length; ++i) {
         s.setLine(i, lines[i]);
      }

      s.update();
   }

   private String getFancyStatus(Game game) {
      String status = "";
      Status st = game.getStatus();
      if (st == Status.LOBBY) {
         if (game.isStarting()) {
            status = Splegg.getSplegg().getConfig().getString("Sings.Status.Join").replaceAll("&", "§");
         } else {
            status = Splegg.getSplegg().getConfig().getString("Sings.Status.Join").replaceAll("&", "§");
         }
      } else if (st == Status.DISABLED) {
         status = Splegg.getSplegg().getConfig().getString("Sings.Status.Disabled").replaceAll("&", "§");
      } else if (st == Status.INGAME) {
         status = Splegg.getSplegg().getConfig().getString("Sings.Status.Started").replaceAll("&", "§");
      } else {
         status = ChatColor.DARK_GREEN + st.toString().toLowerCase();
      }

      return status;
   }
}
