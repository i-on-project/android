package edu.isel.ion.android.common

import android.app.Application
import androidx.room.Room
import edu.isel.ion.android.common.db.Database
import edu.isel.ion.android.common.ionwebapi.MockIonWebAPI

/*

    This class is used to hold instances that need the singleton pattern,
    its fields are accessable from any point in the appliation.

 */
class IonApplication : Application() {

    companion object {
        var coursesRepository: CourseRepository = CourseRepository(MockIonWebAPI())
    }

    override fun onCreate() {
        super.onCreate()

        /*
         Our app runs in a single process therefore we follow
         the singleton design pattern when instantiating an
         AppDatabase object. Each RoomDatabase instance is fairly expensive.
        */
        val db = Room.databaseBuilder(
            applicationContext,
            Database::class.java,
            "app-db"
        ).build()

        /*
        Using Mocks
         */
        val webAPI = MockIonWebAPI()


        coursesRepository = CourseRepository(webAPI)
        
        


        //TODO Instanciate repositories and pass the db to them
    }

}