package ru.public4nt.olddrop.listener;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import ru.public4nt.olddrop.config.ConfigManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DeathDropListener implements Listener {

    private final ConfigManager configManager;
    private final Logger logger;

    public DeathDropListener(ConfigManager configManager, Logger logger) {
        this.configManager = configManager;
        this.logger = logger;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        ConfigManager.Settings settings = configManager.settings();
        if (!settings.enabled()) {
            return;
        }

        List<ItemStack> drops = event.getDrops();
        if (drops.isEmpty()) {
            return;
        }

        Player player = event.getEntity();
        World world = player.getWorld();
        Location deathLocation = player.getLocation();

        double horizontalSpeed = settings.scatter() * 0.5D;
        double verticalSpeed = settings.throwHeight() * 0.4D;
        int pickupDelay = settings.pickupDelay();

        List<ItemStack> snapshot = new ArrayList<>(drops);
        drops.clear();

        List<ItemStack> failed = new ArrayList<>(0);

        ThreadLocalRandom rng = ThreadLocalRandom.current();
        for (ItemStack stack : snapshot) {
            if (stack == null || stack.getType().isAir()) {
                continue;
            }

            try {
                Item item = world.dropItem(deathLocation, stack.clone());
                if (item == null) {
                    failed.add(stack);
                    continue;
                }

                item.setPickupDelay(pickupDelay);

                double angle = rng.nextDouble(Math.PI * 2.0D);
                item.setVelocity(new Vector(
                        Math.cos(angle) * horizontalSpeed,
                        verticalSpeed,
                        Math.sin(angle) * horizontalSpeed
                ));
            } catch (RuntimeException ex) {
                failed.add(stack);
                logger.log(Level.WARNING,
                        "Не удалось выбросить " + stack.getType()
                                + " игрока " + player.getName(),
                        ex);
            }
        }

        if (!failed.isEmpty()) {
            drops.addAll(failed);
        }
    }
}