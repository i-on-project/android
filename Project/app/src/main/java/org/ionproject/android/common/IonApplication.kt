package org.ionproject.android.common

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import org.ionproject.android.common.connectivity.ConnectivityObservableFactory
import org.ionproject.android.common.connectivity.IConnectivityObservable
import org.ionproject.android.common.db.AppDatabase
import org.ionproject.android.common.ionwebapi.*
import org.ionproject.android.common.repositories.*
import org.ionproject.android.common.workers.WorkerManagerFacade
import org.ionproject.android.loading.RemoteConfigRepository
import org.ionproject.android.offline.CatalogRepository
import org.ionproject.android.settings.Preferences
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * This class is used to hold instances that need the singleton pattern,
 * its fields are accessible from any point in the application.
 *
 * It injects the dependencies into these classes
 */
class IonApplication : Application() {

    companion object {
        lateinit var ionMapper: JacksonIonMapper private set
        lateinit var programmesRepository: ProgrammesRepository private set
        lateinit var coursesRepository: CourseRepository private set
        lateinit var classesRepository: ClassesRepository private set
        lateinit var db: AppDatabase private set
        lateinit var ionWebAPI: IIonWebAPI private set
        lateinit var favoritesRepository: FavoriteRepository private set
        lateinit var calendarTermRepository: CalendarTermRepository private set
        lateinit var workerRepository: WorkerRepository private set
        lateinit var workerManagerFacade: WorkerManagerFacade private set
        lateinit var eventsRepository: EventsRepository
        lateinit var rootRepository: RootRepository private set
        lateinit var searchRepository: SearchRepository private set
        lateinit var globalExceptionHandler: GlobalExceptionHandler private set
        lateinit var preferences: Preferences private set
        lateinit var connectivityObservable: IConnectivityObservable private set
        lateinit var remoteConfigRepository: RemoteConfigRepository private set
        lateinit var catalogRepository: CatalogRepository private set
    }

    override fun onCreate() {
        super.onCreate()

        globalExceptionHandler = GlobalExceptionHandler()

        preferences = Preferences(applicationContext)

        /**
         * Our app runs in a single process therefore we follow
         * the singleton design pattern when instantiating an
         * AppDatabase object. Each RoomDatabase instance is fairly expensive.
         */
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "ion-database"
        ).build()

        val ionMapper = JacksonIonMapper()

        //------- Using mocked API -----------
        //val webAPI = MockIonWebAPI(ionMapper)
        //------------------------------------

        //------- Using real API -------------
        val retrofit = Retrofit.Builder()
            .baseUrl(preferences.getWebApiHost()?: WEB_API_HOST)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val service: IonService = retrofit.create(IonService::class.java)
        val webAPI = IonWebAPI(service, ionMapper)
        //------------------------------------

        ionWebAPI = webAPI

        workerRepository = WorkerRepository(db.workerDao())
        workerManagerFacade = WorkerManagerFacade(applicationContext, workerRepository)

        programmesRepository = ProgrammesRepository(
            webAPI,
            db.programmeDao(),
            db.programmeOfferDao(),
            workerManagerFacade
        )
        coursesRepository = CourseRepository(webAPI, db.courseDao(), workerManagerFacade)
        classesRepository =
            ClassesRepository(
                webAPI,
                db.classSectionDao(),
                db.classCollectionDao(),
                db.classesDao(),
                workerManagerFacade
            )
        favoritesRepository =
            FavoriteRepository(db.favoriteDao())
        calendarTermRepository =
            CalendarTermRepository(
                webAPI,
                db.calendarTermDao(),
                db.favoriteDao(),
                workerManagerFacade
            )
        eventsRepository =
            EventsRepository(db.eventsDao(), webAPI, workerManagerFacade)
        rootRepository = RootRepository(db.rootDao(), ionWebAPI, workerManagerFacade)
        searchRepository = SearchRepository(webAPI)
        connectivityObservable = ConnectivityObservableFactory.create(applicationContext)

        remoteConfigRepository = RemoteConfigRepository(preferences, ionWebAPI)

        catalogRepository = CatalogRepository(ionWebAPI)
    }
}
