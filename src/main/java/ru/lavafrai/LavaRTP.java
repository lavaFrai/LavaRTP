package ru.lavafrai;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.Random;


public class LavaRTP extends JavaPlugin implements Listener {
    private static final int maximalRange = 1000;
    private static final Material[] unsafeBlocks = {
            Material.MAGMA_BLOCK,
            Material.CACTUS,
            Material.TNT
    };

    @Getter
    private static LavaRTP instance;

    @Getter
    private static Logger jlogger;

    private final Random RandomGenerator = new Random();

    @Override
    public void onEnable() {
        instance = this;
        jlogger = super.getLogger();

        Bukkit.getPluginManager().registerEvents(this, this);

        jlogger.info("LavaRTP loaded success");
    }

    private boolean isBlockSafe(Block block) {
        return  block.isSolid() &&
                !Arrays.asList(unsafeBlocks).contains(block.getType());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("rtp")) {
            int trysCounter = 0;
            Player player = (Player) sender;

            while (trysCounter < 5) {
                Location new_location = new Location(player.getWorld(),
                        RandomGenerator.nextInt(maximalRange * 2) - maximalRange,
                        0,
                        RandomGenerator.nextInt(maximalRange * 2) - maximalRange);
                Block target = player.getWorld().getHighestBlockAt(new_location);

                if (isBlockSafe(target)) {
                    new_location.setY(player.getWorld().getHighestBlockYAt(new_location) + 1);
                    player.teleport(new_location);
                    return true;
                }

                trysCounter++;
            }

            player.sendMessage(Component.text()
                    .append(Component.text("[Error]", TextColor.fromHexString("#ff2020")))
                    .append(Component.text(" in /rtp: Can not found safety position")
                    ));
        }

        return false;
    }
}
