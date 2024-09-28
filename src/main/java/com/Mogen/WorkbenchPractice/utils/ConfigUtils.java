package com.Mogen.WorkbenchPractice.utils;

import com.Mogen.WorkbenchPractice.WorkbenchPractice;
import com.Mogen.WorkbenchPractice.game.CraftStation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigUtils {
    private final WorkbenchPractice plugin;

    public ConfigUtils(WorkbenchPractice plugin) {
        this.plugin = plugin;
        setupConfig();
    }

    private void setupConfig() {
        // Ensure the crafting_stations directory exists
        File dir = new File(plugin.getDataFolder(), "crafting_stations");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Create the default configuration for Craft Stations if they don't exist
        for (int i = 0; i < 8; i++) {
            File stationFile = new File(dir, "station_" + (i + 1) + ".yml");
            if (!stationFile.exists()) {
                createDefaultStationConfig(stationFile, i + 1, i * 21, "NORTH");
            }
        }

        // Create the new configuration for the additional 8 stations if they don't exist
        for (int i = 0; i < 8; i++) {
            File stationFile = new File(dir, "new_station_" + (i + 1) + ".yml");
            if (!stationFile.exists()) {
                createNewStationConfig(stationFile, i + 9, i * 21);
            }
        }
    }

    private void createDefaultStationConfig(File file, int id, int xOffset, String direction) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("id", id);
        config.set("direction", direction);

        config.set("spawn.world", "world");
        config.set("spawn.x", -1844.5 + xOffset);
        config.set("spawn.y", 39);
        config.set("spawn.z", 719.5);

        config.set("corner1.world", "world");
        config.set("corner1.x", -1857 + xOffset);
        config.set("corner1.y", 38);
        config.set("corner1.z", 734);

        config.set("corner2.world", "world");
        config.set("corner2.x", -1841 + xOffset);
        config.set("corner2.y", 50);
        config.set("corner2.z", 717);

        config.set("villager.world", "world");
        config.set("villager.x", -1845 + xOffset);
        config.set("villager.y", 39);
        config.set("villager.z", 716.3);

        List<Map<String, Object>> furnaces = new ArrayList<>();
        addBlock(furnaces, -1855 + xOffset, 40, 716);
        addBlock(furnaces, -1854 + xOffset, 40, 716);
        addBlock(furnaces, -1852 + xOffset, 40, 716);
        addBlock(furnaces, -1851 + xOffset, 40, 716);
        config.set("furnaces", furnaces);

        List<Map<String, Object>> leftSideEasy = new ArrayList<>();
        addBlock(leftSideEasy, -1857 + xOffset, 40, 719);
        addBlock(leftSideEasy, -1857 + xOffset, 40, 720);
        addBlock(leftSideEasy, -1857 + xOffset, 40, 721);
        addBlock(leftSideEasy, -1857 + xOffset, 40, 722);
        addBlock(leftSideEasy, -1857 + xOffset, 40, 723);
        addBlock(leftSideEasy, -1857 + xOffset, 42, 719);
        addBlock(leftSideEasy, -1857 + xOffset, 42, 720);
        addBlock(leftSideEasy, -1857 + xOffset, 42, 721);
        addBlock(leftSideEasy, -1857 + xOffset, 42, 722);
        addBlock(leftSideEasy, -1857 + xOffset, 42, 723);
        config.set("left_side_easy", leftSideEasy);

        List<Map<String, Object>> leftSideOther = new ArrayList<>();
        addBlock(leftSideOther, -1857 + xOffset, 40, 727);
        addBlock(leftSideOther, -1857 + xOffset, 40, 728);
        addBlock(leftSideOther, -1857 + xOffset, 40, 729);
        addBlock(leftSideOther, -1857 + xOffset, 40, 730);
        addBlock(leftSideOther, -1857 + xOffset, 40, 731);
        addBlock(leftSideOther, -1857 + xOffset, 42, 727);
        addBlock(leftSideOther, -1857 + xOffset, 42, 728);
        addBlock(leftSideOther, -1857 + xOffset, 42, 729);
        addBlock(leftSideOther, -1857 + xOffset, 42, 730);
        addBlock(leftSideOther, -1857 + xOffset, 42, 731);
        config.set("left_side_other", leftSideOther);

        List<Map<String, Object>> rightSideEasy = new ArrayList<>();
        addBlock(rightSideEasy, -1841 + xOffset, 40, 719);
        addBlock(rightSideEasy, -1841 + xOffset, 40, 720);
        addBlock(rightSideEasy, -1841 + xOffset, 40, 721);
        addBlock(rightSideEasy, -1841 + xOffset, 40, 722);
        addBlock(rightSideEasy, -1841 + xOffset, 40, 723);
        addBlock(rightSideEasy, -1841 + xOffset, 42, 719);
        addBlock(rightSideEasy, -1841 + xOffset, 42, 720);
        addBlock(rightSideEasy, -1841 + xOffset, 42, 721);
        addBlock(rightSideEasy, -1841 + xOffset, 42, 722);
        addBlock(rightSideEasy, -1841 + xOffset, 42, 723);
        config.set("right_side_easy", rightSideEasy);

        List<Map<String, Object>> rightSideOther = new ArrayList<>();
        addBlock(rightSideOther, -1841 + xOffset, 40, 727);
        addBlock(rightSideOther, -1841 + xOffset, 40, 728);
        addBlock(rightSideOther, -1841 + xOffset, 40, 729);
        addBlock(rightSideOther, -1841 + xOffset, 40, 730);
        addBlock(rightSideOther, -1841 + xOffset, 40, 731);
        addBlock(rightSideOther, -1841 + xOffset, 42, 727);
        addBlock(rightSideOther, -1841 + xOffset, 42, 728);
        addBlock(rightSideOther, -1841 + xOffset, 42, 729);
        addBlock(rightSideOther, -1841 + xOffset, 42, 730);
        addBlock(rightSideOther, -1841 + xOffset, 42, 731);
        config.set("right_side_other", rightSideOther);

        List<Map<String, Object>> otherBlocks = new ArrayList<>();
        addBlock(otherBlocks, -1857 + xOffset, 46, 719);
        addBlock(otherBlocks, -1857 + xOffset, 46, 720);
        addBlock(otherBlocks, -1857 + xOffset, 46, 721);
        addBlock(otherBlocks, -1857 + xOffset, 46, 722);
        addBlock(otherBlocks, -1857 + xOffset, 46, 723);
        addBlock(otherBlocks, -1857 + xOffset, 48, 719);
        addBlock(otherBlocks, -1857 + xOffset, 48, 720);
        addBlock(otherBlocks, -1857 + xOffset, 48, 721);
        addBlock(otherBlocks, -1857 + xOffset, 48, 722);
        addBlock(otherBlocks, -1857 + xOffset, 48, 723);
        addBlock(otherBlocks, -1857 + xOffset, 48, 727);
        addBlock(otherBlocks, -1857 + xOffset, 48, 728);
        addBlock(otherBlocks, -1857 + xOffset, 48, 729);
        addBlock(otherBlocks, -1857 + xOffset, 48, 730);
        addBlock(otherBlocks, -1857 + xOffset, 48, 731);

        addBlock(otherBlocks, -1857 + xOffset, 46, 727);
        addBlock(otherBlocks, -1857 + xOffset, 46, 728);
        addBlock(otherBlocks, -1857 + xOffset, 46, 729);
        addBlock(otherBlocks, -1857 + xOffset, 46, 730);
        addBlock(otherBlocks, -1857 + xOffset, 46, 731);

        addBlock(otherBlocks, -1841 + xOffset, 46, 719);
        addBlock(otherBlocks, -1841 + xOffset, 46, 720);
        addBlock(otherBlocks, -1841 + xOffset, 46, 721);
        addBlock(otherBlocks, -1841 + xOffset, 46, 722);
        addBlock(otherBlocks, -1841 + xOffset, 46, 723);

        addBlock(otherBlocks, -1841 + xOffset, 46, 727);
        addBlock(otherBlocks, -1841 + xOffset, 46, 728);
        addBlock(otherBlocks, -1841 + xOffset, 46, 729);
        addBlock(otherBlocks, -1841 + xOffset, 46, 730);
        addBlock(otherBlocks, -1841 + xOffset, 46, 731);

        addBlock(otherBlocks, -1841 + xOffset, 48, 719);
        addBlock(otherBlocks, -1841 + xOffset, 48, 720);
        addBlock(otherBlocks, -1841 + xOffset, 48, 721);
        addBlock(otherBlocks, -1841 + xOffset, 48, 722);
        addBlock(otherBlocks, -1841 + xOffset, 48, 723);
        addBlock(otherBlocks, -1841 + xOffset, 48, 727);
        addBlock(otherBlocks, -1841 + xOffset, 48, 728);
        addBlock(otherBlocks, -1841 + xOffset, 48, 729);
        addBlock(otherBlocks, -1841 + xOffset, 48, 730);
        addBlock(otherBlocks, -1841 + xOffset, 48, 731);
        addBlock(otherBlocks, -1855 + xOffset, 46, 717);
        addBlock(otherBlocks, -1854 + xOffset, 46, 717);
        addBlock(otherBlocks, -1853 + xOffset, 46, 717);
        addBlock(otherBlocks, -1852 + xOffset, 46, 717);
        addBlock(otherBlocks, -1851 + xOffset, 46, 717);
        addBlock(otherBlocks, -1855 + xOffset, 48, 717);
        addBlock(otherBlocks, -1854 + xOffset, 48, 717);
        addBlock(otherBlocks, -1853 + xOffset, 48, 717);
        addBlock(otherBlocks, -1852 + xOffset, 48, 717);
        addBlock(otherBlocks, -1851 + xOffset, 48, 717);
        addBlock(otherBlocks, -1847 + xOffset, 46, 717);
        addBlock(otherBlocks, -1846 + xOffset, 46, 717);
        addBlock(otherBlocks, -1845 + xOffset, 46, 717);
        addBlock(otherBlocks, -1844 + xOffset, 46, 717);
        addBlock(otherBlocks, -1843 + xOffset, 46, 717);
        addBlock(otherBlocks, -1847 + xOffset, 48, 717);
        addBlock(otherBlocks, -1846 + xOffset, 48, 717);
        addBlock(otherBlocks, -1845 + xOffset, 48, 717);
        addBlock(otherBlocks, -1844 + xOffset, 48, 717);
        addBlock(otherBlocks, -1843 + xOffset, 48, 717);
        config.set("other_blocks", otherBlocks);

        List<Map<String, Object>> itemFrameGrid = new ArrayList<>();
        addBlock(itemFrameGrid, -1849 + xOffset, 39, 718);
        addBlock(itemFrameGrid, -1850 + xOffset, 42, 718);
        addBlock(itemFrameGrid, -1849 + xOffset, 42, 718);
        addBlock(itemFrameGrid, -1848 + xOffset, 42, 718);
        addBlock(itemFrameGrid, -1850 + xOffset, 41, 718);
        addBlock(itemFrameGrid, -1849 + xOffset, 41, 718);
        addBlock(itemFrameGrid, -1848 + xOffset, 41, 718);
        addBlock(itemFrameGrid, -1850 + xOffset, 40, 718);
        addBlock(itemFrameGrid, -1849 + xOffset, 40, 718);
        addBlock(itemFrameGrid, -1848 + xOffset, 40, 718);
        config.set("item_frame_grid", itemFrameGrid);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNewStationConfig(File file, int id, int xOffset) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("id", id);
        config.set("direction", "SOUTH");

        config.set("spawn.world", "world");
        config.set("spawn.x", -1852.5 + xOffset);
        config.set("spawn.y", 39);
        config.set("spawn.z", 759.5);

        config.set("corner1.world", "world");
        config.set("corner1.x", -1857 + xOffset);
        config.set("corner1.y", 38);
        config.set("corner1.z", 744);

        config.set("corner2.world", "world");
        config.set("corner2.x", -1841 + xOffset);
        config.set("corner2.y", 50);
        config.set("corner2.z", 761);

        config.set("villager.world", "world");
        config.set("villager.x", -1853 + xOffset);
        config.set("villager.y", 39);
        config.set("villager.z", 762.3);

        List<Map<String, Object>> furnaces = new ArrayList<>();
        addBlock(furnaces, -1847 + xOffset, 40, 762);
        addBlock(furnaces, -1846 + xOffset, 40, 762);
        addBlock(furnaces, -1844 + xOffset, 40, 762);
        addBlock(furnaces, -1843 + xOffset, 40, 762);
        config.set("furnaces", furnaces);

        List<Map<String, Object>> leftSideEasy = new ArrayList<>();
        addBlock(leftSideEasy, -1841 + xOffset, 40, 759);
        addBlock(leftSideEasy, -1841 + xOffset, 40, 758);
        addBlock(leftSideEasy, -1841 + xOffset, 40, 757);
        addBlock(leftSideEasy, -1841 + xOffset, 40, 756);
        addBlock(leftSideEasy, -1841 + xOffset, 40, 755);
        addBlock(leftSideEasy, -1841 + xOffset, 42, 759);
        addBlock(leftSideEasy, -1841 + xOffset, 42, 758);
        addBlock(leftSideEasy, -1841 + xOffset, 42, 757);
        addBlock(leftSideEasy, -1841 + xOffset, 42, 756);
        addBlock(leftSideEasy, -1841 + xOffset, 42, 755);
        config.set("left_side_easy", leftSideEasy);

        List<Map<String, Object>> leftSideOther = new ArrayList<>();
        addBlock(leftSideOther, -1841 + xOffset, 40, 751);
        addBlock(leftSideOther, -1841 + xOffset, 40, 750);
        addBlock(leftSideOther, -1841 + xOffset, 40, 749);
        addBlock(leftSideOther, -1841 + xOffset, 40, 748);
        addBlock(leftSideOther, -1841 + xOffset, 40, 747);
        addBlock(leftSideOther, -1841 + xOffset, 42, 751);
        addBlock(leftSideOther, -1841 + xOffset, 42, 750);
        addBlock(leftSideOther, -1841 + xOffset, 42, 749);
        addBlock(leftSideOther, -1841 + xOffset, 42, 748);
        addBlock(leftSideOther, -1841 + xOffset, 42, 747);
        config.set("left_side_other", leftSideOther);

        List<Map<String, Object>> rightSideEasy = new ArrayList<>();
        addBlock(rightSideEasy, -1857 + xOffset, 40, 759);
        addBlock(rightSideEasy, -1857 + xOffset, 40, 758);
        addBlock(rightSideEasy, -1857 + xOffset, 40, 757);
        addBlock(rightSideEasy, -1857 + xOffset, 40, 756);
        addBlock(rightSideEasy, -1857 + xOffset, 40, 755);
        addBlock(rightSideEasy, -1857 + xOffset, 42, 759);
        addBlock(rightSideEasy, -1857 + xOffset, 42, 758);
        addBlock(rightSideEasy, -1857 + xOffset, 42, 757);
        addBlock(rightSideEasy, -1857 + xOffset, 42, 756);
        addBlock(rightSideEasy, -1857 + xOffset, 42, 755);
        config.set("right_side_easy", rightSideEasy);

        List<Map<String, Object>> rightSideOther = new ArrayList<>();
        addBlock(rightSideOther, -1857 + xOffset, 40, 751);
        addBlock(rightSideOther, -1857 + xOffset, 40, 750);
        addBlock(rightSideOther, -1857 + xOffset, 40, 749);
        addBlock(rightSideOther, -1857 + xOffset, 40, 748);
        addBlock(rightSideOther, -1857 + xOffset, 40, 747);
        addBlock(rightSideOther, -1857 + xOffset, 42, 751);
        addBlock(rightSideOther, -1857 + xOffset, 42, 750);
        addBlock(rightSideOther, -1857 + xOffset, 42, 749);
        addBlock(rightSideOther, -1857 + xOffset, 42, 748);
        addBlock(rightSideOther, -1857 + xOffset, 42, 747);
        config.set("right_side_other", rightSideOther);

        List<Map<String, Object>> otherBlocks = new ArrayList<>();
        addBlock(otherBlocks, -1841 + xOffset, 46, 759);
        addBlock(otherBlocks, -1841 + xOffset, 46, 758);
        addBlock(otherBlocks, -1841 + xOffset, 46, 757);
        addBlock(otherBlocks, -1841 + xOffset, 46, 756);
        addBlock(otherBlocks, -1841 + xOffset, 46, 755);
        addBlock(otherBlocks, -1841 + xOffset, 48, 759);
        addBlock(otherBlocks, -1841 + xOffset, 48, 758);
        addBlock(otherBlocks, -1841 + xOffset, 48, 757);
        addBlock(otherBlocks, -1841 + xOffset, 48, 756);
        addBlock(otherBlocks, -1841 + xOffset, 48, 755);
        addBlock(otherBlocks, -1841 + xOffset, 46, 751);
        addBlock(otherBlocks, -1841 + xOffset, 46, 750);
        addBlock(otherBlocks, -1841 + xOffset, 46, 749);
        addBlock(otherBlocks, -1841 + xOffset, 46, 748);
        addBlock(otherBlocks, -1841 + xOffset, 46, 747);
        addBlock(otherBlocks, -1841 + xOffset, 48, 751);
        addBlock(otherBlocks, -1841 + xOffset, 48, 750);
        addBlock(otherBlocks, -1841 + xOffset, 48, 749);
        addBlock(otherBlocks, -1841 + xOffset, 48, 748);
        addBlock(otherBlocks, -1841 + xOffset, 48, 747);

        addBlock(otherBlocks, -1857 + xOffset, 46, 759);
        addBlock(otherBlocks, -1857 + xOffset, 46, 758);
        addBlock(otherBlocks, -1857 + xOffset, 46, 757);
        addBlock(otherBlocks, -1857 + xOffset, 46, 756);
        addBlock(otherBlocks, -1857 + xOffset, 46, 755);
        addBlock(otherBlocks, -1857 + xOffset, 48, 759);
        addBlock(otherBlocks, -1857 + xOffset, 48, 758);
        addBlock(otherBlocks, -1857 + xOffset, 48, 757);
        addBlock(otherBlocks, -1857 + xOffset, 48, 756);
        addBlock(otherBlocks, -1857 + xOffset, 48, 755);
        addBlock(otherBlocks, -1857 + xOffset, 46, 751);
        addBlock(otherBlocks, -1857 + xOffset, 46, 750);
        addBlock(otherBlocks, -1857 + xOffset, 46, 749);
        addBlock(otherBlocks, -1857 + xOffset, 46, 748);
        addBlock(otherBlocks, -1857 + xOffset, 46, 747);
        addBlock(otherBlocks, -1857 + xOffset, 48, 751);
        addBlock(otherBlocks, -1857 + xOffset, 48, 750);
        addBlock(otherBlocks, -1857 + xOffset, 48, 749);
        addBlock(otherBlocks, -1857 + xOffset, 48, 748);
        addBlock(otherBlocks, -1857 + xOffset, 48, 747);

        addBlock(otherBlocks, -1843 + xOffset, 46, 761);
        addBlock(otherBlocks, -1844 + xOffset, 46, 761);
        addBlock(otherBlocks, -1845 + xOffset, 46, 761);
        addBlock(otherBlocks, -1846 + xOffset, 46, 761);
        addBlock(otherBlocks, -1847 + xOffset, 46, 761);
        addBlock(otherBlocks, -1843 + xOffset, 48, 761);
        addBlock(otherBlocks, -1844 + xOffset, 48, 761);
        addBlock(otherBlocks, -1845 + xOffset, 48, 761);
        addBlock(otherBlocks, -1846 + xOffset, 48, 761);
        addBlock(otherBlocks, -1847 + xOffset, 48, 761);
        addBlock(otherBlocks, -1851 + xOffset, 46, 761);
        addBlock(otherBlocks, -1852 + xOffset, 46, 761);
        addBlock(otherBlocks, -1853 + xOffset, 46, 761);
        addBlock(otherBlocks, -1854 + xOffset, 46, 761);
        addBlock(otherBlocks, -1855 + xOffset, 46, 761);
        addBlock(otherBlocks, -1851 + xOffset, 48, 761);
        addBlock(otherBlocks, -1852 + xOffset, 48, 761);
        addBlock(otherBlocks, -1853 + xOffset, 48, 761);
        addBlock(otherBlocks, -1854 + xOffset, 48, 761);
        addBlock(otherBlocks, -1855 + xOffset, 48, 761);
        config.set("other_blocks", otherBlocks);

        List<Map<String, Object>> itemFrameGrid = new ArrayList<>();
        addBlock(itemFrameGrid, -1849 + xOffset, 39, 760);
        addBlock(itemFrameGrid, -1848 + xOffset, 42, 760);
        addBlock(itemFrameGrid, -1849 + xOffset, 42, 760);
        addBlock(itemFrameGrid, -1850 + xOffset, 42, 760);
        addBlock(itemFrameGrid, -1848 + xOffset, 41, 760);
        addBlock(itemFrameGrid, -1849 + xOffset, 41, 760);
        addBlock(itemFrameGrid, -1850 + xOffset, 41, 760);
        addBlock(itemFrameGrid, -1848 + xOffset, 40, 760);
        addBlock(itemFrameGrid, -1849 + xOffset, 40, 760);
        addBlock(itemFrameGrid, -1850 + xOffset, 40, 760);
        config.set("item_frame_grid", itemFrameGrid);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addBlock(List<Map<String, Object>> blockList, int x, int y, int z) {
        Map<String, Object> block = new HashMap<>();
        block.put("x", x);
        block.put("y", y);
        block.put("z", z);
        blockList.add(block);
    }

    public List<CraftStation> getCraftStations() {
        List<CraftStation> stations = new ArrayList<>();
        File dir = new File(plugin.getDataFolder(), "crafting_stations");
        for (File file : dir.listFiles()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);

            int id = config.getInt("id");
            String direction = config.getString("direction");

            Map<String, Object> spawnMap = config.getConfigurationSection("spawn").getValues(false);
            World world = Bukkit.getWorld((String) spawnMap.get("world"));
            double spawnX = getDoubleValue(spawnMap.get("x"));
            double spawnY = getDoubleValue(spawnMap.get("y"));
            double spawnZ = getDoubleValue(spawnMap.get("z"));
            Location spawn = new Location(world, spawnX, spawnY, spawnZ);

            Map<String, Object> corner1Map = config.getConfigurationSection("corner1").getValues(false);
            double corner1X = getDoubleValue(corner1Map.get("x"));
            double corner1Y = getDoubleValue(corner1Map.get("y"));
            double corner1Z = getDoubleValue(corner1Map.get("z"));
            Location corner1 = new Location(world, corner1X, corner1Y, corner1Z);

            Map<String, Object> corner2Map = config.getConfigurationSection("corner2").getValues(false);
            double corner2X = getDoubleValue(corner2Map.get("x"));
            double corner2Y = getDoubleValue(corner2Map.get("y"));
            double corner2Z = getDoubleValue(corner2Map.get("z"));
            Location corner2 = new Location(world, corner2X, corner2Y, corner2Z);

            List<Location> leftSideEasy = loadBlockLocations((List<Map<String, Object>>) config.getList("left_side_easy"), world);
            List<Location> leftSideOther = loadBlockLocations((List<Map<String, Object>>) config.getList("left_side_other"), world);

            List<Location> rightSideEasy = loadBlockLocations((List<Map<String, Object>>) config.getList("right_side_easy"), world);
            List<Location> rightSideOther = loadBlockLocations((List<Map<String, Object>>) config.getList("right_side_other"), world);

            List<Location> otherBlocks = loadBlockLocations((List<Map<String, Object>>) config.getList("other_blocks"), world);
            List<Location> craftGrid = loadBlockLocations((List<Map<String, Object>>) config.getList("item_frame_grid"), world);
            Map<String, Object> villagerMap = config.getConfigurationSection("villager").getValues(false);
            double villagerX = getDoubleValue(villagerMap.get("x"));
            double villagerY = getDoubleValue(villagerMap.get("y"));
            double villagerZ = getDoubleValue(villagerMap.get("z"));
            Location villagerLocation = new Location(world, villagerX, villagerY, villagerZ);
            List<Location> furnaces = loadBlockLocations((List<Map<String, Object>>) config.getList("furnaces"), world);

            stations.add(new CraftStation(id, direction, spawn, corner1, corner2, leftSideEasy, leftSideOther, rightSideEasy, rightSideOther,
                    otherBlocks, craftGrid, villagerLocation, furnaces));
        }
        return stations;
    }

    private double getDoubleValue(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Double) {
            return (Double) value;
        } else {
            throw new IllegalArgumentException("Unsupported number type: " + value.getClass());
        }
    }

    private List<Location> loadBlockLocations(List<Map<String, Object>> blockList, World world) {
        List<Location> locations = new ArrayList<>();
        for (Map<String, Object> block : blockList) {
            double x = getDoubleValue(block.get("x"));
            double y = getDoubleValue(block.get("y"));
            double z = getDoubleValue(block.get("z"));
            locations.add(new Location(world, x, y, z));
        }
        return locations;
    }
}