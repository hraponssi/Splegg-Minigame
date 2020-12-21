package me.mrluangamer.utils;

import org.bukkit.entity.Player;

public class SpleggPlayer {
   Player player;
   UtilPlayer u;
   int kills;
   int broken;

   public SpleggPlayer(Player player, UtilPlayer u) {
      this.player = player;
      this.u = u;
      this.kills = 0;
      this.broken = 0;
   }

   public UtilPlayer getUtilPlayer() {
      return this.u;
   }

   public Player getPlayer() {
      return this.player;
   }

   public int getKills() {
      return this.kills;
   }

   public void setKills(int i) {
      this.kills = i;
   }

   public int getBroken() {
      return this.broken;
   }
}
