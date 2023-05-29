package de.md5lukas.waypoints.gui.pages

import de.md5lukas.commons.collections.PaginationList
import de.md5lukas.kinvs.GUIPattern
import de.md5lukas.kinvs.items.GUIItem
import de.md5lukas.waypoints.api.Folder
import de.md5lukas.waypoints.api.Waypoint
import de.md5lukas.waypoints.gui.WaypointsGUI

class MoveToFolderPage(wpGUI: WaypointsGUI, private val waypoint: Waypoint) :
    ListingPage<Folder>(wpGUI, wpGUI.extendApi { waypoint.type.getBackgroundItem() }) {

  override suspend fun getContent() =
      PaginationList<Folder>(PAGINATION_LIST_PAGE_SIZE).also {
        it.addAll(wpGUI.getHolderForType(waypoint.type).getFolders())
      }

  override suspend fun toGUIContent(value: Folder) =
      wpGUI.extendApi {
        GUIItem(value.getItem(wpGUI.viewer)) {
          wpGUI.skedule {
            waypoint.setFolder(value)
            wpGUI.goBack()
          }
        }
      }

  private companion object {
    /** p = Previous g = No folder b = Back n = Next */
    val controlsPattern = GUIPattern("p__g_b__n")
  }

  private fun updateControls() {
    applyPattern(
        controlsPattern,
        4,
        0,
        background,
        'p' to GUIItem(wpGUI.translations.GENERAL_PREVIOUS.item) { previousPage() },
        'n' to GUIItem(wpGUI.translations.GENERAL_NEXT.item) { nextPage() },
        'g' to
            GUIItem(wpGUI.translations.SELECT_FOLDER_NO_FOLDER.item) {
              wpGUI.skedule {
                waypoint.setFolder(null)
                wpGUI.goBack()
              }
            },
        'b' to GUIItem(wpGUI.translations.GENERAL_BACK.item) { wpGUI.goBack() })
  }

  override suspend fun init() {
    super.init()
    updateListingInInventory()
    updateControls()
  }
}
