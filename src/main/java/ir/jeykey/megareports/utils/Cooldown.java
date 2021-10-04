package ir.jeykey.megareports.utils;

import ir.jeykey.megareports.config.Config;
import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Cooldown {
        @Getter
        public static HashMap<UUID, Long> cooldown = new HashMap<>();

        public static Long getCooldownSeconds(UUID uuid) {
                if (cooldown.containsKey(uuid)) {
                        return TimeUnit.MILLISECONDS.toSeconds( (Config.COOLDOWN * 1000) - (System.currentTimeMillis() - cooldown.get(uuid)) );
                }
                return null;
        }

        public static boolean isCooldown(UUID uuid)
        {
                if (cooldown.containsKey(uuid)) {
                        if (System.currentTimeMillis() - cooldown.get(uuid) > Config.COOLDOWN * 1000) {
                                cooldown.replace(uuid, System.currentTimeMillis());
                                return false;
                        } else {
                                return true;
                        }
                } else {
                        cooldown.put(uuid, System.currentTimeMillis());
                        return false;
                }
        }


}
