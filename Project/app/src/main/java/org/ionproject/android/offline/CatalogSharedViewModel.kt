package org.ionproject.android.offline

import androidx.lifecycle.ViewModel
import org.ionproject.android.offline.models.CatalogCalendar
import org.ionproject.android.offline.models.CatalogProgramme
import org.ionproject.android.offline.models.ExamSchedule
import org.ionproject.android.offline.models.Timetable

class CatalogSharedViewModel : ViewModel() {

    /**
     * The programme that the user chose from the list in the ProgrammesFragment (curso)
     */
    var selectedCatalogProgramme: CatalogProgramme? = null

    /**
     * The Term the user chose from the list of terms of the selected Programme (semestre)
     */
    var selectedCatalogProgrammeTerm: String = ""

    var selectedYear: String = ""

    var parsedExamSchedule: ExamSchedule? = null

    var parsedTimeTable: Timetable? = null

    var parsedCalendar: CatalogCalendar? = null
}