package com.Mogen.WorkbenchPractice.commands;

import com.Mogen.WorkbenchPractice.game.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayCommand implements CommandExecutor {
    private final GameManager gameManager;

    public PlayCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Check if the correct number of arguments is provided (now expecting 3 arguments)
            if (args.length < 3) {
                return false; // Show usage message
            }

            String direction = args[0].toLowerCase();
            String difficulty = args[1].toLowerCase();
            String waitTimeStr = args[2];

            // Validate direction and difficulty
            if (!("left".equals(direction) || "right".equals(direction)) ||
                    !("easy".equals(difficulty) || "normal".equals(difficulty))) {
                player.sendMessage("Invalid direction or difficulty. Use /play <left/right> <easy/normal> <wait time>");
                return false;
            }

            // Validate waitTime (check if it's an integer)
            int waitTime;
            try {
                waitTime = Integer.parseInt(waitTimeStr);
            } catch (NumberFormatException e) {
                player.sendMessage("Invalid wait time. Please enter a valid number between 3 and 10.");
                return false;
            }

            // Ensure waitTime is between 3 and 10
            if (waitTime < 3 || waitTime > 10) {
                player.sendMessage("Wait time must be between 3 and 10.");
                return false;
            }

            // Now that everything is valid, start the game with the waitTime
            gameManager.startGame(player, direction, difficulty, waitTime);
            return true;
        }
        return false;
    }
}
