package me.mrluangamer.events;

import me.mrluangamer.Splegg;
import me.mrluangamer.utils.UtilPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

public class MapListener implements Listener {
   public Splegg splegg;

   public MapListener(Splegg s) {
      this.splegg = s;
   }

   @EventHandler
   public void blockBreak(BlockBreakEvent e) {
      Player player = e.getPlayer();
      UtilPlayer u = this.splegg.pm.getPlayer(player);
      if (u.getGame() != null && u.isAlive()) {
         e.setCancelled(true);
      }

   }

   @EventHandler
   public void blockPlace(BlockPlaceEvent e) {
      Player player = e.getPlayer();
      UtilPlayer u = this.splegg.pm.getPlayer(player);
      if (u.getGame() != null && u.isAlive()) {
         e.setCancelled(true);
      }

   }

   @EventHandler
   public void hangingEntityBreak(HangingBreakByEntityEvent e) {
      Player player = (Player)e.getRemover();
      UtilPlayer u = this.splegg.pm.getPlayer(player);
      if (u.getGame() != null && u.isAlive()) {
         e.setCancelled(true);
      }

   }
}
