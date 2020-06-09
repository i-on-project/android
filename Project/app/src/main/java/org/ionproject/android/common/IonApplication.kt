package org.ionproject.android.common

import android.app.Application
import androidx.room.Room
import org.ionproject.android.common.db.AppDatabase
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.ionwebapi.IonService
import org.ionproject.android.common.ionwebapi.IonWebAPI
import org.ionproject.android.common.ionwebapi.JacksonIonMapper
import org.ionproject.android.common.repositories.*
import org.ionproject.android.common.workers.WorkerManagerFacade
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


/**
 * This class is used to hold instances that need the singleton pattern,
 * its fields are accessible from any point in the application.
 */
class IonApplication : Application() {

    companion object {
        lateinit var programmesRepository: ProgrammesRepository private set
        lateinit var coursesRepository: CourseRepository private set
        lateinit var classesRepository: ClassesRepository private set
        lateinit var suggestionsMockRepository: SuggestionsMockRepository private set
        lateinit var db: AppDatabase private set
        lateinit var ionWebAPI: IIonWebAPI private set
        lateinit var favoritesRepository: FavoriteRepository private set
        lateinit var calendarTermRepository: CalendarTermRepository private set
        lateinit var workerRepository: WorkerRepository private set
        lateinit var workerManagerFacade: WorkerManagerFacade private set
        lateinit var eventsRepository: EventsRepository
    }

    override fun onCreate() {
        super.onCreate()

        /**
         * Our app runs in a single process therefore we follow
         * the singleton design pattern when instantiating an
         * AppDatabase object. Each RoomDatabase instance is fairly expensive.
         */
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "ion-database"
        ).build()

        // TODO Inject dependencies with dagger instead of here

        // Used to map string http responses to SirenEntities
        val ionMapper = JacksonIonMapper()

        // Using mocks
        // val webAPI = MockIonWebAPI(ionMapper)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val service: IonService = retrofit.create(IonService::class.java)
        val webAPI = IonWebAPI(service, ionMapper)

        IonApplication.db = db
        ionWebAPI = webAPI

        workerRepository =
            WorkerRepository(db.workerDao())
        workerManagerFacade = WorkerManagerFacade(applicationContext, workerRepository)

        programmesRepository =
            ProgrammesRepository(
                webAPI,
                db.programmeDao(),
                db.programmeOfferDao(),
                workerManagerFacade
            )
        coursesRepository =
            CourseRepository(webAPI, db.courseDao(), workerManagerFacade)
        classesRepository =
            ClassesRepository(
                webAPI,
                db.classSectionDao(),
                db.classSummaryDao(),
                workerManagerFacade
            )
        favoritesRepository =
            FavoriteRepository(db.favoriteDao())
        calendarTermRepository =
            CalendarTermRepository(webAPI, db.calendarTermDao(), workerManagerFacade)
        suggestionsMockRepository =
            SuggestionsMockRepository(db)
        eventsRepository =
            EventsRepository(webAPI)
    }

}
