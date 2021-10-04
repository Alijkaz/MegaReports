package ir.jeykey.megareports.config;

public class Discord extends Configurable {
        public static boolean DISCORD_ENABLED;
        public static String DISCORD_WEBHOOK;
        public static String EMBED_TITLE;
        public static String EMBED_DESCRIPTION;
        public static String EMBED_FOOTER;
        public static String EMBED_THUMBNAIL;

        public Discord() {
                super("discord.yml");
        }

        @Override
        public void init() {
                DISCORD_ENABLED = getConfig().getBoolean("discord.enable");
                DISCORD_WEBHOOK = getConfig().getString("discord.webhook-url");
                EMBED_TITLE = getConfig().getString("discord.embed.title");
                EMBED_DESCRIPTION = getConfig().getString("discord.embed.description");
                EMBED_FOOTER = getConfig().getString("discord.embed.footer");
                EMBED_THUMBNAIL = getConfig().getString("discord.embed.thumbnail-url");
        }
}
