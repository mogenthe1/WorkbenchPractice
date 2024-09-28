package com.Mogen.WorkbenchPractice.commands;

import com.Mogen.WorkbenchPractice.game.ScoreManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LeaderboardCommand implements CommandExecutor {
    private final ScoreManager scoreManager;

    public LeaderboardCommand(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("leaderboard")) {
            List<Map.Entry<UUID, Double>> topScores = scoreManager.getTopScores(100);

            sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Top 100 Players:");
            for (int i = 0; i < topScores.size(); i++) {
                Map.Entry<UUID, Double> entry = topScores.get(i);
                UUID playerUUID = entry.getKey();
                String playerName = Bukkit.getOfflinePlayer(playerUUID).getName();
                String formattedScore = scoreManager.getFormattedScore(entry.getValue());

                sender.sendMessage(ChatColor.AQUA + "" + (i + 1) + ". " + playerName + ": " + ChatColor.YELLOW + formattedScore);
            }
            return true;
        }
        return false;
    }
}
