package musaddict.golfcraft;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;



public class GcSigns implements Listener {

	public Golfcraft plugin;


	public GcSigns(Golfcraft plugin) {
		this.plugin = plugin;
	}


	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.getBlock() != null) { // Will prevent an NPE if the user right-clicks air.
			if (event.getBlock().getState() != null) {
				if (event.getBlock().getState() instanceof Sign) {
					Sign sign = null;
					try {
						sign = (Sign) event.getBlock();
					}
					catch (ClassCastException | NullPointerException e) {
						return;
					}
					String [] lines = sign.getLines();
					Player player = event.getPlayer();
					if (lines[0].equalsIgnoreCase(ChatColor.WHITE + "   [" + ChatColor.DARK_GREEN + "Golf" + ChatColor.WHITE + "]")) {
						if (GcFiles.holeExists(player, lines[2])) {
							if (!player.isOp() && !player.hasPermission("golf.sign")) {
								player.sendMessage(ChatColor.DARK_RED + "You do not have permission to remove golf signs.");
								event.setCancelled(true);
								final Location bl = event.getBlock().getLocation();
								plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

									public void run() {
										bl.getBlock().getState().update();
									}
								}, 20L);
							}
							else {
								player.sendMessage(ChatColor.GRAY + "You have removed a golf play sign");
							}
						}
					}
				}
			}
		}
	}


	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		if (event.getBlockAgainst() == null) { // Will prevent an NPE if the user right-clicks air.
			return;
		}

		if (event.getBlockAgainst().getState() == null) {
			return;
		}

		if (event.getBlockAgainst().getState() instanceof Sign) {
			Sign sign = (Sign) event.getBlockAgainst().getState();

			if (ChatColor.stripColor((sign.getLine(0))).equalsIgnoreCase("[golf]")) {
				event.setCancelled(true);
			}
		}
	}


	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		Player player = event.getPlayer();
		// Sign sign = (Sign)event.getBlock().getState();
		String [] lines = event.getLines();

		if (lines[0].equalsIgnoreCase("golf") || lines[0].equalsIgnoreCase("[golf]")) {

			if (player.isOp() || player.hasPermission("golf.sign")) {

				String hole = lines[2];

				if (GcFiles.holeExists(player, hole)) {
					event.setLine(0, ChatColor.WHITE + "   [" + ChatColor.DARK_GREEN + "Golf" + ChatColor.WHITE + "]");
					event.setLine(1, "Start:");
					event.setLine(2, hole);
					event.setLine(3, "");
					player.sendMessage(ChatColor.GREEN + "Successfully created a play sign!");
				}
				else {
					event.setLine(0, ChatColor.DARK_GRAY + "   [" + ChatColor.DARK_RED + "Golf" + ChatColor.DARK_GRAY + "]");
					event.setLine(1, ChatColor.DARK_GRAY + "This hole...");
					event.setLine(2, ChatColor.DARK_GRAY + "Is a lie!!!");
					event.setLine(3, "");
				}
			}
			else {
				event.setLine(0, ChatColor.DARK_GRAY + "   [" + ChatColor.DARK_RED + "Golf" + ChatColor.DARK_GRAY + "]");
				event.setLine(1, ChatColor.DARK_GRAY + "You can not");
				event.setLine(2, ChatColor.DARK_GRAY + "create golf");
				event.setLine(3, ChatColor.DARK_GRAY + "play signs!");
			}
		}
	}


	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (event.getClickedBlock() == null) { // Will prevent an NPE if the user right-clicks air.
			return;
		}

		if (event.getClickedBlock().getState() == null) {
			return;
		}

		if (event.getClickedBlock().getState() instanceof Sign) {
			final Player player = event.getPlayer();

			Sign sign = (Sign) event.getClickedBlock().getState();
			String [] lines = sign.getLines();

			String signLine1 = lines[0];

			if (signLine1.equalsIgnoreCase(ChatColor.WHITE + "   [" + ChatColor.DARK_GREEN + "Golf" + ChatColor.WHITE + "]")) {

				if (player.isOp() || player.hasPermission("golf.play")) {

					String hole = ChatColor.stripColor(lines[2]);

					// Begin processing the sign key purchase
					if (GcFiles.holeExists(player, hole)) {
						GcHole existingHole = GcFiles.getHole(player.getWorld().getName(), hole);
						if (GcEntityListener.signExists.containsKey(player)) {
							if (GcEntityListener.signExists.get(player)) {
								if (GcEntityListener.signLocation.containsKey(player)) {
									GcEntityListener.signLocation.get(player).getBlock().setType(Material.AIR);
									GcEntityListener.signLocation.remove(player);
								}
							}
							GcEntityListener.signExists.remove(player);
						}
						Location tpHoleLoc = GcFiles.getHole(player.getWorld().getName(), hole).getBlock().getLocation();
						player.teleport(tpHoleLoc);

						if (GcCommands.Golfing.containsKey(player)) {
							if (!GcCommands.Golfing.get(player)) {
								GcCommands.Golfing.put(player, true);
								player.sendMessage(ChatColor.GOLD + "You are now golfing!");
							}
						}
						else {
							GcCommands.Golfing.put(player, true);
							player.sendMessage(ChatColor.GOLD + "You are now golfing!");
						}

						if (GcEntityListener.finishedHole.containsKey(player)) {
							if (GcEntityListener.finishedHole.get(player)) {
								GcEntityListener.finishedHole.put(player, false);
							}
						}
						else {
							GcEntityListener.finishedHole.put(player, false);
						}

						if (!(hole == GcCommands.playingHole.get(player))) {
							Player [] list = Bukkit.getOnlinePlayers();
							for (Player players : list) {

								if (GcCommands.Golfing.containsKey(players)) {
									if (GcCommands.Golfing.get(players)) {

										players.sendMessage(ChatColor.DARK_GREEN + "" + player.getName() + ChatColor.GRAY + " has begun " + ChatColor.GOLD + hole + " par: " + existingHole.par);

									}
								}
							}
						}
						else {
							player.sendMessage(ChatColor.GRAY + "You have restarted " + ChatColor.GOLD + hole + " par: " + existingHole.par);
						}

						while (player.getInventory().contains(Material.ARROW)) {
							player.getInventory().remove(Material.ARROW);
						}
						ItemStack arrow = new ItemStack(Material.ARROW);
						arrow.setAmount(1);
						player.getInventory().addItem(arrow);
						while (player.getInventory().contains(Material.BOW)) {
							player.getInventory().remove(Material.BOW);
						}
						ItemStack bow = new ItemStack(Material.BOW);
						arrow.setAmount(1);
						player.getInventory().addItem(bow);
						GcEntityListener.score.put(player, 0);
						GcCommands.playingHole.put(player, hole);
						player.updateInventory();

					}
					else {
						player.sendMessage(ChatColor.GOLD + "[CK] " + ChatColor.RED + "That hole no longer exists!");
					}

				}
			}
		}
	}

}
