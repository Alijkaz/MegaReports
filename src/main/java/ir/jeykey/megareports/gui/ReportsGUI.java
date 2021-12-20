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
        List<Report> reportList = Report.all(getOffset() - 1, getLimit() - 1);
        setItemsCount((int) Report.count());

        for (int i = 0; i <= getMaxItemsPerPage(); i++) {
            if (reportList.size() <= i)
                break;
            AtomicReference<Report> report = new AtomicReference<>(reportList.get(i));

            MegaItem reportItem = new MegaItem(
                    Material.EMERALD,
                    "&2&l#" + report.get().getId() + " &aReport " + report.get().getTarget(),
                    "",
                    "&aServer: &2" + report.get().getServer(),
                    "&aReported At: &2" + report.get().getCreatedAt(),
                    "&aReported Closed At: &2" + report.get().getClosedAt(),
                    "&aClosed By: &2" + report.get().getClosedBy(),
                    "&aClosed Reason: &2" + report.get().getClosedReason(),
                    "",
                    "&2&l[ &aCLICK TO MANAGE &2&l]"
            );

            if (report.get().getClosedAt() == null)
                reportItem = new MegaItem(
                        Material.REDSTONE_BLOCK,
                        "&4&l#" + report.get().getId() + " &cReport " + report.get().getTarget(),
                        "",
                        "&cServer: &4" + report.get().getServer(),
                        "&cReporter: &4" + report.get().getReporter(),
                        "&cTarget: &4" + report.get().getTarget(),
                        "&cReason: &4" + report.get().getReason(),
                        "&cWorld: &4" + report.get().getWorldName(),
                        "&cXYZ: &4" + Math.round(report.get().getLocation().getX()) + "," + Math.round(report.get().getLocation().getY()) + "," + Math.round(report.get().getLocation().getZ()),
                        "&cReported At: &4" + report.get().getCreatedAt(),
                        "",
                        "&4&l[ &cCLICK TO MANAGE &4&l]"
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
                "&4Go to the previous page"
        );

        if (!isFirstPage()) {
            place(39, previousPage, (player, itemStack, slot, clickType) -> previousPage());
        }

        MegaItem closeItem = new MegaItem(
                Material.BARRIER,
                "&cClose",
                "",
                "&4Close Management Menu"
        );
        place(40, closeItem, (player, itemStack, slot, clickType) -> close());

        MegaItem nextPage = new MegaItem(
                Material.STONE_BUTTON,
                "&aNext Page",
                "",
                "&2Go to the next page"
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
