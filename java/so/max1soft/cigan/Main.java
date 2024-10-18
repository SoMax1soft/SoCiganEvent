package so.max1soft.cigan;

import com.earth2me.essentials.Essentials;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main extends JavaPlugin {
    private BukkitTask villagerSpawnTask;
    private BossBarManager bossBarManager;
    private WorldGuardPlugin worldGuardPlugin;
    private Essentials essentials;
    String name = this.getDescription().getName();

    @Override
    public void onEnable() {
        getLogger().info("");
        getLogger().info("§fПлагин: §aЗапущен");
        getLogger().info("§fСоздатель: §b@max1soft");
        getLogger().info("§fВерсия: §c1.3");
        getLogger().info("");
        anus();
        Plugin essentialsPlugin = Bukkit.getPluginManager().getPlugin("Essentials");
        if (essentialsPlugin instanceof Essentials) {
            essentials = (Essentials) essentialsPlugin;
        } else {
            getLogger().warning("Плагин требует ESSENTIALS.");
            onDisable();
        }
        saveDefaultConfig();
        getCommand("sociganevent").setExecutor(new CommandHandler(this));
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (plugin instanceof WorldGuardPlugin) {
            worldGuardPlugin = (WorldGuardPlugin) plugin;
        } else {
            getLogger().warning("Плагин требует WORLDGUARD.");
            worldGuardPlugin = null;
            plugin.onDisable();
        }
        int interval = getConfig().getInt("Villager.spawn-interval", 7200);
        bossBarManager = new BossBarManager(this);

        VillagerSpawnTask villager = new VillagerSpawnTask(this, worldGuardPlugin, bossBarManager);
        villagerSpawnTask = villager.runTaskTimer(this, interval * 20L, interval * 20L);
        Bukkit.getPluginManager().registerEvents(new VillagerListener(this, worldGuardPlugin, essentials, villager), this);
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "Плагин отключен.");
        if (villagerSpawnTask != null) {
            villagerSpawnTask.cancel();
        }
        anus();
    }

    public void anus() {
        removeVillagers();
        if (worldGuardPlugin != null) {
            RegionManager regionManager = worldGuardPlugin.getRegionManager(Bukkit.getWorld("world"));
            if (regionManager != null) {
                regionManager.removeRegion(getConfig().getString("Villager.regioname"));
            }
        }
    }

    public void removeVillagers() {
        for (Entity entity : Bukkit.getWorld("world").getEntities()) {
            if (entity instanceof Villager) {
                Villager villager = (Villager) entity;
                if (villager.getCustomName() != null && villager.getCustomName().contains(getConfig().getString("Villager.Name"))) {
                    villager.remove();
                }
            }
        }
    }

    public void startVillagerSpawn() {
        if (villagerSpawnTask != null && !villagerSpawnTask.isCancelled()) {
            villagerSpawnTask.cancel();
        }
        int interval = getConfig().getInt("Villager.spawn-interval", 7200);
        villagerSpawnTask = new VillagerSpawnTask(this, worldGuardPlugin, bossBarManager).runTaskTimer(this, 1, interval * 20L);
    }

    public BossBarManager getBossBarManager() {
        return bossBarManager;
    }
}
