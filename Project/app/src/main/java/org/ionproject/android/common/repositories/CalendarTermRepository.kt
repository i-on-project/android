package org.ionproject.android.common.repositories

import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.model.toCalendarTermList
import java.net.URI

//TODO This should not be hardcoded, otherwise if its altered we have to refactor the code
private val CALENDAR_TERMS_URI = URI("/v0/calendar-terms")

class CalendarTermRepository(private val ionWebAPI: IIonWebAPI) {

    /**
     * Obtains all calendar terms from the IonWebAPI
     */
    suspend fun getAllCalendarTerm(): List<CalendarTerm> {
        return ionWebAPI
            .getFromURI(CALENDAR_TERMS_URI, SirenEntity::class.java)
            .toCalendarTermList()
    }

}