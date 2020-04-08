package org.ionproject.android.common

import android.app.Application
import org.ionproject.android.common.ionwebapi.JacksonIonMapper
import org.ionproject.android.common.ionwebapi.MockIonWebAPI
import org.ionproject.android.common.repositories.ClassesRepository
import org.ionproject.android.common.repositories.CourseRepository

/**
 * This class is used to hold instances that need the singleton pattern,
 * its fields are accessible from any point in the application.
 */
class IonApplication : Application() {

    companion object {
        lateinit var coursesRepository: CourseRepository
        lateinit var classesRepository: ClassesRepository
    }

    override fun onCreate() {
        super.onCreate()

        /**
         * Our app runs in a single process therefore we follow
         * the singleton design pattern when instantiating an
         * AppDatabase object. Each RoomDatabase instance is fairly expensive.
         */

        //TODO: Create an instance of RoomDatabase

        //TODO Inject dependencies with dagger instead of here

        //Used to map string http responses to SirenEntities
        val ionMapper = JacksonIonMapper()

        //Using mocks
        val webAPI = MockIonWebAPI(ionMapper)

        //TODO: Pass the database to the repositories
        coursesRepository =
            CourseRepository(webAPI)
        classesRepository =
            ClassesRepository(webAPI)
    }

}