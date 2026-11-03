package ru.public4nt.olddrop.listener;

import ru.public4nt.olddrop.config.ConfigManager;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DeathDropListener implements Listener {

    private static final double SCATTER_TO_VELOCITY = 0.5;
    private static final double THROW_TO_VELOCITY = 0.4;

    private final ConfigManager config;

    public DeathDropListener(ConfigManager config) {
        this.config = config;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!config.isEnabled()) {
            return;
        }

        List<ItemStack> drops = event.getDrops();
        if (drops == null || drops.isEmpty()) {
            return;
        }

        List<ItemStack> snapshot = new ArrayList<>(drops);
        drops.clear();

        Player player = event.getEntity();
        Location deathLocation = player.getLocation();

        double scatter = config.getScatter() * SCATTER_TO_VELOCITY;
        double throwHeight = config.getThrowHeight() * THROW_TO_VELOCITY;
        int pickupDelay = config.getPickupDelay();

        for (ItemStack stack : snapshot) {
            if (stack == null || stack.getAmount() <= 0) {
                continue;
            }

            Item item = player.getWorld().dropItem(deathLocation, stack);

            double angle = ThreadLocalRandom.current().nextDouble() * 2.0 * Math.PI;
            item.setVelocity(new Vector(
                    Math.cos(angle) * scatter,
                    throwHeight,
                    Math.sin(angle) * scatter
            ));
            item.setPickupDelay(pickupDelay);
        }
    }
}
