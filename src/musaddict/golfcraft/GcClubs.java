package musaddict.golfcraft;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;


public class GcClubs {

	public static HashMap <Player, Integer> Club = new HashMap <Player, Integer>();
	public static HashMap <Player, Float> Force = new HashMap <Player, Float>();

	public static String getClubMessage(Player player) {
		int club = Club.get(player);
		String message = "Unknown club";
		if(club == 0){
			message = ChatColor.GRAY + "[" + ChatColor.GOLD + "Driver (1 wood)" + ChatColor.GRAY + "]" ;
		}
		if(club == 1){
			message = ChatColor.GRAY + "[" + ChatColor.GOLD + "3 wood" + ChatColor.GRAY + "]" ;
		}
		if(club == 2){
			message = ChatColor.GRAY + "[" + ChatColor.GOLD + "5 wood" + ChatColor.GRAY + "]" ;
		}
		if(club == 3){
			message = ChatColor.GRAY + "[" + ChatColor.GOLD + "4 iron" + ChatColor.GRAY + "]" ;
		}
		if(club == 4){
			message = ChatColor.GRAY + "[" + ChatColor.GOLD + "5 iron" + ChatColor.GRAY + "]" ;
		}
		if(club == 5){
			message = ChatColor.GRAY + "[" + ChatColor.GOLD + "6 iron" + ChatColor.GRAY + "]" ;
		}
		if(club == 6){
			message = ChatColor.GRAY + "[" + ChatColor.GOLD + "7 iron" + ChatColor.GRAY + "]" ;
		}
		if(club == 7){
			message = ChatColor.GRAY + "[" + ChatColor.GOLD + "8 iron" + ChatColor.GRAY + "]" ;
		}
		if(club == 8){
			message = ChatColor.GRAY + "[" + ChatColor.GOLD + "9 iron" + ChatColor.GRAY + "]" ;
		}
		if(club == 9){
			message = ChatColor.GRAY + "[" + ChatColor.GOLD + "Pitching Wedge" + ChatColor.GRAY + "]" ;
		}
		if(club == 10){
			message = ChatColor.GRAY + "[" + ChatColor.GOLD + "Sand Wedge" + ChatColor.GRAY + "]" ;
		}
		if(club == 11){
			message = ChatColor.GRAY + "[" + ChatColor.GOLD + "Putter" + ChatColor.GRAY + "]" ;
		}
		return message;
	}

	public static String getClubMessage2(Player player) {
		int club = Club.get(player);
		String message = "Unknown club";
		if(club == 0){
			message = ChatColor.GRAY  + "Driver (1 wood)";
		}
		if(club == 1){
			message = ChatColor.GRAY + "3 wood";
		}
		if(club == 2){
			message = ChatColor.GRAY + "5 wood";
		}
		if(club == 3){
			message = ChatColor.GRAY + "4 iron";
		}
		if(club == 4){
			message = ChatColor.GRAY + "5 iron";
		}
		if(club == 5){
			message = ChatColor.GRAY + "6 iron";
		}
		if(club == 6){
			message = ChatColor.GRAY + "7 iron";
		}
		if(club == 7){
			message = ChatColor.GRAY + "8 iron";
		}
		if(club == 8){
			message = ChatColor.GRAY + "9 iron";
		}
		if(club == 9){
			message = ChatColor.GRAY + "Pitching Wedge";
		}
		if(club == 10){
			message = ChatColor.GRAY + "Sand Wedge";
		}
		if(club == 11){
			message = ChatColor.GRAY + "Putter";
		}
		return message;
	}

	public static Double getClubSpeed(Player player) {
		int club = Club.get(player);
		if(club == 0){
			if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.GRASS){
				return 0.9;
			}
			if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SAND){
				return 0.35;
			}
			if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.WOOD){
				return 1.63;
			}
			return 1.25;
		}
		if(club == 1){
			if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.GRASS){
				return 0.86;
			}
			if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SAND){
				return 0.35;
			}
			if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.WOOD){
				return 1.49;
			}
			return 1.2;
		}
		if(club == 2){
			if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SAND){
				return 0.35;
			}
			if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.WOOD){
				return 1.35;
			}
			return 1.15;
		}
		if(club == 3){
			if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SAND){
				return 0.6;
			}
			return 1.24;
		}
		if(club == 4){
			if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SAND){
				return 0.6;
			}
			return 1.18;
		}
		if(club == 5){
			if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SAND){
				return 0.6;
			}
			return 1.12;
		}
		if(club == 6){
			if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SAND){
				return 0.6;
			}
			return 1.06;
		}
		if(club == 7){
			if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SAND){
				return 0.6;
			}
			return 1.0;
		}
		if(club == 8){
			if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SAND){
				return 0.6;
			}
			return 0.93;
		}
		if(club == 9){
			if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SAND){
				return 0.6;
			}
			return 0.85;
		}
		if(club == 10){
			if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SAND){
				return 0.7;
			}
			return 0.6;
		}
		if(club == 11){
			if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.GRASS){
				return 0.3;
			}
			if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SAND){
				return 0.2;
			}
			return 0.45;
		}
		return 1.0;
	}

	public static Float getClubPitch(Player player) {
		int club = Club.get(player);
		float force = Force.get(player);
		if(club == 0){
			return (float) (1.6 * force);
		}
		if(club == 1){
			return (float) (1.75 * force);
		}
		if(club == 2){
			return (float) (2 * force);
		}
		if(club == 3){
			return (float) (2.1 * force);
		}
		if(club == 4){
			return (float) (2.13 * force);
		}
		if(club == 5){
			return (float) (2.16 * force);
		}
		if(club == 6){
			return (float) (2.2 * force);
		}
		if(club == 7){
			return (float) (2.23 * force);
		}
		if(club == 8){
			return (float) (2.26 * force);
		}
		if(club == 9){
			return (float) (2.44 * force);
		}
		if(club == 10){
			return (float) (2.44 * force);
		}
		if(club == 11){
			return (float) (2.5 * force);
		}
		return (float) (1.57 * force);
	}

	public static String getEfficiencyMessage(Player player) {
		int club = Club.get(player);
		String message = "";
		Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
		if(club == 0){
			if(block.getType() == Material.GRASS){
				message = ChatColor.GRAY + "Your driver was less efficient in the rough.";
			}
			if(block.getType() == Material.SAND){
				message = ChatColor.GRAY + "Your driver was almost useless in the sand.";
			}
			if(block.getType() == Material.WOOL && block.getData() == 13){
				message = ChatColor.GRAY + "Your driver lost momentum on the fairway.";
			}
		}
		if(club >= 1 && club <= 9){
			if(block.getType() == Material.SAND){
				message = ChatColor.GRAY + "Your " + GcClubs.getClubMessage2(player).toLowerCase() + " was less efficient in the sand.";
			}
		}
		if(club == 10){
			if(block.getType() == Material.SAND){
				message = ChatColor.GRAY + "Your " + GcClubs.getClubMessage2(player).toLowerCase() + " was more efficient in the sand!";
			}
		}
		if(club == 11){
			if(block.getType() == Material.GRASS){
				message = ChatColor.GRAY + "Your putter was almost useless in the grass.";
			}
			if(block.getType() == Material.SAND){
				message = ChatColor.GRAY + "Your driver was useless in the sand.";
			}
		}
		return message;
	}

}
