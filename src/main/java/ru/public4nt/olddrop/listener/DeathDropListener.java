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

        Player player = event.getEntity();
        List<ItemStack> drops = event.getDrops();

        if (drops == null || drops.isEmpty()) {
            return;
        }

        List<ItemStack> snapshot = new ArrayList<>(drops);
        drops.clear();

        Location deathLoc = player.getLocation();
        double scatter = config.getScatter();
        double throwHeight = config.getThrowHeight();
        int pickupDelay = config.getPickupDelay();

        for (ItemStack stack : snapshot) {
            if (stack == null || stack.getAmount() <= 0) {
                continue;
            }

            Item item = player.getWorld().dropItem(deathLoc, stack);

            double angle = ThreadLocalRandom.current().nextDouble() * 2.0 * Math.PI;
            double horizontalForce = scatter * SCATTER_TO_VELOCITY;
            double vx = Math.cos(angle) * horizontalForce;
            double vz = Math.sin(angle) * horizontalForce;

            double vy = throwHeight * THROW_TO_VELOCITY;

            item.setVelocity(new Vector(vx, vy, vz));
            item.setPickupDelay(pickupDelay);
        }
    }
}
