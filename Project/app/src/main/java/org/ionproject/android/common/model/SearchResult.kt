package org.ionproject.android.common.model

import androidx.navigation.NavController
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import java.net.URI

/**
 * This type represents a Search Result from the i-on Core API as documented here:
 * https://github.com/i-on-project/core/blob/master/docs/api/search.md
 *
 * Even though the api support search for calendar-terms and class, we decided to opt those
 * out because there is no area of the application which can correctly present those results.
 * The 3 types of result which we utilize are:
 * - ClassSection
 * - CourseDetails
 * - ProgrammeDetails
 */
abstract class SearchResult(
    val properties: Map<String, String>,
    val resourceURI: URI
) {
    /**
     * We need to override equals method, in order to know how each
     * SearchResult is equal to another SearchResult.
     * This comparison is useful for PageListAdapter.
     */
    override operator fun equals(other: Any?) : Boolean {
        if(other !is SearchResult)
            return false
        return resourceURI == other.resourceURI
    }


    /**
     * Navigates to the area of the application which represents
     * this result. Should pass the [resourceURI] to [sharedViewModel]
     * and navigate to the area of the application using the [navController]
     */
    abstract fun navigateToResource(navController: NavController, sharedViewModel: SharedViewModel)
}

class ClassSectionResult(
    properties: Map<String, String>,
    resourceURI: URI
) : SearchResult(properties, resourceURI) {
    override fun navigateToResource(
        navController: NavController,
        sharedViewModel: SharedViewModel
    ) {
        sharedViewModel.classSectionUri = resourceURI
        navController.navigate(R.id.navigation_class_section)
    }
}

class CourseDetailsResult(
    properties: Map<String, String>,
    resourceURI: URI
) : SearchResult(properties, resourceURI) {
    override fun navigateToResource(
        navController: NavController,
        sharedViewModel: SharedViewModel
    ) {
        sharedViewModel.courseDetailsUri = resourceURI
        navController.navigate(R.id.navigation_course_details)
    }
}

class ProgrammeDetailsResult(
    properties: Map<String, String>,
    resourceURI: URI
) : SearchResult(properties, resourceURI) {
    override fun navigateToResource(
        navController: NavController,
        sharedViewModel: SharedViewModel
    ) {
        sharedViewModel.programmeDetailsUri = resourceURI
        navController.navigate(R.id.navigation_programme_details)
    }
}

enum class SearchResultType {
    COURSE {
        override fun toString(): String {
            return "course"
        }
    },
    PROGRAMME {
        override fun toString(): String {
            return "programme"
        }
    },
    CLASS_SECTION {
        override fun toString(): String {
            return "class-section"
        }
    };

}



