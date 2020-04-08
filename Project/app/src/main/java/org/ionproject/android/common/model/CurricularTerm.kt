package org.ionproject.android.common.model

import java.net.URI

/**
 * This type represents a CurricularTerm in the context of this application.
 * This type still contains the most relevant links which came from the
 * siren representation which means we don't need to have hard-coded
 * uris.
 */
data class CurricularTerm(
    val numberOfTerms: Int,
    val termURIs: Map<String, URI>?
)
