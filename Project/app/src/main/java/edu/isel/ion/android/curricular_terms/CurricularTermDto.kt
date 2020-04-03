package edu.isel.ion.android.curricular_terms

/**
 *   Represents the properties of the curricular term representation in siren
 */
data class CurricularTermProperties(
    val numberOfTerms: Int
)

/**
 *  Converts from a course [SirenEntity] to [CurricularTerm]
 */
/*fun SirenEntity<CurricularTermProperties>.toCurricularTerm(): CurricularTerm {
    TODO - We must wait for an example of a curricular term representation
}*/