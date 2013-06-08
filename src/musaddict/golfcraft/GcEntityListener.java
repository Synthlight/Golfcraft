package musaddict.golfcraft;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;



public class GcEntityListener implements Listener {

	Golfcraft plugin;


	GcEntityListener(Golfcraft plugin) {
		this.plugin = plugin;
	}

	ArrayList <Snowball> bounce1 = new ArrayList <Snowball>();

	public static HashMap <Player, Integer> bounce = new HashMap <Player, Integer>();
	public static HashMap <Snowball, Player> snowballShooters = new HashMap <Snowball, Player>();
	public static HashMap <Player, Boolean> signExists = new HashMap <Player, Boolean>();
	public static HashMap <Player, Location> signLocation = new HashMap <Player, Location>();
	public static HashMap <Player, Boolean> finishedHole = new HashMap <Player, Boolean>();
	public static HashMap <Player, Integer> score = new HashMap <Player, Integer>();


	/*
	 * @EventHandler(priority = EventPriority.HIGH) public void
	 * onProjectileLaunch(ProjectileLaunchEvent event) { onProjectileLaunch_Func(event); }
	 */

	@EventHandler(priority = EventPriority.HIGH)
	public void onProjectileHit(ProjectileHitEvent event) {
		onProjectileHit_Func(event);
	}


	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityTarget(EntityTargetEvent event) {
		onEntityTarget_Func(event);
	}


	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityShootBow(EntityShootBowEvent event) {
		onEntityShootBow_Func(event);
	}


	/**
	 * This method saves the bow force, to manually set the vector later
	 */
	private void onEntityShootBow_Func(EntityShootBowEvent event) {
		if (event.getEntity() instanceof Player) {
			Player shooter = (Player) event.getEntity();

			if (snowballShooters.containsValue(shooter)) // If they've fired a ball then this map
															// will have an entry for them.
				return;

			String shooterName = shooter.getName();
			boolean isGolfer = true;

			if (!GcCommands.Golfing.containsKey(shooter)) {
				isGolfer = false;
			}
			else if (!GcCommands.Golfing.get(shooter)) {
				isGolfer = false;
			}

			if (!signExists.containsKey(shooter))
				signExists.put(shooter, false);

			if (shooter.hasPermission("golf.play") || shooter.isOp()) {

				if (isGolfer) {
					if (!signExists.get(shooter)) {

						Player [] list = Bukkit.getOnlinePlayers();
						for (Player players : list) {

							if (GcCommands.playingHole.containsKey(players)) {
								if (GcCommands.playingHole.get(players).equals(GcCommands.playingHole.get(shooter))) {

									boolean Continue = false;

									if (finishedHole.containsKey(shooter)) {
										if (!finishedHole.get(shooter)) {
											Continue = true;
										}
									}
									else {
										Continue = true;
										finishedHole.put(shooter, false);
									}

									if (Continue) {
										if (!GcClubs.Club.containsKey(shooter)) {
											GcClubs.Club.put(shooter, 0);
										}
										if (plugin.getConfig().getBoolean("enable-clubs")) {
											players.sendMessage(ChatColor.DARK_GREEN + "" + shooterName + ChatColor.GRAY + " swung their " + GcClubs.getClubMessage2(shooter));
											if (!GcClubs.getEfficiencyMessage(shooter).isEmpty())
												shooter.sendMessage("" + GcClubs.getEfficiencyMessage(shooter));
										}
										else {
											players.sendMessage(ChatColor.DARK_GREEN + "" + shooterName + ChatColor.GRAY + " has launched the ball!");
										}

									}
								}
							}
						}

						// Ball.put(shooter, event.getEntity().getUniqueId());
						if (score.containsKey(shooter)) {
							score.put(shooter, score.get(shooter) + 1);
						}
						else {
							score.put(shooter, 1);
						}

						GcClubs.Force.put(shooter, event.getForce());

						Entity proj = event.getProjectile();
						World world = proj.getWorld();

						Snowball newProj = (Snowball) world.spawn(proj.getLocation(), Snowball.class);

						newProj.setShooter(shooter);
						newProj.setVelocity(proj.getVelocity());
						newProj.setBounce(true);

						if (!plugin.getConfig().getBoolean("enable-clubs") && plugin.getConfig().getBoolean("enable-speed-variances")) {
							if (shooter.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SAND) { // 60-80
								int chance = new Random().nextInt(GcConfig.sandChance());
								chance = GcConfig.sandBase() + chance;
								double calcChance = chance / 100.0;
								chance = 100 - chance;
								newProj.setVelocity(newProj.getVelocity().multiply(calcChance));
								shooter.sendMessage(ChatColor.YELLOW + "The sand trap reduced your speed by " + chance + "%");
							}
							if (shooter.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.GRASS) { // 20-40
								int chance = new Random().nextInt(GcConfig.roughChance());
								chance = GcConfig.roughBase() - chance;
								double calcChance = chance / 100.0;
								chance = 100 - chance;
								newProj.setVelocity(newProj.getVelocity().multiply(calcChance));
								shooter.sendMessage(ChatColor.GRAY + "The rough reduced your speed by " + ChatColor.YELLOW + chance + "%");
							}
							if (shooter.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.WOOD) { // 100-130
								int chance = new Random().nextInt(GcConfig.teeingGroundChance());
								chance = GcConfig.teeingGroundBase() + chance;
								double calcChance = chance / 100.0;
								chance -= 100;
								newProj.setVelocity(newProj.getVelocity().multiply(calcChance));
								shooter.sendMessage(ChatColor.GRAY + "Your driver increased your speed by " + ChatColor.YELLOW + chance + "%");
							}
						}
						else {
							if (!GcClubs.Club.containsKey(shooter)) {
								GcClubs.Club.put(shooter, 0);
							}
							Float force = event.getForce();
							Float dirX = (float) ((0 - (Math.sin((shooter.getLocation().getYaw() / 180) * Math.PI) * 3)) * force);
							Float dirZ = (float) ((Math.cos((shooter.getLocation().getYaw() / 180) * Math.PI) * 3) * force);
							newProj.setVelocity(newProj.getVelocity().setX(dirX));
							newProj.setVelocity(newProj.getVelocity().setZ(dirZ));
							if (!GcClubs.Club.get(shooter).equals(11))
								newProj.setVelocity(newProj.getVelocity().setY(GcClubs.getClubPitch(shooter)));

							newProj.setVelocity(newProj.getVelocity().multiply((GcClubs.getClubSpeed(shooter) * 0.475 * (plugin.getConfig().getInt("ball-power-percentage") / 100.0))));
						}

						snowballShooters.put(newProj, shooter);
						bounce1.add(newProj);

						event.setProjectile(newProj);

					}
					else {
						shooter.sendMessage(ChatColor.RED + "You must destroy your sign to register a hit.");
					}
				}
			}
		}
	}


	private Vector getReflectedVector(final Projectile oldProjectile, final Vector oldVelocity) {
		Vector newVector = oldVelocity.clone();

		newVector.setY(-newVector.getY());

		Vector normalized = oldVelocity.normalize();

		double x = normalized.getX(), z = normalized.getZ();

		Block loc = oldProjectile.getLocation().getBlock();

		if (x > 0 && loc.getRelative(BlockFace.SOUTH).getType() != Material.AIR)
			newVector.setX(-newVector.getX());
		else if (x < 0 && loc.getRelative(BlockFace.NORTH).getType() != Material.AIR)
			newVector.setX(-newVector.getX());

		if (z > 0 && loc.getRelative(BlockFace.WEST).getType() != Material.AIR)
			newVector.setZ(-newVector.getZ());
		else if (z < 0 && loc.getRelative(BlockFace.EAST).getType() != Material.AIR)
			newVector.setZ(-newVector.getZ());

		return newVector;
	}


	private Snowball makeNewBall(final Projectile oldProjectile, final Vector newVector) {
		World world = oldProjectile.getWorld();

		Snowball ball = (Snowball) world.spawn(oldProjectile.getLocation(), Snowball.class);

		ball.setVelocity(newVector);
		ball.setBounce(true);

		return ball;
	}


	/**
	 * This method compares the arrow UUID to the launched arrow's UUID. If they match then alter
	 * the score, tell the golfers where it landed, and how far away it is. If the arrow landed in a
	 * valid location, then save the location to generate a sign later.
	 */
	private void onProjectileHit_Func(ProjectileHitEvent event) {
		Player shooter = null;

		if (event.getEntity() instanceof Snowball) {
			Snowball proj = (Snowball) event.getEntity();
			Snowball newBall = null;
			Integer bounceInt = plugin.getConfig().getInt("number-of-bounces");

			if (bounce1.size() > 0 && bounce1.contains(proj)) {
				bounce1.remove(proj);
				if (!bounce.containsKey(shooter))
					bounce.put(shooter, 0);

				Integer bounceMap = bounce.get(shooter);
				if (bounceMap <= bounceInt) {
					newBall = makeNewBall(proj, getReflectedVector(proj, proj.getVelocity().multiply(0.5)));
					bounce1.add(newBall);
					bounce.put(shooter, bounceMap + 1);
				}
				else
					bounce.remove(shooter);

			}

			if (snowballShooters.containsKey(proj)) {
				shooter = snowballShooters.get(proj);
				snowballShooters.remove(proj);

				if (newBall != null) { // Remove the old ball and track the new one.
					if (newBall.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SAND) {
						newBall.setVelocity(newBall.getVelocity().multiply(0.05));
					}
					if (newBall.getLocation().getBlock().getType() == Material.WATER) {
						newBall.setVelocity(newBall.getVelocity().multiply(0.2));
					}
					if (newBall.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.LEAVES) {
						newBall.setVelocity(newBall.getVelocity().multiply(0.1));
					}
					if (newBall.getLocation().getBlock().getType() == Material.LAVA) {
						newBall.setVelocity(newBall.getVelocity().multiply(0.01));
					}
					if (newBall.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.GRASS) {
						newBall.setVelocity(newBall.getVelocity().multiply(0.7));
					}
					snowballShooters.put(newBall, shooter);

					return;
				}
			}

		}

		if (shooter == null)
			return;

		boolean hazard = false;
		boolean createSign = false;
		boolean inHole = false;
		Block block = event.getEntity().getLocation().getBlock().getRelative(BlockFace.DOWN);
		String playersMessage = null;
		String scoreString = null;
		String shooterName = shooter.getName();

		/**
		 * everything from here down requires the location of the block 1-down from the ball (As
		 * long as it's not a hazard)
		 */
		// moves the block down till its not air or fence
		while (block.getType() == Material.AIR || block.getType() == Material.FENCE) {
			block = block.getRelative(BlockFace.DOWN);
		}

		// if the block isn't a cup, then move it back up while it's a fence
		if (!(block.getType() == Material.GLOWSTONE)) {
			while (block.getType() == Material.FENCE || block.getType() == Material.FENCE) {
				block = block.getRelative(BlockFace.UP);
			}
		}

		// boolean isGolfer = true;

		if (!GcCommands.Golfing.containsKey(shooter)) {
			// isGolfer = false;
			return;
		}
		else if (!GcCommands.Golfing.get(shooter)) {
			// isGolfer = false;
			return;
		}

		boolean Continue = false;

		if (finishedHole.containsKey(shooter)) {
			if (!finishedHole.get(shooter)) {
				Continue = true;
			}
		}
		else {
			Continue = true;
			finishedHole.put(shooter, false);
		}

		if (Continue) {
			int dist = (int) event.getEntity().getLocation().distance(shooter.getLocation());

			if (block.getRelative(BlockFace.UP).getType() == Material.WATER || block.getRelative(BlockFace.UP).getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER) {
				playersMessage = ChatColor.DARK_GREEN + shooterName + ChatColor.RED + " just landed in a water hazard!";
				hazard = true;
			}
			if (block.getRelative(BlockFace.UP).getType() == Material.LAVA || block.getRelative(BlockFace.UP).getType() == Material.STATIONARY_LAVA || block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA) {
				playersMessage = ChatColor.DARK_GREEN + shooterName + ChatColor.DARK_RED + " just landed in a lava hazard!";
				hazard = true;
			}
			if (block.getType() == Material.SAND && !hazard) {
				playersMessage = ChatColor.DARK_GREEN + shooterName + ChatColor.YELLOW + " just landed in a sand trap... " + ChatColor.GRAY + "(" + ChatColor.AQUA + dist + "m" + ChatColor.GRAY + ")";
			}
			if (block.getType() == Material.GRASS && !hazard) {
				playersMessage = ChatColor.DARK_GREEN + shooterName + ChatColor.GRAY + " just landed in the rough... " + ChatColor.GRAY + "(" + ChatColor.AQUA + dist + "m" + ChatColor.GRAY + ")";
			}
			if (block.getType() == Material.WOOL && block.getData() == 13 && !hazard) {
				playersMessage = ChatColor.DARK_GREEN + shooterName + " just landed in the fairway! " + ChatColor.GRAY + "(" + ChatColor.AQUA + dist + "m" + ChatColor.GRAY + ")";
			}
			if (block.getType() == Material.WOOL && block.getData() == 5 && !hazard) {
				playersMessage = ChatColor.DARK_GREEN + shooterName + ChatColor.GREEN + " just landed on the green. " + ChatColor.GRAY + "(" + ChatColor.AQUA + dist + "m" + ChatColor.GRAY + ")";
			}
			if (block.getType() == Material.GLOWSTONE && !hazard) {

				if (GcCommands.playingHole.containsKey(shooter)) {
					GcHole existingHole = GcFiles.getHole(shooter.getWorld().getName(), GcCommands.playingHole.get(shooter));
					int scoreA = score.get(shooter);
					int parA = existingHole.par;
					int scoreFinal = scoreA - parA;

					if (scoreA == 1) {
						scoreString = ChatColor.GOLD + "----" + shooter.getName() + " GOT A HOLE IN ONE----";
					}
					else {
						if (scoreFinal <= 0 - 3) {
							scoreString = ChatColor.GRAY + "Score: " + scoreA + " (" + ChatColor.GOLD + "Double Eagle" + ChatColor.GRAY + ")";
						}
						if (scoreFinal == 0 - 2) {
							scoreString = ChatColor.GRAY + "Score: " + scoreA + " (" + ChatColor.YELLOW + "Eagle" + ChatColor.GRAY + ")";
						}
						if (scoreFinal == 0 - 1) {
							scoreString = ChatColor.GRAY + "Score: " + scoreA + " (" + ChatColor.GREEN + "Birdie" + ChatColor.GRAY + ")";
						}
						if (scoreFinal == 0) {
							scoreString = ChatColor.GRAY + "Score: " + scoreA + " (" + ChatColor.AQUA + "Par" + ChatColor.GRAY + ")";
						}
						if (scoreFinal == 1) {
							scoreString = ChatColor.GRAY + "Score: " + scoreA + " (" + ChatColor.YELLOW + "Bogey" + ChatColor.GRAY + ")";
						}
						if (scoreFinal == 2) {
							scoreString = ChatColor.GRAY + "Score: " + scoreA + " (" + ChatColor.RED + "Double Bogey" + ChatColor.GRAY + ")";
						}
						if (scoreFinal >= 3) {
							scoreString = ChatColor.GRAY + "Score: " + scoreA + " (" + ChatColor.DARK_RED + "Triple Bogey" + ChatColor.GRAY + ")";
						}
					}

					playersMessage = ChatColor.DARK_GREEN + shooterName + ChatColor.GREEN + " just landed in the" + ChatColor.GOLD + " cup!";
					shooter.sendMessage(ChatColor.GREEN + "You have finished this hole! Start a new hole?");
					shooter.sendMessage(ChatColor.GRAY + "/golf play [hole]");
					finishedHole.put(shooter, true);
					inHole = true;
				}
			}

			if (block.getType() == Material.LEAVES && !hazard) {
				playersMessage = ChatColor.DARK_GREEN + shooterName + ChatColor.RED + " just landed in a birds nest";
				hazard = true;
			}

			Player [] list = Bukkit.getOnlinePlayers();
			for (Player players : list) {

				if (GcCommands.playingHole.containsKey(players)) {
					if (GcCommands.playingHole.get(players).equals(GcCommands.playingHole.get(shooter))) {
						if (playersMessage == null && !(block.getType() == Material.GLOWSTONE)) {
							playersMessage = ChatColor.GRAY + "The ball has landed on " + block.getType().toString().toLowerCase().replace("_", " ") + ". " + ChatColor.GRAY + "(" + ChatColor.AQUA + dist + "m" + ChatColor.GRAY + ")";
							if (plugin.getConfig().getBoolean("unknow-blocks-are-hazards"))
								hazard = true;
						}
						players.sendMessage(playersMessage);
						if (inHole && !(scoreString == null)) {
							players.sendMessage(scoreString);
						}
						createSign = true;

					}
				}
			}
		}

		if (inHole) {
			signExists.remove(shooter);
			signLocation.remove(shooter);
			GcBlockListener.signLocation.remove(shooter);

		}

		if (hazard) {
			while (shooter.getInventory().contains(Material.ARROW)) {
				shooter.getInventory().remove(Material.ARROW);
			}
			ItemStack arrow = new ItemStack(Material.ARROW);
			arrow.setAmount(1);
			shooter.getInventory().addItem(arrow);
			while (shooter.getInventory().contains(Material.BOW)) {
				shooter.getInventory().remove(Material.BOW);
			}
			ItemStack bow = new ItemStack(Material.BOW);
			arrow.setAmount(1);
			shooter.getInventory().addItem(bow);
			score.put(shooter, score.get(shooter) + 1);
		}

		if (createSign && !hazard && !inHole) {
			Block up = block.getRelative(BlockFace.UP);
			up.setType(Material.SIGN_POST);
			up.getState().update(true);
			signExists.put(shooter, true);

			Player [] list = Bukkit.getOnlinePlayers();

			if (up.getState() instanceof Sign) { // It's a sign
				Sign sign = (Sign) (up.getState());
				sign.setLine(0, ChatColor.WHITE + "   [" + ChatColor.DARK_GREEN + "Golf" + ChatColor.WHITE + "]");
				sign.setLine(1, ChatColor.DARK_GRAY + shooterName);
				sign.update(true);
				up.getState().update(true);
				signLocation.put(shooter, sign.getLocation());

			}
			else {

				for (Player players : list) {
					players.sendMessage(ChatColor.RED + "Sign failed to generate!");
				}
			}
		}
	}


	/**
	 * This method kills all mobs who target golfers, but only if there's green or lime wool 1-3
	 * blocks beneath them when the mob targets them, otherwise they can still attack.
	 */
	private void onEntityTarget_Func(EntityTargetEvent event) {
		if (plugin.getConfig().getBoolean("kill-mobs-on-course")) {
			Entity mob = event.getEntity();
			int targetUUID = 0;
			try {
				targetUUID = event.getTarget().getEntityId();
			}
			catch (NullPointerException e) {
				return;
			}
			Player [] list = Bukkit.getOnlinePlayers();

			for (Player players : list) {
				int player = players.getEntityId();
				if (targetUUID == player) {
					if (GcCommands.Golfing.containsKey(players)) {
						if (GcCommands.Golfing.get(players)) {
							Block block = players.getLocation().getBlock().getRelative(BlockFace.DOWN);
							Block block2 = block.getRelative(BlockFace.DOWN);
							Block block3 = block2.getRelative(BlockFace.DOWN);
							if ((block.getType() == Material.WOOL && (block.getData() == 13 || block.getData() == 5) && !mob.isDead()) || (block2.getType() == Material.WOOL && (block2.getData() == 13 || block2.getData() == 5) && !mob.isDead()) || (block3.getType() == Material.WOOL && (block3.getData() == 13 || block3.getData() == 5) && !mob.isDead())) {
								mob.remove();
							}
						}
					}
				}
			}
		}

	}
}
