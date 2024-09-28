package com.Mogen.WorkbenchPractice.listeners;

import com.Mogen.WorkbenchPractice.WorkbenchPractice;
import com.Mogen.WorkbenchPractice.game.GameManager;
import com.Mogen.WorkbenchPractice.game.SidebarManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

public class PlayerJoinListener implements Listener {
    private WorkbenchPractice plugin;
    private final GameManager gameManager;
    private final SidebarManager sidebarManager;

    public PlayerJoinListener(Plugin plugin, GameManager gameManager, SidebarManager sidebarManager) {
        this.plugin = (WorkbenchPractice) plugin;
        this.gameManager = gameManager;
        this.sidebarManager = sidebarManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        sidebarManager.updateSidebar(player);
        player.teleport(player.getWorld().getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        player.sendMessage(ChatColor.GOLD + "Welcome to Workbench Practice " + ChatColor.AQUA + player.getName() +
                ChatColor.GOLD + "! Use " + ChatColor.AQUA + "/play <left/right> <easy/normal> <pregame seconds>" + ChatColor.GOLD + " to play, " +
                "and use " + ChatColor.AQUA + "/leaderboard" + ChatColor.GOLD + " to see the top players!");
        player.sendMessage(ChatColor.AQUA + "");

    }
}
