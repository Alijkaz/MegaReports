package ir.jeykey.megareports.config;

import ir.jeykey.megacore.MegaPlugin;
import ir.jeykey.megacore.config.Configurable;

public class Discord extends Configurable {
        public static boolean DISCORD_ENABLED;
        public static String DISCORD_WEBHOOK;
        public static String EMBED_TITLE;
        public static String EMBED_DESCRIPTION;
        public static String EMBED_FOOTER;
        public static String EMBED_THUMBNAIL;

        public Discord(MegaPlugin plugin) {
                super(plugin,"discord.yml");
        }

        @Override
        public void init() {
                DISCORD_ENABLED = getConfig().getBoolean("enabled");
                DISCORD_WEBHOOK = getConfig().getString("webhook-url");
                EMBED_TITLE = getConfig().getString("embed.title");
                EMBED_DESCRIPTION = getConfig().getString("embed.description");
                EMBED_FOOTER = getConfig().getString("embed.footer");
                EMBED_THUMBNAIL = getConfig().getString("embed.thumbnail-url");
        }
}
