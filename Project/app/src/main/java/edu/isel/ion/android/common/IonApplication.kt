package edu.isel.ion.android.common

import android.app.Application
import androidx.room.Room
import edu.isel.ion.android.common.ionwebapi.MockIonWebAPI

/*

    This class is used to hold instances that need the singleton pattern,
    its fields are accessible from any point in the application.

 */
class IonApplication : Application() {

    companion object {
        lateinit var coursesRepository: CourseRepository
        lateinit var classSectionRepository: ClassSectionRepository
    }

    override fun onCreate() {
        super.onCreate()

        /*
         Our app runs in a single process therefore we follow
         the singleton design pattern when instantiating an
         AppDatabase object. Each RoomDatabase instance is fairly expensive.
        */

        /*val db = Room.databaseBuilder(
            applicationContext,
            Database::class.java,
            "app-db"
        ).build()*/

        /*
        Using Mocks
         */
        val webAPI = MockIonWebAPI()

        //TODO Instanciate repositories and pass the db to them
        coursesRepository = CourseRepository(webAPI)
        classSectionRepository = ClassSectionRepository(webAPI)

    }

}