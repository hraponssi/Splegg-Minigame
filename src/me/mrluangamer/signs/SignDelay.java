package me.mrluangamer.signs;

import org.bukkit.block.Sign;

public class SignDelay implements Runnable {
   String[] data;
   Sign sign;

   public SignDelay(String[] data, Sign sign) {
      this.data = data;
      this.sign = sign;
   }

   public void run() {
      for(int i = 0; i < this.data.length; ++i) {
         this.sign.setLine(i, this.data[i]);
      }

      this.sign.update();
   }
}
