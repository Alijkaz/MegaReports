package ir.jeykey.megareports.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Serialization {

        /**
         * Simple serialization for Bukkit#Location for storing in database
         *
         * @param location Location that will be serialized
         * @return Serialized location string
         */
        public static String serializeLocation(Location location) {
                return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + ","  + location.getYaw() + "," + location.getPitch();
        }

        /**
         * Simple deserialization for Bukkit#Location for reading from database
         *
         * @param serializedLoc Location that will be deserialized
         * @return Deserialized Bukkit#Location
         */
        public static Location deserializeLocation(String serializedLoc) {
                String[] splitted = serializedLoc.split(",");
                World world = Bukkit.getWorld(splitted[0]);

                return new Location(
                        world,
                        Double.parseDouble(splitted[1]),
                        Double.parseDouble(splitted[2]),
                        Double.parseDouble(splitted[3]),
                        Float.parseFloat(splitted[4]),
                        Float.parseFloat(splitted[5])
                );

        }


}
