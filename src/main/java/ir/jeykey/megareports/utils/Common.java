package ir.jeykey.megareports.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.*;

public class Common {
        public static String colorize(String string) {
                return ChatColor.translateAlternateColorCodes('&', string);
        }

        public static String[] colorize(String... strings) {
                String[] translatedStrings = new String[strings.length];
                for (int i = 0; i < strings.length; i++) translatedStrings[i] = colorize(strings[i]);
                return translatedStrings;
        }

        public static void log(String... messages) {
                Bukkit.getServer().getConsoleSender().sendMessage(colorize(messages));
        }

        public static void logPrefixed(String ... messages) {
                for(String message : messages) log(YMLLoader.Messages.PREFIX + message);
        }

        public static String repeat(String string, int amount) {
                return repeat(string, amount, "");
        }

        public static String repeat(String string, int amount, String separator) {
                return String.join(separator, Collections.nCopies(amount, string));
        }

        public static void send(CommandSender sender, String string) {
                send(sender, string, true);
        }

        public static void send(CommandSender sender, String string, boolean addPrefix) {
                string = colorize(string);

                if (addPrefix) string = YMLLoader.Messages.PREFIX + string;

                sender.sendMessage(string);
        }

        public static String getBeautifiedDt() {
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                Date date = new Date(System.currentTimeMillis());
                return formatter.format(date);
        }
}
