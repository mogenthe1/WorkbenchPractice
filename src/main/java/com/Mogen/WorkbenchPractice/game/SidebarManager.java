package com.Mogen.WorkbenchPractice.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.HashMap;
import java.util.Map;

public class SidebarManager {

    private final ScoreboardManager manager;
    private final Map<Player, Scoreboard> playerScoreboards = new HashMap<>();
    private final GameManager gameManager;

    public SidebarManager(GameManager gameManager) {
        this.manager = Bukkit.getScoreboardManager();
        this.gameManager = gameManager;
    }

    public void updateSidebar(Player player) {
        Game game = gameManager.getActiveGame(player);
        if (game != null && game.getStatus() == GameStatus.IN_PROGRESS) {
            createSidebar(player, game.getCurrentCraftIndex() + 1);
        } else {
            showGeneralInfo(player);
        }
    }

    public void createSidebar(Player player, int craftNumber) {
        Scoreboard board = getOrCreateScoreboard(player);
        Objective objective = getOrCreateObjective(board);

        clearSidebar(board);
        // Set up the scoreboard for a game
        objective.getScore(ChatColor.GRAY + getDate()).setScore(10);
        objective.getScore(" ").setScore(9);
        objective.getScore("Game:").setScore(8);
        objective.getScore(ChatColor.GREEN + "Workbench").setScore(7);
        objective.getScore("  ").setScore(6);
        objective.getScore(ChatColor.AQUA + player.getName() + ChatColor.WHITE + ": " + ChatColor.GREEN + craftNumber).setScore(5);

        // Add personal best time
        String pbTime = gameManager.getScoreManager().getFormattedTopGameScore(player);
        objective.getScore(ChatColor.GOLD + "PB: " + ChatColor.YELLOW + pbTime).setScore(4);

        objective.getScore("   ").setScore(3);
        objective.getScore(ChatColor.YELLOW + "Workbench Practice").setScore(2);

        // Apply the scoreboard to the player
        player.setScoreboard(board);
    }

    public void showGeneralInfo(Player player) {
        Scoreboard board = getOrCreateScoreboard(player);
        Objective objective = getOrCreateObjective(board);

        clearSidebar(board);
        // Set up the scoreboard for general information
        objective.getScore(ChatColor.GRAY + getDate()).setScore(10);
        objective.getScore(" ").setScore(9);
        objective.getScore("Game:").setScore(8);
        objective.getScore(ChatColor.GREEN + "Workbench").setScore(7);
        objective.getScore("  ").setScore(6);
        objective.getScore(ChatColor.AQUA + player.getName()).setScore(5);

        // Add personal best time
        String pbTime = gameManager.getScoreManager().getFormattedTopGameScore(player);
        objective.getScore(ChatColor.GOLD + "PB: " + ChatColor.YELLOW + pbTime).setScore(4);

        objective.getScore("   ").setScore(3);
        objective.getScore(ChatColor.YELLOW + "Workbench Practice").setScore(2);

        // Apply the scoreboard to the player
        player.setScoreboard(board);
    }

    private Scoreboard getOrCreateScoreboard(Player player) {
        return playerScoreboards.computeIfAbsent(player, p -> manager.getNewScoreboard());
    }

    private Objective getOrCreateObjective(Scoreboard board) {
        Objective objective = board.getObjective("sidebar");
        if (objective == null) {
            objective = board.registerNewObjective("sidebar", "dummy", ChatColor.YELLOW + "" + ChatColor.BOLD + "Party Games");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        return objective;
    }

    private void clearSidebar(Scoreboard board) {
        for (String entry : board.getEntries()) {
            board.resetScores(entry);
        }
    }

    private String getDate() {
        // Simple date formatting
        return java.time.LocalDate.now().toString();
    }
}
