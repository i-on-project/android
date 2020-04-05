package edu.isel.ion.android.common

import edu.isel.ion.android.class_section.ClassSectionProperties
import edu.isel.ion.android.class_section.toClassSection
import edu.isel.ion.android.common.ionwebapi.IIonWebAPI
import edu.isel.ion.android.common.model.ClassSection
import java.net.URI

typealias SirenClassSection = SirenEntity<ClassSectionProperties>

class ClassSectionRepository(private val ionWebAPI: IIonWebAPI) {

    suspend fun getClassSection(
        course: String?,
        calendarTerm: String?,
        klass: String?
    ): ClassSection {
        val uri = URI("/v0/courses/$course/classes/$calendarTerm/$klass")
        return ionWebAPI.getFromURI(uri, SirenClassSection()).toClassSection()
    }

}