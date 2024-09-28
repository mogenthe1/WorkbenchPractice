package com.Mogen.WorkbenchPractice.listeners;

import com.Mogen.WorkbenchPractice.game.CraftStation;
import com.Mogen.WorkbenchPractice.game.Game;
import com.Mogen.WorkbenchPractice.game.GameManager;
import com.Mogen.WorkbenchPractice.game.GameStatus;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

public class FurnaceListener implements Listener {
    private final GameManager gameManager;

    public FurnaceListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory() instanceof FurnaceInventory) {
            HumanEntity humanEntity = event.getPlayer();
            if (!(humanEntity instanceof Player)) {
                return;
            }
            Player player = (Player) humanEntity;
            Game game = gameManager.getActiveGame(player);

            if (game != null && game.getStatus() != GameStatus.COMPLETED) {
                CraftStation station = game.getStation();
                FurnaceInventory furnaceInventory = (FurnaceInventory) event.getInventory();
                ItemStack fuel = furnaceInventory.getFuel();

                // Check if the furnace is one of the game's furnaces
                boolean isGameFurnace = station.getFurnaces().stream()
                        .anyMatch(loc -> loc.equals(furnaceInventory.getLocation()));

                // Add 64 coal if there is no fuel or less than 64 coal in the furnace
                if (isGameFurnace && (fuel == null || fuel.getType() != Material.COAL || fuel.getAmount() < 64)) {
                    furnaceInventory.setFuel(new ItemStack(Material.COAL, 64));
                }
            }
        }
    }
}
