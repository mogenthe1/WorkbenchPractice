package com.Mogen.WorkbenchPractice.listeners;

import com.Mogen.WorkbenchPractice.game.Game;
import com.Mogen.WorkbenchPractice.game.GameManager;
import com.Mogen.WorkbenchPractice.game.GameStatus;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryClickListener implements Listener {

    private final GameManager gameManager;

    public InventoryClickListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player && !isOpInCreative(player)) {
            Game game = gameManager.getActiveGame(player);

            if (game != null && game.getStatus() == GameStatus.IN_PROGRESS) {
                InventoryHolder holder = event.getInventory().getHolder();
                InventoryType inventoryType = event.getInventory().getType();

                if (inventoryType == InventoryType.CRAFTING) {
                    // Check if the click is in the 2x2 crafting grid (slots 1 to 4)
                    if (event.getSlot() >= 1 && event.getSlot() <= 4) {
                        // Check if the player is placing an item in the crafting grid
                        if ((event.getCursor() != null && event.getCursor().getType() != Material.AIR) ||
                                (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR)) {
                            event.setCancelled(true);
                            player.sendMessage(ChatColor.RED + "You may not use your player crafting! Use a Workbench (Crafting Table " +
                                    "instead!");
                            return;
                        }
                    }
                } else if (!(holder instanceof Furnace) && inventoryType != InventoryType.WORKBENCH) {
                    event.setCancelled(true);
                    return;
                }
            }

            // Handle hotkey functionality for crafting table
            if (event.getInventory().getType() == InventoryType.WORKBENCH && event.getHotbarButton() >= 0) {
                CraftingInventory craftingInventory = (CraftingInventory) event.getInventory();
                ItemStack clickedItem = event.getCurrentItem();
                PlayerInventory playerInventory = (PlayerInventory) event.getWhoClicked().getInventory();
                int hotbarSlot = event.getHotbarButton();
                ItemStack hotbarItem = playerInventory.getItem(hotbarSlot);

                // Check if the clicked item is in the crafting result slot
                if (event.getSlotType() == InventoryType.SlotType.RESULT && clickedItem != null && clickedItem.getType() != Material.AIR) {
                    // Consume the ingredients
                    if (consumeCraftingIngredients(craftingInventory)) {
                        if (hotbarItem == null || hotbarItem.getType() == Material.AIR) {
                            // Move the item to the hotbar slot if it's empty
                            playerInventory.setItem(hotbarSlot, clickedItem);
                        } else if (hotbarItem.isSimilar(clickedItem)) {
                            // Stack the items if they are similar
                            int totalAmount = hotbarItem.getAmount() + clickedItem.getAmount();
                            int maxStackSize = hotbarItem.getMaxStackSize();

                            if (totalAmount <= maxStackSize) {
                                hotbarItem.setAmount(totalAmount);
                            } else {
                                hotbarItem.setAmount(maxStackSize);
                                clickedItem.setAmount(totalAmount - maxStackSize);
                                playerInventory.addItem(clickedItem);
                            }
                        } else {
                            // Swap the items if they are different
                            playerInventory.setItem(hotbarSlot, clickedItem);
                        }
                        event.setCancelled(true);

                        // Update the crafting result slot
                        updateCraftingResult(craftingInventory);
                    }
                }
            }
        }
    }

    private boolean consumeCraftingIngredients(CraftingInventory craftingInventory) {
        ItemStack[] matrix = craftingInventory.getMatrix();
        boolean hasMaterials = true;

        // First, check if we have enough materials to consume
        for (ItemStack item : matrix) {
            if (item != null && item.getAmount() <= 0) {
                hasMaterials = false;
                break;
            }
        }

        if (hasMaterials) {
            for (int i = 0; i < matrix.length; i++) {
                ItemStack item = matrix[i];
                if (item != null && item.getType() != Material.AIR) {
                    item.setAmount(item.getAmount() - 1);
                    if (item.getAmount() <= 0) {
                        matrix[i] = null;
                    }
                }
            }
            craftingInventory.setMatrix(matrix);
            return true;
        } else {
            return false;
        }
    }

    private void updateCraftingResult(CraftingInventory craftingInventory) {
        // Check if the recipe is null
        if (craftingInventory.getRecipe() == null) {
            craftingInventory.setResult(null);
            return;
        }

        // Simulate the crafting result update
        ItemStack[] matrix = craftingInventory.getMatrix();
        ItemStack result = craftingInventory.getRecipe().getResult(); // Use your own method to get the new result if needed
        craftingInventory.setResult(result);
    }

    private boolean isOpInCreative(Player player) {
        return player.isOp() && player.getGameMode() == org.bukkit.GameMode.CREATIVE;
    }
}
