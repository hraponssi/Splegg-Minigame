package me.mrluangamer.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;

import me.mrluangamer.Splegg;
import me.mrluangamer.config.Map;
import me.mrluangamer.managers.Game;
import me.mrluangamer.managers.Status;
import me.mrluangamer.utils.UtilPlayer;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpleggCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String tag, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			UtilPlayer u = Splegg.getSplegg().pm.getPlayer(player);
			if (args.length == 0) {
				Splegg.getSplegg().chat.sendMessage(player, "Plugin created by MrLuangamer, > Theluangamer9416 for more information /splegg help");
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("join")) {
					if (player.hasPermission("splegg.join")) {
						if (u.getGame() != null && u.isAlive()) {
							Splegg.getSplegg().chat.sendMessage(player, "&cYou are already playing.");
						} else if (this.lobbyset()) {
							player.teleport(Splegg.getSplegg().config.getLobby());
						} else {
							Splegg.getSplegg().chat.sendMessage(player, "&cSplegg is incorrectly setup! Ask an admin to set the lobby.");
						}
					} else {
						Splegg.getSplegg().chat.sendMessage(player, "&cYou do not have permission.");
					}
				} else if (args[0].equalsIgnoreCase("leave")) {
					if (u.getGame() != null && u.isAlive()) {
						Game game = u.getGame();
						game.leaveGame(u);
						Splegg.getSplegg().chat.sendMessage(player, Splegg.getSplegg().getConfig().getString("Messages.LeaveGame").replaceAll("&", "§").replaceAll("%map%", game.getMap().getName()));
					}
				} else if (args[0].equalsIgnoreCase("start")) {
					if (player.hasPermission("splegg.admin")) {
						if (u.getGame() == null) {
							Splegg.getSplegg().chat.sendMessage(player, "&cYou are not in a game.");
						} else if (u.getGame().getStatus() == Status.LOBBY) {
							Splegg.getSplegg().game.startGame(u.getGame());
							Splegg.getSplegg().chat.sendMessage(player, "&eGame started!");
						} else if (u.getGame().getStatus() == Status.INGAME) {
							Splegg.getSplegg().chat.sendMessage(player, "§cGame has already begun.");
						}
					} else {
						Splegg.getSplegg().chat.sendMessage(player, "&cYou do not have permission.");
					}
				} else if (args[0].equalsIgnoreCase("stop")) {
					if (player.hasPermission("splegg.admin")) {
						if (u.getGame() == null) {
							Splegg.getSplegg().chat.sendMessage(player, "&cYou are not in a game.");
						} else if (u.getGame().getStatus() == Status.LOBBY) {
							Splegg.getSplegg().chat.sendMessage(player, "&cGame has not begun yet!");
						} else if (u.getGame().getStatus() == Status.INGAME) {
							Splegg.getSplegg().chat.bc("&5" + player.getName() + "&6 has stopped the game.", u.getGame());
							Splegg.getSplegg().game.stopGame(u.getGame(), 1);
							Splegg.getSplegg().chat.sendMessage(player, "§eYou have stopped the game.");
						}
					} else {
						Splegg.getSplegg().chat.sendMessage(player, "&cYou do not have permission.");
					}
				} else if (args[0].equalsIgnoreCase("setlobby")) {
					if (player.hasPermission("splegg.admin")) {
						Splegg.getSplegg().config.setLobby(player.getLocation());
						Splegg.getSplegg().chat.sendMessage(player, "You have set splegg lobby.");
					} else {
						Splegg.getSplegg().chat.sendMessage(player, "&cYou do not have permission.");
					}
				} else if (!args[0].equalsIgnoreCase("")) {
					Splegg.getSplegg().chat.sendMessage(player, "&cIncorrect Usage: &6/" + tag + " <join,leave,stop,start,setlobby,help>");
				}
			} else {
				Map map;
				String name;
				if (args.length == 2) {
					if (args[0].equalsIgnoreCase("create")) {
						if (player.hasPermission("splegg.admin")) {
							name = args[1];
							if (Splegg.getSplegg().maps.mapExists(name)) {
								Splegg.getSplegg().chat.sendMessage(player, "Map already exists.");
							} else {
								Splegg.getSplegg().maps.c.addMap(name);
								Splegg.getSplegg().maps.addMap(name);
								map = Splegg.getSplegg().maps.getMap(name);
								Game game = new Game(Splegg.getSplegg(), map);
								Splegg.getSplegg().games.addGame(map.getName(), game);
								if (!map.isUsable()) {
									game.setStatus(Status.DISABLED);
								}

								Splegg.getSplegg().chat.sendMessage(player, "Map has been added and created &c" + name + "&6.");
							}
						} else {
							Splegg.getSplegg().chat.sendMessage(player, "&cYou do not have permission.");
						}
					} else if (args[0].equalsIgnoreCase("delete")) {
						if (player.hasPermission("splegg.admin")) {
							name = args[1];
							if (Splegg.getSplegg().maps.mapExists(name)) {
								Splegg.getSplegg().maps.deleteMap(name);
								Splegg.getSplegg().chat.sendMessage(player, "Map has been deleted.");
							} else {
								Splegg.getSplegg().chat.sendMessage(player, "&cMap does not exist.");
							}
						} else {
							Splegg.getSplegg().chat.sendMessage(player, "&cYou do not have permission.");
						}
					} else {
						Game game;
						if (args[0].equalsIgnoreCase("start")) {
							if (player.hasPermission("splegg.admin")) {
								name = args[1];
								if (Splegg.getSplegg().maps.mapExists(name)) {
									game = Splegg.getSplegg().games.getGame(name);
									if (game == null) {
										Splegg.getSplegg().chat.sendMessage(player, "&cYou are not in a game.");
									} else if (game.getStatus() == Status.LOBBY) {
										Splegg.getSplegg().game.startGame(game);
										Splegg.getSplegg().chat.sendMessage(player, "&eStarting " + name + ".");
									} else if (game.getStatus() == Status.INGAME) {
										Splegg.getSplegg().chat.sendMessage(player, "§cGame has already begun.");
									}
								} else {
									Splegg.getSplegg().chat.sendMessage(player, "&cMap does not exist.");
								}
							} else {
								Splegg.getSplegg().chat.sendMessage(player, "&cYou do not have permission.");
							}
						} else if (args[0].equalsIgnoreCase("stop")) {
							if (player.hasPermission("splegg.admin")) {
								name = args[1];
								if (Splegg.getSplegg().maps.mapExists(name)) {
									game = Splegg.getSplegg().games.getGame(name);
									if (game == null) {
										Splegg.getSplegg().chat.sendMessage(player, "&cYou are not in a game.");
									} else if (game.getStatus() == Status.LOBBY) {
										Splegg.getSplegg().chat.sendMessage(player, "&cGame has not begun yet!");
									} else if (game.getStatus() == Status.INGAME) {
										Splegg.getSplegg().chat.bc("&5" + player.getName() + "&6 has stopped the game.", game);
										Splegg.getSplegg().game.stopGame(game, 1);
										Splegg.getSplegg().chat.sendMessage(player, "§eYou have stopped the game.");
									}
								} else {
									Splegg.getSplegg().chat.sendMessage(player, "&cMap does not exist.");
								}
							} else {
								Splegg.getSplegg().chat.sendMessage(player, "&cYou do not have permission.");
							}
						} else if (args[0].equalsIgnoreCase("setlobby")) {
							if (player.hasPermission("splegg.admin")) {
								name = args[1];
								if (Splegg.getSplegg().maps.mapExists(name)) {
									Splegg.getSplegg().maps.getMap(name).setLobby(player.getLocation());
									Splegg.getSplegg().chat.sendMessage(player, "Lobby for map &e" + name + "&6 set.");
								} else {
									Splegg.getSplegg().chat.sendMessage(player, "&cMap does not exist.");
								}
							} else {
								Splegg.getSplegg().chat.sendMessage(player, "&cYou do not have permission.");
							}
						} else if (args[0].equalsIgnoreCase("addfloor")) {
							if (player.hasPermission("splegg.admin")) {
								name = args[1];
								if (Splegg.getSplegg().maps.mapExists(name)) {
									map = Splegg.getSplegg().maps.getMap(name);
									WorldEditPlugin we = Splegg.getSplegg().getWorldEdit();
									Region sel = null;
									try {
										sel = we.getSession(player).getSelection(new BukkitWorld(player.getWorld()));
									} catch (IncompleteRegionException e) {
										// TODO Auto-generated catch block
										//e.printStackTrace();
									}
									if (sel == null) {
										Splegg.getSplegg().chat.sendMessage(player, "&5Please select an area with worldedit.");
									} else {
										map.addFloor(new Location(player.getWorld(), sel.getMinimumPoint().getBlockX(), sel.getMinimumPoint().getBlockY(), sel.getMinimumPoint().getBlockZ()), new Location(player.getWorld(), sel.getMaximumPoint().getBlockX(), sel.getMaximumPoint().getBlockY(), sel.getMaximumPoint().getBlockZ()));
										Splegg.getSplegg().chat.sendMessage(player, "Floor " + map.getFloors() + " added to map " + map.getName() + ".");
									}
								} else {
									Splegg.getSplegg().chat.sendMessage(player, "&cMap does not exist.");
								}
							} else {
								Splegg.getSplegg().chat.sendMessage(player, "&cYou do not have permission.");
							}
						} else if (args[0].equalsIgnoreCase("join")) {
							if (player.hasPermission("splegg.join")) {
								if (u.getGame() != null && u.isAlive()) {
									Splegg.getSplegg().chat.sendMessage(player, "&cYou are already playing.");
								} else {
									name = args[1];
									if (Splegg.getSplegg().maps.mapExists(name)) {
										game = Splegg.getSplegg().games.getGame(name);
										if (game != null && Splegg.getSplegg().maps.getMap(name).isUsable()) {
											game.joinGame(u);
										} else {
											Splegg.getSplegg().chat.sendMessage(player, "This map is incorrectly setup - See console for detailed output.");
										}
									} else {
										Splegg.getSplegg().chat.sendMessage(player, "&cMap does not exist.");
									}
								}
							} else {
								Splegg.getSplegg().chat.sendMessage(player, "&cYou do not have permission.");
							}
						} else if (args[0].equalsIgnoreCase("leave")) {
							Splegg.getSplegg().chat.sendMessage(player, "Please use &e/" + tag + " leave");
						} else if (args[0].equalsIgnoreCase("") && !args[1].equalsIgnoreCase("") && !args[1].equalsIgnoreCase("") && !args[1].equalsIgnoreCase("")) {
							Splegg.getSplegg().chat.sendMessage(player, "Usage: /" + tag + " help <player|mod|admin>");
						}
					}
				} else if (args.length == 3) {
					if (args[0].equalsIgnoreCase("setspawn")) {
						if (player.hasPermission("splegg.admin")) {
							name = args[1];
							if (Splegg.getSplegg().maps.mapExists(name)) {
								map = Splegg.getSplegg().maps.getMap(name);
								if (args[2].equalsIgnoreCase("next")) {
									map.addSpawn(player.getLocation());
									Splegg.getSplegg().chat.sendMessage(player, "Spawn &a" + map.getSpawnCount() + "&6 set for map &c" + map.getName() + "&6.");
								} else {
									try {
										int id = Integer.parseInt(args[2]);
										if (this.spawnset(id, map)) {
											map.setSpawn(id, player.getLocation());
											Splegg.getSplegg().chat.sendMessage(player, "You have re-set the spawn " + id + " for map " + name + ".");
										} else {
											Splegg.getSplegg().chat.sendMessage(player, "Please set the spawn using &e/" + tag + " setspawn <mapname> next &6then try this command again.");
										}
									} catch (NumberFormatException var11) {
										Splegg.getSplegg().chat.sendMessage(player, "&cPlease type a number.");
									}
								}
							} else {
								Splegg.getSplegg().chat.sendMessage(player, "&cMap does not exist!");
							}
						} else {
							Splegg.getSplegg().chat.sendMessage(player, "&cYou do not have permission.");
						}
					} else {
						Splegg.getSplegg().chat.sendMessage(player, "Usage: &a/" + tag + " setspawn <mapname> <next|spawnid>");
					}
				} else {
					Splegg.getSplegg().chat.sendMessage(player, "Incorrect Usage!");
				}
			}
		}

		return false;
	}

	boolean lobbyset() {
		try {
			Splegg.getSplegg().config.getLobby();
			return true;
		} catch (Exception var2) {
			return false;
		}
	}

	boolean spawnset(int i, Map map) {
		return map.getConfig().isString("Spawns." + i + ".world");
	}

	public void sendUsage(Player player, String tag, String usage, String def) {
		Splegg.getSplegg().chat.sendMessage(player, "&c/" + tag + " &d" + usage + " &5- &b" + def);
	}
}
