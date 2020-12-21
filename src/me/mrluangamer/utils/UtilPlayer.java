package me.mrluangamer.utils;

import me.mrluangamer.managers.Game;
import org.bukkit.entity.Player;

public class UtilPlayer {
   Player player;
   String name;
   boolean alive;
   Game game;
   InvStore store;

   public UtilPlayer(Player player) {
      this.player = player;
      this.game = null;
      this.name = player.getName();
      this.alive = false;
      this.store = new InvStore(player);
   }

   public InvStore getStore() {
      return this.store;
   }

   public Player getPlayer() {
      return this.player;
   }

   public String getName() {
      return this.name;
   }

   public boolean isAlive() {
      return this.alive;
   }

   public void setAlive(boolean a) {
      this.alive = a;
   }

   public Game getGame() {
      return this.game;
   }

   public void setGame(Game game) {
      this.game = game;
   }
}
