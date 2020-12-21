package me.mrluangamer.events;

import java.util.ArrayList;
import me.mrluangamer.Splegg;
import me.mrluangamer.utils.UtilPlayer;
import me.mrluangamer.utils.Utils;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class Listeners implements Listener {
   public static ArrayList manager = new ArrayList();
   public static ArrayList moneymanager = new ArrayList();
   public static ArrayList shopmanager = new ArrayList();
   public static ArrayList diamondspade = new ArrayList();
   public static ArrayList goldspade = new ArrayList();
   public static ArrayList launchEggs = new ArrayList();

   @EventHandler
   public void onInventoryClick(InventoryClickEvent event) {
      Player player = (Player)event.getWhoClicked();
      UtilPlayer u = Splegg.getSplegg().pm.getPlayer(player);
      ItemStack stack = event.getCurrentItem();
      if (event.getView().getTitle().equals(Splegg.getSplegg().getConfig().getString("GUI.Shop.Title"))) {
         if (shopmanager.contains(player.getName())) {
            EconomyResponse r;
            if (stack.getType() == Material.GOLDEN_SHOVEL) {
               player.closeInventory();
               if (Splegg.getSplegg().econ.getBalance(player.getName()) >= (double)Splegg.getSplegg().getConfig().getInt("GUI.Shop.GoldShovel.Price")) {
                  r = Splegg.getSplegg().econ.withdrawPlayer(player.getName(), (double)Splegg.getSplegg().getConfig().getInt("GUI.Shop.GoldShovel.Price"));
                  if (r.transactionSuccess()) {
                     goldspade.add(player.getName());
                     diamondspade.remove(player.getName());
                     shopmanager.remove(player.getName());
                     manager.remove(player.getName());
                     Splegg.getSplegg().chat.sendMessage(player, Splegg.getSplegg().getConfig().getString("Messages.BuyGoldShovel").replaceAll("&", "§"));
                  } else {
                     player.sendMessage(String.format("An error occured: %s", r.errorMessage));
                     shopmanager.add(player.getName());
                     manager.add(player.getName());
                     goldspade.remove(player.getName());
                     diamondspade.remove(player.getName());
                  }
               } else {
                  Splegg.getSplegg().chat.sendMessage(player, Splegg.getSplegg().getConfig().getString("Messages.NoEnoughMoney").replaceAll("&", "§"));
               }
            }

            if (stack.getType() == Material.DIAMOND_SHOVEL) {
               player.closeInventory();
               if (Splegg.getSplegg().econ.getBalance(player.getName()) >= (double)Splegg.getSplegg().getConfig().getInt("GUI.Shop.DiamondShovel.Price")) {
                  r = Splegg.getSplegg().econ.withdrawPlayer(player.getName(), (double)Splegg.getSplegg().getConfig().getInt("GUI.Shop.DiamondShovel.Price"));
                  if (r.transactionSuccess()) {
                     goldspade.remove(player.getName());
                     diamondspade.add(player.getName());
                     shopmanager.remove(player.getName());
                     manager.remove(player.getName());
                     Splegg.getSplegg().chat.sendMessage(player, Splegg.getSplegg().getConfig().getString("Messages.BuyDiamondShovel").replaceAll("&", "§"));
                  } else {
                     player.sendMessage(String.format("An error occured: %s", r.errorMessage));
                     shopmanager.add(player.getName());
                     manager.add(player.getName());
                     goldspade.remove(player.getName());
                     diamondspade.remove(player.getName());
                  }
               } else {
                  Splegg.getSplegg().chat.sendMessage(player, Splegg.getSplegg().getConfig().getString("Messages.NoEnoughMoney").replaceAll("&", "§"));
               }
            }
         } else {
            Splegg.getSplegg().chat.sendMessage(player, Splegg.getSplegg().getConfig().getString("Messages.Haveyoueverbought").replaceAll("&", "§"));
            player.closeInventory();
         }
      }

      if (u.getGame() != null && u.isAlive()) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onPlayerInteract(PlayerInteractEvent event) {
      Player player = event.getPlayer();
      ItemStack hand = player.getItemInHand();
      UtilPlayer u = Splegg.getSplegg().pm.getPlayer(player);
      if (event.getAction() == Action.RIGHT_CLICK_AIR) {
         if (hand.getType() == Material.IRON_SHOVEL && u.getGame() != null && u.isAlive() && launchEggs.contains(player.getName())) {
            player.launchProjectile(Egg.class);
            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0F, 1.0F);
         }

         Integer i;
         if (hand.getType() == Material.GOLDEN_SHOVEL && u.getGame() != null && u.isAlive() && launchEggs.contains(player.getName())) {
            for(i = 0; i < 2; i = i + 1) {
               player.launchProjectile(Egg.class);
            }

            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0F, 1.0F);
         }

         if (hand.getType() == Material.DIAMOND_SHOVEL && u.getGame() != null && u.isAlive() && launchEggs.contains(player.getName())) {
            for(i = 0; i < 3; i = i + 1) {
               player.launchProjectile(Egg.class);
            }

            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0F, 1.0F);
         }

         if (hand.getType() == Material.COMPASS && u.getGame() != null && u.isAlive()) {
            player.openInventory(this.getSpecInventory());
         }

         if (hand.getType() == Material.MAGMA_CREAM && u.getGame() != null && u.isAlive()) {
            u.getGame().leaveGame(u);
         }

         if (hand.getType() == Material.getMaterial(Splegg.getSplegg().getConfig().getString("Shop.Item")) && u.getGame() != null && u.isAlive()) {
            player.openInventory(this.getShopInventory());
         }
      }

   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event) {
      Player player = event.getPlayer();
      manager.remove(player.getName());
      shopmanager.remove(player.getName());
      goldspade.remove(player.getName());
      diamondspade.remove(player.getName());
      launchEggs.remove(player.getName());
      moneymanager.remove(player.getName());
   }

   @EventHandler
   public void onPlayerPickupItem(PlayerPickupItemEvent event) {
      Player player = event.getPlayer();
      UtilPlayer u = Splegg.getSplegg().pm.getPlayer(player);
      if (u.getGame() != null && u.isAlive()) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
   }

   private Inventory getShopInventory() {
      Inventory shop = Bukkit.createInventory((InventoryHolder)null, Splegg.getSplegg().getConfig().getInt("GUI.Shop.Size"), Splegg.getSplegg().getConfig().getString("GUI.Shop.Title"));
      shop.setItem(0, Utils.getItem(Material.GOLDEN_SHOVEL, 0, Splegg.getSplegg().getConfig().getString("GUI.Shop.GoldShovel.Name").replaceAll("&", "§"), Splegg.getSplegg().getConfig().getString("GUI.Shop.GoldShovel.Description").replaceAll("&", "§").replaceAll("%price%", String.valueOf(Splegg.getSplegg().getConfig().getInt("GUI.Shop.GoldShovel.Price")))));
      shop.setItem(1, Utils.getItem(Material.DIAMOND_SHOVEL, 0, Splegg.getSplegg().getConfig().getString("GUI.Shop.DiamondShovel.Name").replaceAll("&", "§"), Splegg.getSplegg().getConfig().getString("GUI.Shop.DiamondShovel.Description").replaceAll("&", "§").replaceAll("%price%", String.valueOf(Splegg.getSplegg().getConfig().getInt("GUI.Shop.DiamondShovel.Price")))));
      return shop;
   }

   private Inventory getSpecInventory() {
      Inventory spec = Bukkit.createInventory((InventoryHolder)null, 27, "Splegg - Spectators");
      return spec;
   }
}
