package ir.jeykey.megacore;

import ir.jeykey.megacore.events.InventoryClick;
import ir.jeykey.megacore.gui.MegaGUI;
import ir.jeykey.megacore.utils.Common;
import ir.jeykey.megareports.MegaReports;
import ir.jeykey.megareports.commands.ManageCommand;
import lombok.Getter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public abstract class MegaPlugin extends JavaPlugin {
        @Getter public static JavaPlugin instance;
        public static List<MegaGUI> registeredGuis = new ArrayList<>();

        @Override
        public void onEnable() {
                // For calculating
                long start = System.currentTimeMillis();

                // Assigning instance
                instance = this;

                onPluginEnable();

                // Registering core events
                register(new InventoryClick());

                // Finished loading plugin millis
                long end = System.currentTimeMillis();

                // Calculating plugin load time in milliseconds
                long time = end - start;

                // Logging MegaReports has been activated
                Common.log(
                        Common.repeat("&a&m=", 12, "&2"),
                        "&a&l" + getDescription().getName() + " &aActivated",
                        "&a&lVersion: &2" + getDescription().getVersion(),
                        "&a&lTook: &2" + time+ " ms",
                        Common.repeat("&a&m=", 12, "&2")
                );

        }

        @Override
        public void onDisable() {
                onPluginDisable();

                // Logging MegaReports has been deactivated
                Common.log(
                        Common.repeat("&c&m=", 12, "&4"),
                        "&c&l" + getDescription().getName() + " &cDeactivated",
                        Common.repeat("&c&m=", 12, "&4")
                );
        }

        public void register(String name, CommandExecutor executor) {
                getCommand(name).setExecutor(executor);
        }

        public void register(Listener listener) {
                getServer().getPluginManager().registerEvents(listener, getInstance());
        }

        public abstract void onPluginEnable();

        public abstract void onPluginDisable();
}
