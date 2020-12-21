package me.mrluangamer.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import me.mrluangamer.Splegg;
import me.mrluangamer.managers.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

public class Utils {
   Splegg splegg;
   private String prefix = Splegg.getSplegg().getConfig().getString("Messages.Prefix").replaceAll("&", "§");
   public HashMap PLAYERS = new HashMap();
   public FileConfiguration spawns;
   private File f;

   public String getPrefix() {
      return this.prefix;
   }

   public void log(String s) {
      Bukkit.getConsoleSender().sendMessage(this.prefix + s);
   }

   public void sendMessage(Player player, String s) {
      player.sendMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', s));
   }

   public void bc(String s) {
      Bukkit.broadcastMessage(this.prefix + ChatColor.translateAlternateColorCodes('&', s));
   }

   public void bc(String string, Game game) {
      Iterator var4 = Splegg.getSplegg().pm.PLAYERS.values().iterator();

      while(var4.hasNext()) {
         UtilPlayer u = (UtilPlayer)var4.next();
         if (u.getGame() == game && u.isAlive()) {
            this.sendMessage(u.getPlayer(), string);
         }
      }

   }

   public void bcNotForPlayer(Player player, String string, Game game) {
      Iterator var5 = Splegg.getSplegg().pm.PLAYERS.values().iterator();

      while(var5.hasNext()) {
         UtilPlayer u = (UtilPlayer)var5.next();
         if (u.getGame() == game && u.isAlive() && u.getPlayer() != player) {
            this.sendMessage(u.getPlayer(), string);
         }
      }

   }

   public Utils(Splegg s) {
      this.splegg = s;
   }

   public void setup() {
      this.f = new File(this.splegg.getDataFolder(), "spawns.yml");

      try {
         if (!this.f.exists()) {
            this.f.createNewFile();
         }
      } catch (Exception var2) {
      }

      this.reloadSpawns();
      this.saveSpawns();
      this.reloadSpawns();
   }

   private void reloadSpawns() {
      this.spawns = YamlConfiguration.loadConfiguration(this.f);
   }

   private void saveSpawns() {
      try {
         this.spawns.save(this.f);
      } catch (IOException var2) {
      }

   }

   public void setLobby(Location l) {
      int x = l.getBlockX();
      int y = l.getBlockY();
      int z = l.getBlockZ();
      float yaw = l.getYaw();
      float pitch = l.getPitch();
      String worldName = l.getWorld().getName();
      this.spawns.set("Spawns.lobby.world", worldName);
      this.spawns.set("Spawns.lobby.x", x);
      this.spawns.set("Spawns.lobby.y", y);
      this.spawns.set("Spawns.lobby.z", z);
      this.spawns.set("Spawns.lobby.pitch", pitch);
      this.spawns.set("Spawns.lobby.yaw", yaw);
      this.saveSpawns();
   }

   public Location getLobby() {
      int x = this.spawns.getInt("Spawns.lobby.x");
      int y = this.spawns.getInt("Spawns.lobby.y");
      int z = this.spawns.getInt("Spawns.lobby.z");
      float yaw = (float)this.spawns.getInt("Spawns.lobby.yaw");
      float pitch = (float)this.spawns.getInt("Spawns.lobby.pitch");
      World worldName = Bukkit.getWorld(this.spawns.getString("Spawns.lobby.world"));
      return new Location(worldName, (double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, yaw, pitch);
   }

   public UtilPlayer getPlayer(String name) {
      return (UtilPlayer)this.PLAYERS.get(name);
   }

   public UtilPlayer getPlayer(Player player) {
      return (UtilPlayer)this.PLAYERS.get(player.getName());
   }

   public void clearInventory(Player player) {
      PlayerInventory pInv = player.getInventory();
      pInv.setArmorContents((ItemStack[])null);
      pInv.clear();
      player.setFireTicks(0);
      this.clearPotions(player);
   }

   public void clearPotions(Player player) {
      Iterator var3 = player.getActivePotionEffects().iterator();

      while(var3.hasNext()) {
         PotionEffect effect = (PotionEffect)var3.next();
         player.removePotionEffect(effect.getType());
      }

   }

   public static ItemStack getItem(Material material, int id, String name, String lore) {
      ItemStack stack = new ItemStack(material, 1, (short)id);
      ItemMeta meta = stack.getItemMeta();
      meta.setDisplayName(name);
      meta.setLore(Arrays.asList(lore));
      stack.setItemMeta(meta);
      return stack;
   }
}
