package me.mrluangamer.managers;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class Rollback {
   private String world;
   private Material previd;
   private Material newid;
   private BlockData prevdata;
   private BlockData newdata;
   private int x;
   private int y;
   private int z;

   public Rollback(String world, Material previd, BlockData prevdata, int x, int y, int z) {
      this.world = world;
      this.previd = previd;
      this.prevdata = prevdata;
      this.newid = Material.AIR;
      this.newdata = null;
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public String getWorld() {
      return this.world;
   }

   public BlockData getPrevdata() {
      return this.prevdata;
   }

   public BlockData getNewdata() {
      return this.newdata;
   }

   public Material getPrevid() {
      return this.previd;
   }

   public Material getNewid() {
      return this.newid;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getZ() {
      return this.z;
   }
}
