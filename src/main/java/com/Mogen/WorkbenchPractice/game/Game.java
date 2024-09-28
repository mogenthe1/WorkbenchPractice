package com.Mogen.WorkbenchPractice.game;

import com.Mogen.WorkbenchPractice.WorkbenchPractice;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game {
    WorkbenchPractice plugin;
    private final Player player;
    private final List<Material> crafts;
    private final String difficulty;
    private final String foreman;
    private int currentCraft;
    private int finalCraftIndex;
    private long remainingTime;
    private boolean isWaiting;
    private GameStatus status;
    private CraftStation station;
    private final GameManager gameManager;
    private BossBar bossBar;
    private SidebarManager sidebarManager;
    private BukkitTask pregameTask;
    private BukkitRunnable gameTask;
    private int countdown;
    private long startTime;
    private long endTime;
    private long craftStart;
    private long craftEnd;
    private final Set<Location> accessedBlocks = new HashSet<>();
    private final int waitTime;

    public Game(Player player, List<Material> crafts, String difficulty, long gameDuration, CraftStation station,
                GameManager gameManager, WorkbenchPractice plugin, SidebarManager sidebarManager, String foreman, int waitTime) {
        this.player = player;
        this.crafts = crafts;
        this.difficulty = difficulty;
        this.currentCraft = 0;
        this.remainingTime = gameDuration;
        this.status = GameStatus.WAITING;
        this.isWaiting = false;
        this.station = station;
        this.gameManager = gameManager;
        this.bossBar = Bukkit.createBossBar("", BarColor.PINK, BarStyle.SOLID);
        this.bossBar.addPlayer(player);
        this.plugin = plugin;
        this.sidebarManager = sidebarManager;
        this.finalCraftIndex = crafts.size() - 1;
        this.foreman = foreman;
        this.waitTime = waitTime;
    }


    public void start() {
        getStation().setOccupied(true);
        this.status = GameStatus.PREGAME;
        player.setGameMode(GameMode.SURVIVAL);
        getStation().clearStation();
        player.getInventory().clear();
        pregame();
    }

    public void pregame() {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        gameManager.startFurnaceCheckTask(this);
        station.spawnVillager();

        countdown = waitTime + 1;
        pregameTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                countdown--;
                bossBar.setTitle(ChatColor.YELLOW + "" + ChatColor.BOLD + "Workbench starts in " + ChatColor.RED + "" + ChatColor.BOLD
                        + countdown + ChatColor.YELLOW + "" + ChatColor.BOLD + " seconds!");
                bossBar.setProgress(countdown / 10.0);

            if (countdown <= 4) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0f, 1.0f);
            }

            if (countdown <= 0) {
                bossBar.removeAll();
                bossBar.setVisible(false);
                pregameTask.cancel();
                startGame();


            }

        }, 0L, 20L);

    }

    private void startGame() {
        bossBar.removeAll();
        sendCraftRequestMessage(player, getCurrentCraft());
        this.status = GameStatus.IN_PROGRESS;
        sidebarManager.updateSidebar(player);
        startTimer();
        bossBar.addPlayer(player);
        gameManager.drawBlocks(this);
        clearAccessedBlocks();
        player.getInventory().clear();
        equipChainmailArmor(player);
        craftStart = System.nanoTime();
    }



    public void startTimer() {
        startTime = System.nanoTime();
       gameTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (remainingTime <= 0) {
                    end();
                    cancel();
                    return;
                }
                remainingTime--;
                if (remainingTime <= 149) {
                    updateBossBar();
                    bossBar.setVisible(true);
                }
                if (!getStation().isPlayerInside(player)) {
                    end();
                }
            }
        };
        gameTask.runTaskTimer(Bukkit.getPluginManager().getPlugin("WorkbenchPractice"), 20L, 20L);
    }

    public void validateCraft() {
        if (isPlayerWaiting() || getStatus() != GameStatus.IN_PROGRESS) {
            return;
        }
        Material currentCraft = getCurrentCraft();

        if (getCurrentCraftIndex() < finalCraftIndex) {
            if (player.getInventory().contains(currentCraft) || isPlayerWearing()) {
                craftEnd = System.nanoTime();
                setPlayerWaiting(true);
                sendSuccessMessage(player);
                showHeartParticles();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        giveNextCraft();
                        setPlayerWaiting(false);
                    }
                }.runTaskLater(plugin, 20L);
            } else {
                sendFailureMessage(player, getCurrentCraft());
            }
        }

        if (getCurrentCraftIndex() >= finalCraftIndex) {
            if (player.getInventory().contains(currentCraft)) {
                craftEnd = System.nanoTime();
                setPlayerWaiting(true);
                sendSuccessMessage(player);
                showHeartParticles();
                this.currentCraft++;

                finish(player);
            }
            else {
                sendFailureMessage(player, getCurrentCraft());
            }
        }

    }

    private void finish(Player player) {
        endTime = System.nanoTime();
        String message = ChatColor.AQUA + player.getName() + ChatColor.YELLOW + " has completed the items needed to craft! " +
                getGameCraftTimeString();
        Bukkit.broadcastMessage(message);
        double gameTime = (double) (endTime - startTime) / 1_000_000_000L; // Convert to seconds
        gameManager.getScoreManager().updateTopScore(player, gameTime);
        end();

    }

    public void end() {
        this.status = GameStatus.COMPLETED;
        this.getStation().setOccupied(false);
        this.gameManager.endGame(this);
        bossBar.removeAll();
        if (pregameTask != null) {
            pregameTask.cancel();
        }
        if (gameTask != null) {
            gameTask.cancel();
        }
        sidebarManager.updateSidebar(player);
        player.getInventory().clear();
        getStation().resetFurnaces();
        player.teleport(player.getWorld().getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }


    public void giveNextCraft() {
        craftStart = System.nanoTime();
        currentCraft++;
        sidebarManager.updateSidebar(player);
        gameManager.drawBlocks(this);
        if (getStation().getDirection().equalsIgnoreCase("south")) {
            getStation().resetFurnaces();
        }
        clearAccessedBlocks();
        player.getInventory().clear();
        sendCraftRequestMessage(player, getCurrentCraft());

    }

    private void updateBossBar() {
        double progress = (double) remainingTime / 150L; // Assuming game duration is 200 ticks
        bossBar.setTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "Crafting time: " + ChatColor.RED + "" + ChatColor.BOLD + remainingTime);
        bossBar.setProgress(progress);
    }

    public Material getCurrentCraft() {
        return crafts.get(currentCraft);
    }

    public int getCurrentCraftIndex() {
        return currentCraft;
    }


    private void showHeartParticles() {
        Villager villager = station.villager;
        if (villager != null) {
            villager.getWorld().spawnParticle(Particle.HEART, villager.getLocation().add(0, 1, 0), 10);
        }
    }

    private boolean isPlayerWearing() {
        for (ItemStack armorPiece : player.getInventory().getArmorContents()) {
            if (armorPiece != null && armorPiece.getType() == getCurrentCraft()) {
                return true;
            }
            }
        return false;
    }


    // clean up everything

    public Player getPlayer() {
        return player;
    }

    public GameStatus getStatus() {
        return status;
    }

    public CraftStation getStation() {
        return station;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getForeman() {
        return this.foreman;
    }


    public String getCraftTimeString() {
        double elapsedTime = (craftEnd - craftStart) / 1_000_000_000.0; // Convert to seconds
        return ChatColor.AQUA + "(" + String.format("%.3f", elapsedTime) + "s)";
    }

    public String getGameCraftTimeString() {
        double elapsedTime = (endTime - startTime) / 1_000_000_000.0; // Convert to seconds
        return ChatColor.AQUA + "(" + String.format("%.3f", elapsedTime) + "s)";
    }
    private void setPlayerWaiting(boolean waiting) {
        isWaiting = waiting;
    }

    private boolean isPlayerWaiting() {
        return isWaiting;
    }


    private void sendSuccessMessage(Player player) {
        player.sendMessage(ChatColor.GOLD + "Foreman " + getForeman() + ": " + ChatColor.YELLOW + "Perfect! That's just what I needed! "
        + getCraftTimeString());
    }

    private void sendFailureMessage(Player player, Material currentCraft) {
        player.sendMessage(ChatColor.GOLD + "Foreman " + getForeman() + ": " + ChatColor.YELLOW + "That's not what I need! I need "
                + InfoMaps.getItemName(currentCraft) + "! Look at the wall next to me to see the Recipe!");
    }

    private void sendCraftRequestMessage(Player player, Material currentCraft) {
        player.sendMessage(ChatColor.GOLD + "Foreman " + getForeman() + ": " + ChatColor.YELLOW + "Ok, so I need you to craft me " + InfoMaps.getItemName(currentCraft) + "!");
    }

    public void equipChainmailArmor(Player player) {
        player.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
        player.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        player.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
    }
    public boolean isBlockAccessed(Location location) {
        return accessedBlocks.contains(location);
    }

    // Method to add a block to the accessed set
    public void addAccessedBlock(Location location) {
        accessedBlocks.add(location);
    }

    // Method to clear the accessed blocks set
    public void clearAccessedBlocks() {
        accessedBlocks.clear();
    }

    // Helper method to center text




}

