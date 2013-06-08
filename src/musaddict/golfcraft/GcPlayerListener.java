package musaddict.golfcraft;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;



public class GcPlayerListener implements Listener {

	Golfcraft plugin;


	GcPlayerListener(Golfcraft plugin) {
		this.plugin = plugin;
	}


	public void onPlayerMove(PlayerMoveEvent event) {
		onPlayerMove_Func(event);
	}


	public void onPlayerInteract(PlayerInteractEvent event) {
		onPlayerInteract_Func(event);
	}


	@EventHandler(priority = EventPriority.HIGH)
	private void onPlayerInteract_Func(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		if (plugin.getConfig().getBoolean("enable-clubs")) {
			if (GcCommands.Golfing.containsKey(player)) {
				if (GcCommands.Golfing.get(player)) {
					if (event.getAction() == Action.LEFT_CLICK_AIR && player.getItemInHand().getType() == Material.BOW) {
						if (GcClubs.Club.containsKey(player)) {
							if (GcClubs.Club.get(player) < 11) {
								GcClubs.Club.put(player, GcClubs.Club.get(player) + 1);
							}
							else {
								GcClubs.Club.put(player, 0);
							}
						}
						else {
							GcClubs.Club.put(player, 0);
						}
						player.sendMessage(GcClubs.getClubMessage(player));
					}
				}
			}
		}
	}


	@EventHandler(priority = EventPriority.HIGH)
	private void onPlayerMove_Func(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location playerLocation = null;
		Location signLocation = null;

		if (GcEntityListener.signExists.containsKey(player)) {
			if (!GcEntityListener.signExists.get(player)) {
				playerLocation = player.getLocation();
			}
		}

		if (!(playerLocation == null)) {
			signLocation = GcBlockListener.signLocation.get(player);
			if (!(signLocation == null)) {
				if (playerLocation.distance(signLocation) > 2) {
					player.teleport(signLocation);
					player.sendMessage(ChatColor.RED + "You cannot move until your launched ball has landed.");

				}
			}
		}
	}

}
