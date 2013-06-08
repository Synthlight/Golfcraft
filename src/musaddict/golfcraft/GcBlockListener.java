package musaddict.golfcraft;


import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;



public class GcBlockListener implements Listener {

	public static HashMap <Player, Location> signLocation = new HashMap <Player, Location>();


	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockDamage(BlockDamageEvent event) {
		onBlockDamage_Func(event);
	}


	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event) {
		onBlockBreak_Func(event);
	}


	/**
	 * This method instantly deletes valid ball marking golf player signs. If the player removed it,
	 * then they can continue golfing. If a player with "golfcraft.ref" removes the sign, then the
	 * player must exit/enter golf mode to restart.
	 */
	private void onBlockBreak_Func(BlockBreakEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();

		if (block.getState() instanceof Sign) { // It's a sign
			Sign sign = (Sign) (block.getState());
			String [] lines = sign.getLines();

			if (lines[0].equals(ChatColor.WHITE + "   [" + ChatColor.DARK_GREEN + "Golf" + ChatColor.WHITE + "]")) {
				if (lines[1].equals(ChatColor.DARK_GRAY + player.getName())) {
					if (GcCommands.Golfing.containsKey(player)) {
						if (GcCommands.Golfing.get(player)) {
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
							GcEntityListener.signExists.put(player, false);
							signLocation.put(player, block.getLocation());
							block.setType(Material.AIR);
							player.sendMessage(ChatColor.GRAY + "You may now continue golfing.");
						}
					}
				}
				else if (!(lines[1] == "Start:")) {
					if (player.hasPermission("golf.ref") || player.isOp()) {
						String signPlayer = ChatColor.stripColor(lines[1]);
						player.sendMessage(ChatColor.GRAY + "You have removed " + ChatColor.GREEN + signPlayer + ChatColor.GRAY + "'" + ChatColor.GREEN + "s " + ChatColor.GRAY + "sign.");

						Player [] list = Bukkit.getOnlinePlayers();
						for (Player players : list) {
							if (players.getName().equals(signPlayer)) {
								GcEntityListener.signExists.put(players, false);
								block.setType(Material.AIR);
							}
						}
					}
					else {
						event.setCancelled(true);
						block.getState().update(true);
					}
				}
			}
		}
	}


	/**
	 * Identical as the above method, but the above method exists to account for creative mode. If a
	 * player is in survival, and they so much as to begin to damage a ball marking sign, then it is
	 * removed instantly (instead of waiting until the player breaks it)
	 */
	private void onBlockDamage_Func(BlockDamageEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();

		if (block.getState() instanceof Sign) { // It's a sign
			Sign sign = (Sign) (block.getState());
			String [] lines = sign.getLines();

			if (lines[0].equals(ChatColor.WHITE + "   [" + ChatColor.DARK_GREEN + "Golf" + ChatColor.WHITE + "]")) {
				if (lines[1].equals(ChatColor.DARK_GRAY + player.getName())) {
					if (GcCommands.Golfing.containsKey(player)) {
						if (GcCommands.Golfing.get(player)) {
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
							GcEntityListener.signExists.put(player, false);
							signLocation.put(player, block.getLocation());
							block.setType(Material.AIR);
							player.sendMessage(ChatColor.GRAY + "You may now continue golfing.");
						}
					}
				}
				else if (!(lines[1] == "Start:")) {
					if (player.hasPermission("golf.ref") || player.isOp()) {
						String signPlayer = ChatColor.stripColor(lines[1]);
						player.sendMessage(ChatColor.GRAY + "You have removed " + ChatColor.GREEN + signPlayer + ChatColor.GRAY + "'" + ChatColor.GREEN + "s " + ChatColor.GRAY + "sign.");

						Player [] list = Bukkit.getOnlinePlayers();
						for (Player players : list) {
							if (players.getName().equals(signPlayer)) {
								GcEntityListener.signExists.put(players, false);
								block.setType(Material.AIR);
							}
						}
					}
					else {
						event.setCancelled(true);
						block.getState().update(true);
					}
				}
			}
		}
	}
}
