package ir.jeykey.megareports;

import ir.jeykey.megareports.utils.Common;
import lombok.Getter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public final class MegaReports extends JavaPlugin {
        @Getter public static MegaReports instance;

        @Override
        public void onEnable() {
                // Assigning instance
                instance = this;

                // Logging MegaReports has been activated
                Common.log(
                        Common.repeat("&a&m=", 6, "&2"),
                        "&a&lMegaReports &aActivated",
                        "&a&lVersion: &2" + getDescription().getVersion(),
                        Common.repeat("&a&m=", 6, "&2")
                );
        }

        @Override
        public void onDisable() {
                // Plugin shutdown logic
        }
}
