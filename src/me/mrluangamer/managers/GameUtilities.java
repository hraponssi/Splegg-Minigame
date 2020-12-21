package me.mrluangamer.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import me.mrluangamer.utils.SpleggPlayer;
import org.bukkit.entity.Player;

public class GameUtilities {
   public HashMap GAMES = new HashMap();

   public Game getGame(String map) {
      return (Game)this.GAMES.get(map);
   }

   public void addGame(String map, Game game) {
      this.GAMES.put(map, game);
   }

   public SpleggPlayer getPlayer(Player player) {
      if (player == null) {
         return null;
      } else {
         SpleggPlayer sp = null;
         Iterator var4 = this.GAMES.values().iterator();

         while(var4.hasNext()) {
            Game games = (Game)var4.next();
            Iterator var6 = games.players.values().iterator();

            while(var6.hasNext()) {
               SpleggPlayer sps = (SpleggPlayer)var6.next();
               if (sps.getPlayer().getName().equalsIgnoreCase(player.getName())) {
                  sp = sps;
               }
            }
         }

         return sp;
      }
   }

   public Game getMatchedGame(Player player) {
      Game game = null;
      Iterator var4 = this.GAMES.values().iterator();

      while(var4.hasNext()) {
         Game g = (Game)var4.next();
         if (g.players.containsKey(player.getName())) {
            game = g;
         }
      }

      return game;
   }

   public int howManyOpenGames() {
      ArrayList all = new ArrayList();
      Iterator var3 = this.GAMES.values().iterator();

      while(var3.hasNext()) {
         Game games = (Game)var3.next();
         if (games.getStatus() == Status.LOBBY) {
            all.add(games);
         }
      }

      return all.size();
   }

   public void checkWinner(Game game) {
      if (game.players.size() <= 1) {
         if (game.players.size() == 0) {
            game.splegg.game.stopGame(game, 0);
         } else {
            String w = "";
            Iterator var4 = game.players.values().iterator();

            while(var4.hasNext()) {
               SpleggPlayer sp = (SpleggPlayer)var4.next();
               w = sp.getPlayer().getName();
               game.leaveGame(sp.getUtilPlayer());
            }

            game.splegg.game.stopGame(game, 5);
         }
      }

   }
}
