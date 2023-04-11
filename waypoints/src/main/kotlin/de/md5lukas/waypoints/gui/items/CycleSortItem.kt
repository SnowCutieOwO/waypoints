package de.md5lukas.waypoints.gui.items

import de.md5lukas.kinvs.items.GUICycleItem
import de.md5lukas.waypoints.api.OverviewSort
import de.md5lukas.waypoints.gui.WaypointsGUI
import de.md5lukas.waypoints.util.text

class CycleSortItem(wpGUI: WaypointsGUI, onCycle: (OverviewSort) -> Unit) : GUICycleItem<OverviewSort>(
    getOverviewSortCycleValues(wpGUI),
    {
        wpGUI.viewerData.sortBy = it
        onCycle(it)
    }
) {

    init {
        while (wpGUI.viewerData.sortBy != currentValue) {
            cycle()
        }
    }

    private companion object {
        fun getOverviewSortCycleValues(wpGUI: WaypointsGUI) = OverviewSort.values().map { current ->
            val additionalLines = wpGUI.translations.OVERVIEW_CYCLE_SORT_OPTIONS.map {
                text {
                    val copyFrom = if (it.first === current) {
                        wpGUI.translations.OVERVIEW_CYCLE_SORT_ACTIVE_COLOR
                    } else {
                        wpGUI.translations.OVERVIEW_CYCLE_SORT_INACTIVE_COLOR
                    }.text
                    style(copyFrom.style())
                    content(it.second.rawText)
                }
            }.toList()

            val item = wpGUI.translations.OVERVIEW_CYCLE_SORT.getItem()
            item.lore(item.lore()!! + additionalLines)

            current to item
        }.toList()
    }
}