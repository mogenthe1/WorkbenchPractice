package com.Mogen.WorkbenchPractice.listeners;

import com.Mogen.WorkbenchPractice.game.Game;
import com.Mogen.WorkbenchPractice.game.GameManager;
import com.Mogen.WorkbenchPractice.game.GameStatus;
import com.Mogen.WorkbenchPractice.game.InfoMaps;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BlockClickListener implements Listener {
    private final GameManager gameManager;

    public BlockClickListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Game game = gameManager.getActiveGame(player);

        if (game != null && game.getStatus() == GameStatus.IN_PROGRESS && event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getClickedBlock() == null) {
                return;
            }

            Block clickedBlock = event.getClickedBlock();
            if (game.getStation().isBlockInStation(clickedBlock.getLocation()) && !game.isBlockAccessed(clickedBlock.getLocation())) {
                game.addAccessedBlock(event.getClickedBlock().getLocation());

                Material blockType = clickedBlock.getType();
                Material dropType = InfoMaps.getBlockDrop(blockType);

                if (dropType != null) {
                    event.setCancelled(true);

                    // Simulate block damage
                    BlockDamageEvent blockDamageEvent = new BlockDamageEvent(player, clickedBlock, player.getInventory().getItemInMainHand(), true);
                    Bukkit.getServer().getPluginManager().callEvent(blockDamageEvent);

                    if (!blockDamageEvent.isCancelled()) {
                        // Play block breaking sound and show particles

                        // Delay the block removal and item addition to simulate breaking
                        Bukkit.getScheduler().runTaskLater(gameManager.plugin, () -> {
                            Sound breakSound = clickedBlock.getBlockData().getSoundGroup().getBreakSound();
                            player.playSound(player.getLocation(), breakSound, 1.0f, 1.0f);
                            BlockData blockData = clickedBlock.getBlockData();
                            clickedBlock.getWorld().spawnParticle(Particle.BLOCK_DUST, clickedBlock.getLocation().add(0.5, 0.5, 0.5), 50, blockData);
                            clickedBlock.setType(Material.AIR);
                            player.getInventory().addItem(new ItemStack(dropType));
                        }, 2L); // Delay of 2 ticks (100 milliseconds)
                    }
                }
            }
        }
    }
}
