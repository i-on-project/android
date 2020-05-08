package org.ionproject.android.common

import android.app.Application
import androidx.room.Room
import org.ionproject.android.common.db.AppDatabase
import org.ionproject.android.common.ionwebapi.JacksonIonMapper
import org.ionproject.android.common.ionwebapi.MockIonWebAPI
import org.ionproject.android.common.repositories.*

/**
 * This class is used to hold instances that need the singleton pattern,
 * its fields are accessible from any point in the application.
 */
class IonApplication : Application() {

    companion object {
        lateinit var coursesRepository: CourseRepository
        lateinit var classesRepository: ClassesRepository
        lateinit var suggestionsMockRepository: SuggestionsMockRepository
        lateinit var db: AppDatabase
        lateinit var favoritesRepository: FavoriteRepository
        lateinit var calendarTermRepository: CalendarTermRepository
        lateinit var eventsRepository: EventsRepository
    }

    override fun onCreate() {
        super.onCreate()

        /**
         * Our app runs in a single process therefore we follow
         * the singleton design pattern when instantiating an
         * AppDatabase object. Each RoomDatabase instance is fairly expensive.
         */
        db = Room
            .databaseBuilder(applicationContext, AppDatabase::class.java, "database-name")
            .build()

        //TODO Inject dependencies with dagger instead of here

        //Used to map string http responses to SirenEntities
        val ionMapper = JacksonIonMapper()

        //Using mocks
        val webAPI = MockIonWebAPI(ionMapper)

        //TODO: Pass the database to the repositories
        coursesRepository = CourseRepository(webAPI)
        classesRepository = ClassesRepository(webAPI, db.ClassSectionDao())
        suggestionsMockRepository = SuggestionsMockRepository(db)
        favoritesRepository = FavoriteRepository(db.FavoriteDao())
        calendarTermRepository = CalendarTermRepository(webAPI)
        eventsRepository = EventsRepository(webAPI)
    }

}