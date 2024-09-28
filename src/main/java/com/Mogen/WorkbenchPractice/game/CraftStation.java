package com.Mogen.WorkbenchPractice.game;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Furnace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CraftStation {
    private final int id;
    private final String direction;
    private final Location spawn;
    private final Location corner1;
    private final Location corner2;
    private final List<Location> leftSideEasy;
    private final List<Location> leftSideOther;
    private final List<Location> rightSideEasy;
    private final List<Location> rightSideOther;
    private final List<Location> otherBlocks;
    private final List<Location> craftGrid;
    private final List<Location> furnaces;
    private final Location villagerLocation;
    Villager villager;
    private boolean occupied;

    public CraftStation(int id, String direction, Location spawn, Location corner1, Location corner2, List<Location> leftSideEasy, List<Location> leftSideOther,
                        List<Location> rightSideEasy, List<Location> rightSideOther, List<Location> otherBlocks, List<Location> craftGrid,
                        Location villagerLocation, List<Location> furnaces) {
        this.id = id;
        this.direction = direction;
        this.spawn = spawn;
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.leftSideEasy = leftSideEasy;
        this.leftSideOther = leftSideOther;
        this.rightSideEasy = rightSideEasy;
        this.rightSideOther = rightSideOther;
        this.otherBlocks = otherBlocks;
        this.occupied = false;
        this.craftGrid = craftGrid;
        this.villagerLocation = villagerLocation;
        this.furnaces = furnaces;
    }

    public void spawnVillager() {
        // Check if a villager already exists at the location
        for (Entity entity : Objects.requireNonNull(villagerLocation.getWorld()).getNearbyEntities(villagerLocation, 1.0, 1.0, 1.0)) {
            if (entity instanceof Villager) {
                villager = (Villager) entity;
                villager.setCustomNameVisible(false);
                villager.setAI(false); // Prevent the villager from moving
                villager.setInvulnerable(true); // Make the villager invulnerable
                setVillagerRotation(); // Set the correct rotation for the existing villager
                return; // Villager found and set, no need to spawn a new one
            }
        }

        // Spawn a new villager if none exists at the location
        villager = (Villager) villagerLocation.getWorld().spawnEntity(villagerLocation, EntityType.VILLAGER);
        villager.setCustomNameVisible(false);
        villager.setAI(false); // Prevent the villager from moving
        villager.setInvulnerable(true); // Make the villager invulnerable

        Villager.Profession[] professions = Villager.Profession.values();
        Random random = new Random();
        Villager.Profession randomProfession = professions[random.nextInt(professions.length)];
        villager.setProfession(randomProfession);

        setVillagerRotation(); // Set the correct rotation for the new villager
    }

    private void setVillagerRotation() {
        float yaw = 0;
        if (direction.equalsIgnoreCase("NORTH")) {
            yaw = 0; // Villager faces South
        } else if (direction.equalsIgnoreCase("SOUTH")) {
            yaw = 180; // Villager faces North
        }
        villager.setRotation(yaw, villager.getLocation().getPitch());
    }

    public void removeVillager() {
        if (villager != null) {
            villager.remove();
            villager = null;
        }
    }

    public void clearStation() {
        for (Location location : getCombinedBlocks()) {
            location.getBlock().setType(Material.AIR);
        }
    }
    public void resetFurnaces() {
        for (Location location : furnaces) {
            Block block = location.getBlock();
            if (block.getState() instanceof Furnace) {
                Furnace furnace = (Furnace) block.getState();
                furnace.getInventory().clear(); // Clears all slots: input, fuel, and result
                furnace.update();
                block.setType(Material.AIR); // Break the furnace
                block.setType(Material.FURNACE); // Replace the furnace

                // Set the furnace facing the opposite direction of the station
                setFurnaceFacing(block);

                // Clear ground items in a 5-block radius
                clearGroundItems(location, 5);
            }
        }
    }

    private void setFurnaceFacing(Block block) {
        if (block.getBlockData() instanceof Directional) {
            Directional directional = (Directional) block.getBlockData();
            if (direction.equalsIgnoreCase("NORTH")) {
                directional.setFacing(BlockFace.SOUTH); // Furnace faces South
            } else if (direction.equalsIgnoreCase("SOUTH")) {
                directional.setFacing(BlockFace.NORTH); // Furnace faces North
            }
            block.setBlockData(directional);
        }
    }

    private void clearGroundItems(Location location, int radius) {
        for (Entity entity : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
            if (entity instanceof Item) {
                entity.remove();
            }
        }
    }

    public int getId() {
        return id;
    }

    public String getDirection() {
        return direction;
    }

    public Location getSpawn() {
        return spawn;
    }

    public Location getCorner1() {
        return corner1;
    }
    public Location getCorner2() {
        return corner2;
    }

    public boolean isPlayerInside(Player player) {
        Location playerLoc = player.getLocation();
        double x1 = Math.min(corner1.getX(), corner2.getX());
        double y1 = Math.min(corner1.getY(), corner2.getY());
        double z1 = Math.min(corner1.getZ(), corner2.getZ());
        double x2 = Math.max(corner1.getX(), corner2.getX());
        double y2 = Math.max(corner1.getY(), corner2.getY());
        double z2 = Math.max(corner1.getZ(), corner2.getZ());

        double px = playerLoc.getX();
        double py = playerLoc.getY();
        double pz = playerLoc.getZ();

        return (px >= x1 && px <= x2) &&
                (py >= y1 && py <= y2) &&
                (pz >= z1 && pz <= z2);
    }

    public List<Location> getLeftSideEasy() {
        return leftSideEasy;
    }

    public List<Location> getLeftSideOther() {
        return leftSideOther;
    }

    public List<Location> getRightSideEasy() {
        return rightSideEasy;
    }

    public List<Location> getRightSideOther() {
        return rightSideOther;
    }

    public List<Location> getOtherBlocks() {
        return otherBlocks;
    }

    public List<Location> getCraftGrid() {
        return craftGrid;
    }

    public List<Location> getFurnaces() {
        return furnaces;
    }

    public Location getVillagerLocation() {
        return villagerLocation;
    }

    public boolean isBlockInStation(Location blockLocation) {
        return leftSideEasy.contains(blockLocation) ||
                leftSideOther.contains(blockLocation) ||
                rightSideEasy.contains(blockLocation) ||
                rightSideOther.contains(blockLocation) ||
                otherBlocks.contains(blockLocation);
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public List<Location> getCombinedBlocks() {
        List<Location> combinedBlocks = new ArrayList<>();
        combinedBlocks.addAll(leftSideEasy);
        combinedBlocks.addAll(leftSideOther);
        combinedBlocks.addAll(rightSideEasy);
        combinedBlocks.addAll(rightSideOther);
        combinedBlocks.addAll(otherBlocks);
        return combinedBlocks;
    }

    public List<Location> getCombinedLeftSide() {
        List<Location> combinedLeftSide = new ArrayList<>();
        combinedLeftSide.addAll(leftSideEasy);
        combinedLeftSide.addAll(leftSideOther);
        return combinedLeftSide;
    }

    public List<Location> getCombinedRightSide() {
        List<Location> combinedRightSide = new ArrayList<>();
        combinedRightSide.addAll(rightSideEasy);
        combinedRightSide.addAll(rightSideOther);
        return combinedRightSide;
    }
}
