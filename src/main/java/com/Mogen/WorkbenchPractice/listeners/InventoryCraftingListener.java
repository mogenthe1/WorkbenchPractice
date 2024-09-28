package com.Mogen.WorkbenchPractice.listeners;

import com.Mogen.WorkbenchPractice.game.Game;
import com.Mogen.WorkbenchPractice.game.GameManager;
import com.Mogen.WorkbenchPractice.game.GameStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.entity.Player;

public class InventoryCraftingListener implements Listener {

    private final GameManager gameManager;

    public InventoryCraftingListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            Game game = gameManager.getActiveGame(player);

            if (game != null && game.getStatus() == GameStatus.IN_PROGRESS) {
                InventoryView view = event.getView();
                InventoryType.SlotType slotType = event.getSlotType();

                if (view.getTopInventory() instanceof CraftingInventory && view.getType() == InventoryType.CRAFTING) {
                    CraftingInventory craftingInventory = (CraftingInventory) view.getTopInventory();

                    // Check if the click is in the 2x2 crafting grid (slot 1 to 4)
                    if (craftingInventory.getSize() == 5 && slotType == InventoryType.SlotType.CRAFTING && event.getSlot() >= 1 && event.getSlot() <= 4) {
                        event.setCancelled(true);
                        player.sendMessage("You can only use crafting tables during the game.");
                    }
                }
            }
        }
    }
}
