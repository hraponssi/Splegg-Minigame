package me.mrluangamer.events;

import me.mrluangamer.Splegg;
import me.mrluangamer.signs.LobbySign;
import me.mrluangamer.signs.LobbySignUtils;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {
   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void signPlace(SignChangeEvent e) {
      Player player = e.getPlayer();
      if (e.getLine(0).equalsIgnoreCase("[splegg]") && e.getLine(1).equalsIgnoreCase("join") && player.hasPermission("splegg.admin")) {
         String map = e.getLine(2);
         if (Splegg.getSplegg().maps.mapExists(map)) {
            LobbySign ls = new LobbySign(Splegg.getSplegg().maps.getMap(map), Splegg.getSplegg());
            ls.create(e.getBlock().getLocation(), Splegg.getSplegg().maps.getMap(map));
            Splegg.getSplegg().chat.sendMessage(player, Splegg.getSplegg().getConfig().getString("Messages.CreateSign").replaceAll("&", "§").replaceAll("%map%", map));
         }
      }

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

   @EventHandler
   public void onInteract(PlayerInteractEvent e) {
      if (e.hasBlock() && signMaterials.contains(e.getClickedBlock().getType()) && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
         Sign s = (Sign)e.getClickedBlock().getState();
         Player player = e.getPlayer();
         if (s.getLine(0).equalsIgnoreCase(Splegg.getSplegg().getConfig().getString("Sings.Format.1").replaceAll("&", "§"))) {
            String map = ChatColor.stripColor(s.getLine(2));
            if (Splegg.getSplegg().maps.mapExists(map)) {
               player.chat("/splegg join " + map);
               player.updateInventory();
               e.setCancelled(true);
            } else {
               Splegg.getSplegg().chat.sendMessage(player, Splegg.getSplegg().getConfig().getString("Messages.Mapnotexist").replaceAll("&", "§"));
               e.setCancelled(true);
            }
         }
      }

   }

   @EventHandler
   public void signBreak(BlockBreakEvent e) {
      Player player = e.getPlayer();
      if(signMaterials.contains(e.getBlock().getType())) {
         Sign s = (Sign)e.getBlock().getState();
         String[] lines = s.getLines();
         String map = ChatColor.stripColor(lines[1]);
         if (LobbySignUtils.get().isLobbySign(e.getBlock().getLocation(), map)) {
            if (player.hasPermission("splegg.admin")) {
               LobbySign sign = new LobbySign(Splegg.getSplegg().maps.getMap(map), Splegg.getSplegg());
               sign.delete(e.getBlock().getLocation());
               Splegg.getSplegg().chat.sendMessage(player, Splegg.getSplegg().getConfig().getString("Messages.RemovedSign").replaceAll("&", "§").replaceAll("%map%", map));
            } else {
               e.setCancelled(true);
               Splegg.getSplegg().chat.sendMessage(player, Splegg.getSplegg().getConfig().getString("Messages.NotBreakSign").replaceAll("&", "§"));
            }
         }
      }

   }
}
