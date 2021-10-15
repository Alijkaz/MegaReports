package ir.jeykey.megacore;

import ir.jeykey.megareports.utils.Common;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class MegaItem {
        @Getter private ItemStack itemStack;

        /**
         * This method is used to create ItemStack with custom name and meta
         * @param material Material Item/Block
         */
        public MegaItem(final Material material) {
                this(material, null);
        }

        /**
         * This method is used to create ItemStack with custom name and meta
         * @param material Material Item/Block
         * @param name Custom name
         */
        public MegaItem(final Material material, final String name) {
                this(material, name, "");
        }

        /**
         * This method is used to create ItemStack with custom name and meta
         * @param material Material Item/Block
         * @param name Custom name
         * @param lore Custom lore
         */
        public MegaItem(final Material material, final String name, final String... lore) {
                this.itemStack = new ItemStack(material, 1);
                ItemMeta meta = getItemStack().getItemMeta();

                meta.setDisplayName(Common.colorize(name));

                meta.setLore(Arrays.asList(Common.colorize(lore)));

                getItemStack().setItemMeta(meta);

                this.itemStack = getItemStack();
        }
}
