package ir.jeykey.megareports.gui;

import ir.jeykey.megacore.gui.MegaPaginatedGui;
import ir.jeykey.megacore.utils.Common;
import ir.jeykey.megacore.utils.MegaItem;
import ir.jeykey.megareports.config.Messages;
import ir.jeykey.megareports.database.models.Report;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ReportsGUI extends MegaPaginatedGui {
    public ReportsGUI(Player owner) {
        super(Common.colorize("&cReports Management"), 45, owner);
        setMaxItemsPerPage(36);
        setFiller(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14));
    }

    @Override
    public void setup() {
        List<Report> reportList = Report.all(getOffset() - 1, getLimit());
        setItemsCount((int) Report.count());

        for (int i = 0; i <= getMaxItemsPerPage(); i++) {
            if (reportList.size() <= i)
                break;
            AtomicReference<Report> report = new AtomicReference<>(reportList.get(i));

            MegaItem reportItem = new MegaItem(
                    Material.EMERALD,
                    "&2&l#" + report.get().getId() + " &aReport " + report.get().getTarget(),
                    "",
                    "&aServer &a» &2" + report.get().getServer(),
                    "&aReported At &a» &2" + report.get().getCreatedAt(),
                    "&aReported Closed At &a» &2" + report.get().getClosedAt(),
                    "&aClosed By &a» &2" + report.get().getClosedBy(),
                    "&aClosed Reason &a» &2" + report.get().getClosedReason(),
                    "",
                    "&a● &2CLICK TO MANAGE &a●"
            );

            if (report.get().getClosedAt() == null)
                reportItem = new MegaItem(
                        Material.REDSTONE_BLOCK,
                        "&4&l#" + report.get().getId() + " &cReport " + report.get().getTarget(),
                        "",
                        "&cServer &4» &c" + report.get().getServer(),
                        "&cReporter &4» &c" + report.get().getReporter(),
                        "&cTarget &4» &c" + report.get().getTarget(),
                        "&cReason &4» &c" + report.get().getReason(),
                        "&cWorld &4» &c" + report.get().getWorldName(),
                        "&cXYZ &4» &c" + Math.round(report.get().getLocation().getX()) + "," + Math.round(report.get().getLocation().getY()) + "," + Math.round(report.get().getLocation().getZ()),
                        "&cReported At &4» &c" + report.get().getCreatedAt(),
                        "",
                        "&4● &cCLICK TO MANAGE &4●"
                );

            place(i, reportItem, (player, itemStack, slot, clickType) -> {
                Common.send(player, Messages.MANAGEMENT_MANAGING_REPORT.replace("%id%", Integer.toString(report.get().getId())));
                new ManageReportGUI(report.get(), player).open();
            });
        }



        MegaItem previousPage = new MegaItem(
                Material.STONE_BUTTON,
                "&cPrevious Page",
                "",
                "&c● &4Go to the previous page &c●"
        );

        if (!isFirstPage()) {
            place(39, previousPage, (player, itemStack, slot, clickType) -> previousPage());
        }

        MegaItem closeItem = new MegaItem(
                Material.BARRIER,
                "&cClose",
                "",
                "&c● &4Close Management Menu &c●"
        );
        place(40, closeItem, (player, itemStack, slot, clickType) -> close());

        MegaItem nextPage = new MegaItem(
                Material.STONE_BUTTON,
                "&aNext Page",
                "",
                "&a● &2Go to the next page &a●"
        );
        if(hasMorePages()) {
            place(41, nextPage, (player, itemStack, slot, clickType) -> nextPage());
        }

        for (int i = 36; i < 45; i++) {
            if (getSlot(i) == null) {
                place(i, getFiller());
            }
        }
    }

}
