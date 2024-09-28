package com.Mogen.WorkbenchPractice.listeners;

import com.Mogen.WorkbenchPractice.game.Game;
import com.Mogen.WorkbenchPractice.game.GameManager;
import com.Mogen.WorkbenchPractice.game.GameStatus;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class VillagerListener implements Listener {
    private final GameManager gameManager;

    public VillagerListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onVillagerClick(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (entity instanceof Villager) {
            Game game = gameManager.getActiveGame(player);
            if (game != null && game.getStatus() != GameStatus.COMPLETED) {
                Villager villager = (Villager) entity;

                // Check if the villager is associated with the player's game station
                if (isSameLocation(villager.getLocation(), game.getStation().getVillagerLocation())) {
                    game.validateCraft();
                    event.setCancelled(true);
                }
            }
        }
    }

    private boolean isSameLocation(Location loc1, Location loc2) {
        if (loc1.getWorld() != loc2.getWorld()) return false;
        return Math.abs(loc1.getX() - loc2.getX()) < 0.5
                && Math.abs(loc1.getY() - loc2.getY()) < 0.5
                && Math.abs(loc1.getZ() - loc2.getZ()) < 0.5;
    }
}