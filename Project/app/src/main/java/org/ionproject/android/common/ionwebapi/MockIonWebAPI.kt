package org.ionproject.android.common.ionwebapi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.siren.SirenEntity
import java.net.URI
import java.net.URISyntaxException

private const val COURSES_PATH_V0 = "/v0/courses"
private const val CLASSES_PATH = "/classes"
private const val COURSES_PATH = "/courses"
private const val CALENDAR_COMPONENTS = "calendar/components"
private const val CALENDAR_TERMS_PATH_V0 = "/v0/calendar-terms"

class MockIonWebAPI(private val ionMapper: IIonMapper) : IIonWebAPI {

    override suspend fun getFromURI(uri: URI): SirenEntity {
        val responseBody: String = get(uri)
        return ionMapper.parse(responseBody)
    }

    /**
     *  This is using the IO Dispacher, which is optimized to perform disk or network I/O
     *  outside of the main thread. Examples include using the Room component,
     *  reading from or writing to files, and running any network operations.
     */
    private suspend fun get(uri: URI): String =
        withContext(Dispatchers.IO) {
            route(uri)
        }

    private fun route(uri: URI): String = when (uri.path) {
        COURSES_PATH_V0 -> allCoursesMock
        "${COURSES_PATH}/pg" -> pgMock
        "${COURSES_PATH}/e" -> eMock
        "${COURSES_PATH}/lsd" -> lsdMock
        "${COURSES_PATH}/m1" -> m1Mock
        "${COURSES_PATH}/alga" -> algaMock
        "${COURSES_PATH}/alga${CLASSES_PATH}/1920v" -> algaClassesMock
        "${COURSES_PATH}/pg${CLASSES_PATH}/1920v" -> pgClassesMock
        "${COURSES_PATH}/lsd${CLASSES_PATH}/1920v" -> lsdClassesMock
        "${COURSES_PATH}/m1${CLASSES_PATH}/1920v" -> m1ClassesMock
        "${COURSES_PATH}/e${CLASSES_PATH}/1920v" -> eClassesMock
        "${COURSES_PATH_V0}/alga${CLASSES_PATH}/1920v/1d" -> algaClassSection1DMock
        "${COURSES_PATH_V0}/alga${CLASSES_PATH}/1920v/1n" -> algaClassSection1NMock
        "${COURSES_PATH_V0}/pg${CLASSES_PATH}/1920v/1n" -> pgClassSection1NMock
        "${COURSES_PATH_V0}/lsd${CLASSES_PATH}/1920v/1d" -> lsdClassSection1DMock
        "${COURSES_PATH_V0}/lsd${CLASSES_PATH}/1920v/2d" -> lsdClassSection2DMock
        "${COURSES_PATH_V0}/lsd${CLASSES_PATH}/1920v/1n" -> lsdClassSection1NMock
        "${COURSES_PATH_V0}/m1${CLASSES_PATH}/1920v/1d" -> m1ClassSection1DMock
        "${COURSES_PATH_V0}/m1${CLASSES_PATH}/1920v/1n" -> m1ClassSection1NMock
        "${COURSES_PATH_V0}/e${CLASSES_PATH}/1920v/1d" -> eClassSection1DMock
        "${COURSES_PATH_V0}/e${CLASSES_PATH}/1920v/1n" -> eClassSection1NMock
        "${COURSES_PATH_V0}/pg${CLASSES_PATH}/1920v/${CALENDAR_COMPONENTS}/6854" -> pg1stExam
        "${COURSES_PATH_V0}/pg${CLASSES_PATH}/1920v/${CALENDAR_COMPONENTS}/6855" -> pg2ndExam
        "${COURSES_PATH_V0}/lsd${CLASSES_PATH}/1920v/${CALENDAR_COMPONENTS}/6856" -> lsd1stExam
        "${COURSES_PATH_V0}/lsd${CLASSES_PATH}/1920v/${CALENDAR_COMPONENTS}/6857" -> lsd2ndExam
        "${COURSES_PATH_V0}/alga${CLASSES_PATH}/1920v/${CALENDAR_COMPONENTS}/6858" -> alga1stExam
        "${COURSES_PATH_V0}/alga${CLASSES_PATH}/1920v/${CALENDAR_COMPONENTS}/6859" -> alga2ndExam
        "${COURSES_PATH_V0}/m1${CLASSES_PATH}/1920v/${CALENDAR_COMPONENTS}/6860" -> m11stExam
        "${COURSES_PATH_V0}/m1${CLASSES_PATH}/1920v/${CALENDAR_COMPONENTS}/6861" -> m12ndExam
        "${COURSES_PATH_V0}/e${CLASSES_PATH}/1920v/${CALENDAR_COMPONENTS}/6862" -> e1stExam
        "${COURSES_PATH_V0}/e${CLASSES_PATH}/1920v/${CALENDAR_COMPONENTS}/6863" -> e2ndExam
        CALENDAR_TERMS_PATH_V0 -> calendarTermsMock
        else -> throw URISyntaxException(uri.path, "Uri not implemented or invalid")
    }

}

/**
 *  Computer generated mocks
 */

// All courses
private const val allCoursesMock =
    "{\"class\":[\"course\",\"collection\"],\"properties\":{},\"entities\":[{\"class\":[\"course\"],\"rel\":[\"item\"],\"properties\":{\"acronym\":\"PG\"},\"links\":[{\"rel\":[\"self\"],\"href\":\"/courses/pg\"},{\"rel\":[\"current\"],\"href\":\"/v0/courses/pg/classes/1920v\"},{\"rel\":[\"collection\"],\"href\":\"/courses\"}]},{\"class\":[\"class\"],\"rel\":[\"item\"],\"properties\":{\"acronym\":\"E\"},\"links\":[{\"rel\":[\"self\"],\"href\":\"/courses/e\"},{\"rel\":[\"current\"],\"href\":\"/v0/courses/e/classes/1920v\"},{\"rel\":[\"collection\"],\"href\":\"/courses\"}]},{\"class\":[\"class\"],\"rel\":[\"item\"],\"properties\":{\"acronym\":\"LSD\"},\"links\":[{\"rel\":[\"self\"],\"href\":\"/courses/lsd\"},{\"rel\":[\"current\"],\"href\":\"/v0/courses/lsd/classes/1920v\"},{\"rel\":[\"collection\"],\"href\":\"/courses\"}]},{\"class\":[\"class\"],\"rel\":[\"item\"],\"properties\":{\"acronym\":\"M1\"},\"links\":[{\"rel\":[\"self\"],\"href\":\"/courses/m1\"},{\"rel\":[\"current\"],\"href\":\"/v0/courses/m1/classes/1920v\"},{\"rel\":[\"collection\"],\"href\":\"/courses\"}]},{\"class\":[\"class\"],\"rel\":[\"item\"],\"properties\":{\"acronym\":\"ALGA\"},\"links\":[{\"rel\":[\"self\"],\"href\":\"/courses/alga\"},{\"rel\":[\"current\"],\"href\":\"/v0/courses/alga/classes/1920v\"},{\"rel\":[\"collection\"],\"href\":\"/courses\"}]}],\"actions\":[{\"name\":\"add-item\",\"title\":\"Add a new Course\",\"method\":\"POST\",\"href\":\"/v0/courses\",\"isTemplated\":false,\"type\":\"application/json\",\"fields\":[]},{\"name\":\"search\",\"title\":\"Search items\",\"method\":\"GET\",\"href\":\"/v0/courses\",\"isTemplated\":true,\"type\":\"application/vnd.siren+json\",\"fields\":[]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/courses?page=1&limit=2\"},{\"rel\":[\"next\"],\"href\":\"/courses?page=2&limit=2\"},{\"rel\":[\"previous\"],\"href\":\"/courses?page=0&limit=2\"}]}"

// Courses
private const val pgMock =
    "{\"class\":[\"course\"],\"properties\":{\"acronym\":\"PG\",\"name\":\"Programação\"},\"entities\":[{\"class\":[\"class\",\"collection\"],\"rel\":[\"class\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/courses/pg/classes\"},{\"rel\":[\"course\"],\"href\":\"/courses/pg\"}]},{\"class\":[\"event\",\"collection\"],\"rel\":[\"event\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/courses/pg/events\"},{\"rel\":[\"course\"],\"href\":\"/courses/pg\"}]}],\"actions\":[{\"name\":\"delete\",\"title\":\"Delete course\",\"method\":\"DELETE\",\"isTemplated\":false,\"href\":\"/v0/courses/pg\",\"fields\":[]},{\"name\":\"edit\",\"title\":\"Edit course\",\"method\":\"PATCH\",\"isTemplated\":false,\"type\":\"application/json\",\"href\":\"/v0/courses/pg\",\"fields\":[]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/courses/pg\"},{\"rel\":[\"current\"],\"href\":\"/v0/courses/pg/classes/1920v\"},{\"rel\":[\"collection\"],\"href\":\"/courses\"}]}"
private const val eMock =
    "{\"class\":[\"course\"],\"properties\":{\"acronym\":\"E\",\"name\":\"Eletrónica\"},\"entities\":[{\"class\":[\"class\",\"collection\"],\"rel\":[\"class\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/courses/e/classes\"},{\"rel\":[\"course\"],\"href\":\"/courses/e\"}]},{\"class\":[\"event\",\"collection\"],\"rel\":[\"event\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/courses/e/events\"},{\"rel\":[\"course\"],\"href\":\"/courses/e\"}]}],\"actions\":[{\"name\":\"delete\",\"title\":\"Delete course\",\"method\":\"DELETE\",\"isTemplated\":false,\"href\":\"/v0/courses/e\",\"fields\":[]},{\"name\":\"edit\",\"title\":\"Edit course\",\"method\":\"PATCH\",\"isTemplated\":false,\"type\":\"application/json\",\"href\":\"/v0/courses/e\",\"fields\":[]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/courses/e\"},{\"rel\":[\"current\"],\"href\":\"/v0/courses/e/classes/1920v\"},{\"rel\":[\"collection\"],\"href\":\"/courses\"}]}"
private const val lsdMock =
    "{\"class\":[\"course\"],\"properties\":{\"acronym\":\"LSD\",\"name\":\"Lógica de Sistemas Digitais\"},\"entities\":[{\"class\":[\"class\",\"collection\"],\"rel\":[\"class\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/courses/lsd/classes\"},{\"rel\":[\"course\"],\"href\":\"/courses/lsd\"}]},{\"class\":[\"event\",\"collection\"],\"rel\":[\"event\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/courses/lsd/events\"},{\"rel\":[\"course\"],\"href\":\"/courses/lsd\"}]}],\"actions\":[{\"name\":\"delete\",\"title\":\"Delete course\",\"method\":\"DELETE\",\"isTemplated\":false,\"href\":\"/v0/courses/lsd\",\"fields\":[]},{\"name\":\"edit\",\"title\":\"Edit course\",\"method\":\"PATCH\",\"isTemplated\":false,\"type\":\"application/json\",\"href\":\"/v0/courses/lsd\",\"fields\":[]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/courses/lsd\"},{\"rel\":[\"current\"],\"href\":\"/v0/courses/lsd/classes/1920v\"},{\"rel\":[\"collection\"],\"href\":\"/courses\"}]}"
private const val m1Mock =
    "{\"class\":[\"course\"],\"properties\":{\"acronym\":\"M1\",\"name\":\"Matemática I\"},\"entities\":[{\"class\":[\"class\",\"collection\"],\"rel\":[\"class\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/courses/m1/classes\"},{\"rel\":[\"course\"],\"href\":\"/courses/m1\"}]},{\"class\":[\"event\",\"collection\"],\"rel\":[\"event\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/courses/m1/events\"},{\"rel\":[\"course\"],\"href\":\"/courses/m1\"}]}],\"actions\":[{\"name\":\"delete\",\"title\":\"Delete course\",\"method\":\"DELETE\",\"isTemplated\":false,\"href\":\"/v0/courses/m1\",\"fields\":[]},{\"name\":\"edit\",\"title\":\"Edit course\",\"method\":\"PATCH\",\"isTemplated\":false,\"type\":\"application/json\",\"href\":\"/v0/courses/m1\",\"fields\":[]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/courses/m1\"},{\"rel\":[\"current\"],\"href\":\"/v0/courses/m1/classes/1920v\"},{\"rel\":[\"collection\"],\"href\":\"/courses\"}]}"
private const val algaMock =
    "{\"class\":[\"course\"],\"properties\":{\"acronym\":\"ALGA\",\"name\":\"Álgebra Linear e Geometria Analítica\"},\"entities\":[{\"class\":[\"class\",\"collection\"],\"rel\":[\"class\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/courses/alga/classes\"},{\"rel\":[\"course\"],\"href\":\"/courses/alga\"}]},{\"class\":[\"event\",\"collection\"],\"rel\":[\"event\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/courses/alga/events\"},{\"rel\":[\"course\"],\"href\":\"/courses/alga\"}]}],\"actions\":[{\"name\":\"delete\",\"title\":\"Delete course\",\"method\":\"DELETE\",\"isTemplated\":false,\"href\":\"/v0/courses/alga\",\"fields\":[]},{\"name\":\"edit\",\"title\":\"Edit course\",\"method\":\"PATCH\",\"isTemplated\":false,\"type\":\"application/json\",\"href\":\"/v0/courses/alga\",\"fields\":[]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/courses/alga\"},{\"rel\":[\"current\"],\"href\":\"/v0/courses/alga/classes/1920v\"},{\"rel\":[\"collection\"],\"href\":\"/courses\"}]}"

// Course classes
private const val algaClassesMock =
    "{\"class\":[\"class\"],\"properties\":{\"course\":\"ALGA\",\"calendar term\":\"1920v\"},\"entities\":[{\"class\":[\"class\",\"section\"],\"properties\":{\"id\":\"1d\"},\"rel\":[\"item\"],\"title\":\"Class Section of Course ALGA at Calendar Term 1920v\",\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/alga/classes/1920v/1d\"}]},{\"class\":[\"class\",\"section\"],\"properties\":{\"id\":\"1n\"},\"rel\":[\"item\"],\"title\":\"Class Section of Course ALGA at Calendar Term 1920v\",\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/alga/classes/1920v/1n\"}]},{\"class\":[\"calendar\"],\"rel\":[\"calendar\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/alga/classes/s1920v/calendar\"}]}],\"actions\":[{\"name\":\"delete\",\"title\":\"Delete class\",\"method\":\"DELETE\",\"isTemplated\":false,\"href\":\"/v0/courses/alga/classes/1920v\",\"fields\":[]},{\"name\":\"edit\",\"title\":\"Edit class\",\"method\":\"PATCH\",\"isTemplated\":false,\"type\":\"application/json\",\"href\":\"/v0/courses/alga/classes/1920v\",\"fields\":[]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/alga/classes/1920v\"},{\"rel\":[\"collection\"],\"href\":\"/v0/courses/alga/classes/\"}]}"
private const val pgClassesMock =
    "{\"class\":[\"class\"],\"properties\":{\"course\":\"PG\",\"calendar term\":\"1920v\"},\"entities\":[{\"class\":[\"class\",\"section\"],\"properties\":{\"id\":\"1n\"},\"rel\":[\"item\"],\"title\":\"Class Section of Course PG at Calendar Term 1920v\",\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/pg/classes/1920v/1n\"}]},{\"class\":[\"calendar\"],\"rel\":[\"calendar\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/pg/classes/s1920v/calendar\"}]}],\"actions\":[{\"name\":\"delete\",\"title\":\"Delete class\",\"method\":\"DELETE\",\"isTemplated\":false,\"href\":\"/v0/courses/pg/classes/1920v\",\"fields\":[]},{\"name\":\"edit\",\"title\":\"Edit class\",\"method\":\"PATCH\",\"isTemplated\":false,\"type\":\"application/json\",\"href\":\"/v0/courses/pg/classes/1920v\",\"fields\":[]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/pg/classes/1920v\"},{\"rel\":[\"collection\"],\"href\":\"/v0/courses/pg/classes/\"}]}"
private const val m1ClassesMock =
    "{\"class\":[\"class\"],\"properties\":{\"course\":\"M1\",\"calendar term\":\"1920v\"},\"entities\":[{\"class\":[\"class\",\"section\"],\"properties\":{\"id\":\"1d\"},\"rel\":[\"item\"],\"title\":\"Class Section of Course M1 at Calendar Term 1920v\",\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/m1/classes/1920v/1d\"}]},{\"class\":[\"class\",\"section\"],\"properties\":{\"id\":\"1n\"},\"rel\":[\"item\"],\"title\":\"Class Section of Course M1 at Calendar Term 1920v\",\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/m1/classes/1920v/1n\"}]},{\"class\":[\"calendar\"],\"rel\":[\"calendar\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/m1/classes/s1920v/calendar\"}]}],\"actions\":[{\"name\":\"delete\",\"title\":\"Delete class\",\"method\":\"DELETE\",\"isTemplated\":false,\"href\":\"/v0/courses/m1/classes/1920v\",\"fields\":[]},{\"name\":\"edit\",\"title\":\"Edit class\",\"method\":\"PATCH\",\"isTemplated\":false,\"type\":\"application/json\",\"href\":\"/v0/courses/m1/classes/1920v\",\"fields\":[]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/m1/classes/1920v\"},{\"rel\":[\"collection\"],\"href\":\"/v0/courses/m1/classes/\"}]}"
private const val eClassesMock =
    "{\"class\":[\"class\"],\"properties\":{\"course\":\"E\",\"calendar term\":\"1920v\"},\"entities\":[{\"class\":[\"class\",\"section\"],\"properties\":{\"id\":\"1d\"},\"rel\":[\"item\"],\"title\":\"Class Section of Course E at Calendar Term 1920v\",\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/e/classes/1920v/1d\"}]},{\"class\":[\"class\",\"section\"],\"properties\":{\"id\":\"1n\"},\"rel\":[\"item\"],\"title\":\"Class Section of Course E at Calendar Term 1920v\",\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/e/classes/1920v/1n\"}]},{\"class\":[\"calendar\"],\"rel\":[\"calendar\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/e/classes/s1920v/calendar\"}]}],\"actions\":[{\"name\":\"delete\",\"title\":\"Delete class\",\"method\":\"DELETE\",\"isTemplated\":false,\"href\":\"/v0/courses/e/classes/1920v\",\"fields\":[]},{\"name\":\"edit\",\"title\":\"Edit class\",\"method\":\"PATCH\",\"isTemplated\":false,\"type\":\"application/json\",\"href\":\"/v0/courses/e/classes/1920v\",\"fields\":[]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/e/classes/1920v\"},{\"rel\":[\"collection\"],\"href\":\"/v0/courses/e/classes/\"}]}"
private const val lsdClassesMock =
    "{\"class\":[\"class\"],\"properties\":{\"course\":\"LSD\",\"calendar term\":\"1920v\"},\"entities\":[{\"class\":[\"class\",\"section\"],\"properties\":{\"id\":\"1d\"},\"rel\":[\"item\"],\"title\":\"Class Section of Course LSD at Calendar Term 1920v\",\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/lsd/classes/1920v/1d\"}]},{\"class\":[\"class\",\"section\"],\"properties\":{\"id\":\"2d\"},\"rel\":[\"item\"],\"title\":\"Class Section of Course LSD at Calendar Term 1920v\",\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/lsd/classes/1920v/2d\"}]},{\"class\":[\"class\",\"section\"],\"properties\":{\"id\":\"1n\"},\"rel\":[\"item\"],\"title\":\"Class Section of Course LSD at Calendar Term 1920v\",\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/lsd/classes/1920v/1n\"}]},{\"class\":[\"calendar\"],\"rel\":[\"calendar\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/lsd/classes/s1920v/calendar\"}]}],\"actions\":[{\"name\":\"delete\",\"title\":\"Delete class\",\"method\":\"DELETE\",\"isTemplated\":false,\"href\":\"/v0/courses/lsd/classes/1920v\",\"fields\":[]},{\"name\":\"edit\",\"title\":\"Edit class\",\"method\":\"PATCH\",\"isTemplated\":false,\"type\":\"application/json\",\"href\":\"/v0/courses/lsd/classes/1920v\",\"fields\":[]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/lsd/classes/1920v\"},{\"rel\":[\"collection\"],\"href\":\"/v0/courses/lsd/classes/\"}]}"

// Class Section
private const val pgClassSection1NMock =
    "{\"class\":[\"class\",\"section\"],\"properties\":{\"course\":\"PG\",\"class\":\"s1920v\",\"id\":\"1N\"},\"entities\":[{\"class\":[\"calendar\"],\"rel\":[\"/rel/calendar\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/pg/classes/s1920v/1n/calendar\"}]}],\"actions\":[{\"name\":\"enroll\",\"title\":\"Enroll class section\",\"method\":\"POST\",\"href\":\"/v0/courses/pg/classes/s1920v/1n/enroll\",\"type\":\"application/x-www-form-urlencoded\",\"fields\":[]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/pg/classes/s1920v/1n\"},{\"rel\":[\"collection\"],\"href\":\"/v0/courses/pg/classes/s1920v\"}]}"

private const val algaClassSection1DMock =
    "{\"class\":[\"class\",\"section\"],\"properties\":{\"course\":\"ALGA\",\"class\":\"s1920v\",\"id\":\"1D\"},\"entities\":[{\"class\":[\"calendar\"],\"rel\":[\"/rel/calendar\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/alga/classes/s1920v/1d/calendar\"}]}],\"actions\":[{\"name\":\"enroll\",\"title\":\"Enroll class section\",\"method\":\"POST\",\"href\":\"/v0/courses/alga/classes/s1920v/1d/enroll\",\"type\":\"application/x-www-form-urlencoded\",\"fields\":[]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/alga/classes/s1920v/1d\"},{\"rel\":[\"collection\"],\"href\":\"/v0/courses/alga/classes/s1920v\"}]}"
private const val algaClassSection1NMock =
    "{\"class\":[\"class\",\"section\"],\"properties\":{\"course\":\"ALGA\",\"class\":\"s1920v\",\"id\":\"1N\"},\"entities\":[{\"class\":[\"calendar\"],\"rel\":[\"/rel/calendar\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/alga/classes/s1920v/1n/calendar\"}]}],\"actions\":[{\"name\":\"enroll\",\"title\":\"Enroll class section\",\"method\":\"POST\",\"href\":\"/v0/courses/alga/classes/s1920v/1n/enroll\",\"type\":\"application/x-www-form-urlencoded\",\"fields\":[]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/alga/classes/s1920v/1n\"},{\"rel\":[\"collection\"],\"href\":\"/v0/courses/alga/classes/s1920v\"}]}"

private const val lsdClassSection1DMock =
    "{\"class\":[\"class\",\"section\"],\"properties\":{\"course\":\"LSD\",\"class\":\"s1920v\",\"id\":\"1D\"},\"entities\":[{\"class\":[\"calendar\"],\"rel\":[\"/rel/calendar\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/lsd/classes/s1920v/1d/calendar\"}]}],\"actions\":[{\"name\":\"enroll\",\"title\":\"Enroll class section\",\"method\":\"POST\",\"href\":\"/v0/courses/lsd/classes/s1920v/1d/enroll\",\"type\":\"application/x-www-form-urlencoded\",\"fields\":[]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/lsd/classes/s1920v/1d\"},{\"rel\":[\"collection\"],\"href\":\"/v0/courses/lsd/classes/s1920v\"}]}"
private const val lsdClassSection2DMock =
    "{\"class\":[\"class\",\"section\"],\"properties\":{\"course\":\"LSD\",\"class\":\"s1920v\",\"id\":\"2D\"},\"entities\":[{\"class\":[\"calendar\"],\"rel\":[\"/rel/calendar\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/lsd/classes/s1920v/2d/calendar\"}]}],\"actions\":[{\"name\":\"enroll\",\"title\":\"Enroll class section\",\"method\":\"POST\",\"href\":\"/v0/courses/lsd/classes/s1920v/2d/enroll\",\"type\":\"application/x-www-form-urlencoded\",\"fields\":[]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/lsd/classes/s1920v/2d\"},{\"rel\":[\"collection\"],\"href\":\"/v0/courses/lsd/classes/s1920v\"}]}"
private const val lsdClassSection1NMock =
    "{\"class\":[\"class\",\"section\"],\"properties\":{\"course\":\"LSD\",\"class\":\"s1920v\",\"id\":\"1N\"},\"entities\":[{\"class\":[\"calendar\"],\"rel\":[\"/rel/calendar\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/lsd/classes/s1920v/1n/calendar\"}]}],\"actions\":[{\"name\":\"enroll\",\"title\":\"Enroll class section\",\"method\":\"POST\",\"href\":\"/v0/courses/lsd/classes/s1920v/1n/enroll\",\"type\":\"application/x-www-form-urlencoded\",\"fields\":[]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/lsd/classes/s1920v/1n\"},{\"rel\":[\"collection\"],\"href\":\"/v0/courses/lsd/classes/s1920v\"}]}"

private const val m1ClassSection1DMock =
    "{\"class\":[\"class\",\"section\"],\"properties\":{\"course\":\"M1\",\"class\":\"s1920v\",\"id\":\"1D\"},\"entities\":[{\"class\":[\"calendar\"],\"rel\":[\"/rel/calendar\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/m1/classes/s1920v/1d/calendar\"}]}],\"actions\":[{\"name\":\"enroll\",\"title\":\"Enroll class section\",\"method\":\"POST\",\"href\":\"/v0/courses/m1/classes/s1920v/1d/enroll\",\"type\":\"application/x-www-form-urlencoded\",\"fields\":[]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/m1/classes/s1920v/1d\"},{\"rel\":[\"collection\"],\"href\":\"/v0/courses/m1/classes/s1920v\"}]}"
private const val m1ClassSection1NMock =
    "{\"class\":[\"class\",\"section\"],\"properties\":{\"course\":\"M1\",\"class\":\"s1920v\",\"id\":\"1N\"},\"entities\":[{\"class\":[\"calendar\"],\"rel\":[\"/rel/calendar\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/m1/classes/s1920v/1n/calendar\"}]}],\"actions\":[{\"name\":\"enroll\",\"title\":\"Enroll class section\",\"method\":\"POST\",\"href\":\"/v0/courses/m1/classes/s1920v/1n/enroll\",\"type\":\"application/x-www-form-urlencoded\",\"fields\":[]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/m1/classes/s1920v/1n\"},{\"rel\":[\"collection\"],\"href\":\"/v0/courses/m1/classes/s1920v\"}]}"

private const val eClassSection1DMock =
    "{\"class\":[\"class\",\"section\"],\"properties\":{\"course\":\"E\",\"class\":\"s1920v\",\"id\":\"1D\"},\"entities\":[{\"class\":[\"calendar\"],\"rel\":[\"/rel/calendar\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/e/classes/s1920v/1d/calendar\"}]}],\"actions\":[{\"name\":\"enroll\",\"title\":\"Enroll class section\",\"method\":\"POST\",\"href\":\"/v0/courses/e/classes/s1920v/1d/enroll\",\"type\":\"application/x-www-form-urlencoded\",\"fields\":[]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/e/classes/s1920v/1d\"},{\"rel\":[\"collection\"],\"href\":\"/v0/courses/e/classes/s1920v\"}]}"
private const val eClassSection1NMock =
    "{\"class\":[\"class\",\"section\"],\"properties\":{\"course\":\"E\",\"class\":\"s1920v\",\"id\":\"1N\"},\"entities\":[{\"class\":[\"calendar\"],\"rel\":[\"/rel/calendar\"],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/e/classes/s1920v/1n/calendar\"}]}],\"actions\":[{\"name\":\"enroll\",\"title\":\"Enroll class section\",\"method\":\"POST\",\"href\":\"/v0/courses/e/classes/s1920v/1n/enroll\",\"type\":\"application/x-www-form-urlencoded\",\"fields\":[]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/e/classes/s1920v/1n\"},{\"rel\":[\"collection\"],\"href\":\"/v0/courses/e/classes/s1920v\"}]}"

//Calendar terms
private const val calendarTermsMock = "{\n" +
        "  \"class\": [ \"term\", \"collection\" ],\n" +
        "  \"properties\": { },\n" +
        "  \"entities\": [\n" +
        "    {\n" +
        "      \"class\": [ \"term\" ],\n" +
        "      \"rel\": [ \"item\" ], \n" +
        "      \"properties\": { \n" +
        "        \"name\": \"1920v\"\n" +
        "      },\n" +
        "      \"links\": [\n" +
        "        { \"rel\": [ \"self\" ], \"href\": \"/v0/calendar-terms/1920v\" },\n" +
        "        { \"rel\": [ \"collection\" ], \"href\": \"/v0/calendar-terms\" }\n" +
        "      ]\n" +
        "    },\n" +
        "    {\n" +
        "      \"class\": [ \"term\" ],\n" +
        "      \"rel\": [ \"item\" ], \n" +
        "      \"properties\": { \n" +
        "        \"name\": \"1920i\"\n" +
        "      },\n" +
        "      \"links\": [\n" +
        "        { \"rel\": [ \"self\" ], \"href\": \"/v0/calendar-terms/1920i\" },\n" +
        "        { \"rel\": [ \"collection\" ], \"href\": \"/v0/calendar-terms\" }\n" +
        "      ]\n" +
        "    }\n" +
        "  ],\n" +
        "  \"actions\": [\n" +
        "    {\n" +
        "      \"name\": \"search\",\n" +
        "      \"title\": \"Search items\",\n" +
        "      \"method\": \"GET\",\n" +
        "      \"href\": \"/v0/calendar-terms\",\n" +
        "      \"isTemplated\": true,\n" +
        "      \"type\": \"application/vnd.siren+json\",\n" +
        "      \"fields\": [\n" +
        "        { \"name\": \"limit\", \"type\": \"number\", \"class\": \"param/limit\" },\n" +
        "        { \"name\": \"page\", \"type\": \"number\", \"class\": \"param/page\" }\n" +
        "      ]\n" +
        "    }\n" +
        "  ],\n" +
        "  \"links\": [\n" +
        "    { \"rel\": [ \"self\" ], \"href\": \"/v0/calendar-terms?page=1&limit=2\" },\n" +
        "    { \"rel\": [ \"next\" ], \"href\": \"/v0/calendar-terms?page=2&limit=2\" },\n" +
        "    { \"rel\": [ \"previous\" ], \"href\": \"/v0/calendar-terms?page=0&limit=2\" }\n" +
        "  ]\n" +
        "}"

// Exam mocks (Events)
private val pg1stExam = "{\"class\":[\"event\"],\"properties\":{\"type\":\"event\",\"properties\":{\"uid\":{\"parameters\":{},\"value\":6854},\"summary\":{\"parameters\":{},\"value\":\"PG 1st Exam\"},\"description\":{\"parameters\":{},\"value\":\"First exam of the PG course during the semester 1920v\"},\"categories\":{\"parameters\":{},\"value\":\"Exam\"},\"created\":{\"parameters\":{},\"value\":\"2020-04-23T17:22:02Z\"},\"dtstamp\":{\"parameters\":{},\"value\":\"2020-04-23T17:22:02Z\"},\"dtstart\":{\"parameters\":{},\"value\":\"2020-06-24T14:00:00Z\"},\"dtend\":{\"parameters\":{},\"value\":\"2020-06-24T16:30:00Z\"}}},\"entities\":[{\"class\":[\"class\"],\"rel\":[\"/rel/class\"],\"properties\":{\"classId\":\"pg-s1920v\",\"termId\":\"s1920v\"},\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/pg/classes/s1920v\"},{\"rel\":[\"term\"],\"href\":\"/v0/terms/s1920v\"},{\"rel\":[\"course\"],\"href\":\"/v0/courses/pg\"}]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/pg/classes/s1920v/calendar/components/6854\"},{\"rel\":[\"about\"],\"href\":\"/v0/courses/pg/classes/s1920v\"}]}"
private val pg2ndExam = "{\"class\":[\"event\"],\"properties\":{\"type\":\"event\",\"properties\":{\"uid\":{\"parameters\":{},\"value\":6855},\"summary\":{\"parameters\":{},\"value\":\"PG 2nd Exam\"},\"description\":{\"parameters\":{},\"value\":\"Second exam of the PG course during the semester 1920v\"},\"categories\":{\"parameters\":{},\"value\":\"Exam\"},\"created\":{\"parameters\":{},\"value\":\"2020-04-25T17:22:02Z\"},\"dtstamp\":{\"parameters\":{},\"value\":\"2020-04-25T17:22:02Z\"},\"dtstart\":{\"parameters\":{},\"value\":\"2020-07-03T17:00:00Z\"},\"dtend\":{\"parameters\":{},\"value\":\"2020-07-03T18:30:00Z\"}}},\"entities\":[{\"class\":[\"class\"],\"rel\":[\"/rel/class\"],\"properties\":{\"classId\":\"pg-s1920v\",\"termId\":\"s1920v\"},\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/pg/classes/s1920v\"},{\"rel\":[\"term\"],\"href\":\"/v0/terms/s1920v\"},{\"rel\":[\"course\"],\"href\":\"/v0/courses/pg\"}]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/pg/classes/s1920v/calendar/components/6855\"},{\"rel\":[\"about\"],\"href\":\"/v0/courses/pg/classes/s1920v\"}]}"

private val lsd1stExam = "{\"class\":[\"event\"],\"properties\":{\"type\":\"event\",\"properties\":{\"uid\":{\"parameters\":{},\"value\":6856},\"summary\":{\"parameters\":{},\"value\":\"LSD 1st Exam\"},\"description\":{\"parameters\":{},\"value\":\"First exam of the LSD course during the semester 1920v\"},\"categories\":{\"parameters\":{},\"value\":\"Exam\"},\"created\":{\"parameters\":{},\"value\":\"2020-04-25T17:22:02Z\"},\"dtstamp\":{\"parameters\":{},\"value\":\"2020-04-25T17:22:02Z\"},\"dtstart\":{\"parameters\":{},\"value\":\"2020-06-26T17:00:00Z\"},\"dtend\":{\"parameters\":{},\"value\":\"2020-06-26T18:30:00Z\"}}},\"entities\":[{\"class\":[\"class\"],\"rel\":[\"/rel/class\"],\"properties\":{\"classId\":\"lsd-s1920v\",\"termId\":\"s1920v\"},\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/lsd/classes/s1920v\"},{\"rel\":[\"term\"],\"href\":\"/v0/terms/s1920v\"},{\"rel\":[\"course\"],\"href\":\"/v0/courses/lsd\"}]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/lsd/classes/s1920v/calendar/components/6856\"},{\"rel\":[\"about\"],\"href\":\"/v0/courses/lsd/classes/s1920v\"}]}"
private val lsd2ndExam = "{\"class\":[\"event\"],\"properties\":{\"type\":\"event\",\"properties\":{\"uid\":{\"parameters\":{},\"value\":6857},\"summary\":{\"parameters\":{},\"value\":\"LSD 2nd Exam\"},\"description\":{\"parameters\":{},\"value\":\"Second exam of the LSD course during the semester 1920v\"},\"categories\":{\"parameters\":{},\"value\":\"Exam\"},\"created\":{\"parameters\":{},\"value\":\"2020-04-25T17:22:02Z\"},\"dtstamp\":{\"parameters\":{},\"value\":\"2020-04-25T17:22:02Z\"},\"dtstart\":{\"parameters\":{},\"value\":\"2020-07-14T17:00:00Z\"},\"dtend\":{\"parameters\":{},\"value\":\"2020-07-14T18:30:00Z\"}}},\"entities\":[{\"class\":[\"class\"],\"rel\":[\"/rel/class\"],\"properties\":{\"classId\":\"lsd-s1920v\",\"termId\":\"s1920v\"},\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/lsd/classes/s1920v\"},{\"rel\":[\"term\"],\"href\":\"/v0/terms/s1920v\"},{\"rel\":[\"course\"],\"href\":\"/v0/courses/lsd\"}]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/lsd/classes/s1920v/calendar/components/6857\"},{\"rel\":[\"about\"],\"href\":\"/v0/courses/lsd/classes/s1920v\"}]}"

private val alga1stExam = "{\"class\":[\"event\"],\"properties\":{\"type\":\"event\",\"properties\":{\"uid\":{\"parameters\":{},\"value\":6858},\"summary\":{\"parameters\":{},\"value\":\"ALGA 1st Exam\"},\"description\":{\"parameters\":{},\"value\":\"First exam of the ALGA course during the semester 1920v\"},\"categories\":{\"parameters\":{},\"value\":\"Exam\"},\"created\":{\"parameters\":{},\"value\":\"2020-04-25T17:22:02Z\"},\"dtstamp\":{\"parameters\":{},\"value\":\"2020-04-25T17:22:02Z\"},\"dtstart\":{\"parameters\":{},\"value\":\"2020-06-22T17:00:00Z\"},\"dtend\":{\"parameters\":{},\"value\":\"2020-06-22T18:30:00Z\"}}},\"entities\":[{\"class\":[\"class\"],\"rel\":[\"/rel/class\"],\"properties\":{\"classId\":\"alga-s1920v\",\"termId\":\"s1920v\"},\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/alga/classes/s1920v\"},{\"rel\":[\"term\"],\"href\":\"/v0/terms/s1920v\"},{\"rel\":[\"course\"],\"href\":\"/v0/courses/alga\"}]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/alga/classes/s1920v/calendar/components/6858\"},{\"rel\":[\"about\"],\"href\":\"/v0/courses/alga/classes/s1920v\"}]}"
private val alga2ndExam = "{\"class\":[\"event\"],\"properties\":{\"type\":\"event\",\"properties\":{\"uid\":{\"parameters\":{},\"value\":6859},\"summary\":{\"parameters\":{},\"value\":\"ALGA 2nd Exam\"},\"description\":{\"parameters\":{},\"value\":\"Second exam of the ALGA course during the semester 1920v\"},\"categories\":{\"parameters\":{},\"value\":\"Exam\"},\"created\":{\"parameters\":{},\"value\":\"2020-04-25T17:22:02Z\"},\"dtstamp\":{\"parameters\":{},\"value\":\"2020-04-25T17:22:02Z\"},\"dtstart\":{\"parameters\":{},\"value\":\"2020-07-16T17:00:00Z\"},\"dtend\":{\"parameters\":{},\"value\":\"2020-07-16T18:30:00Z\"}}},\"entities\":[{\"class\":[\"class\"],\"rel\":[\"/rel/class\"],\"properties\":{\"classId\":\"alga-s1920v\",\"termId\":\"s1920v\"},\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/alga/classes/s1920v\"},{\"rel\":[\"term\"],\"href\":\"/v0/terms/s1920v\"},{\"rel\":[\"course\"],\"href\":\"/v0/courses/alga\"}]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/alga/classes/s1920v/calendar/components/6859\"},{\"rel\":[\"about\"],\"href\":\"/v0/courses/alga/classes/s1920v\"}]}"

private val m11stExam = "{\"class\":[\"event\"],\"properties\":{\"type\":\"event\",\"properties\":{\"uid\":{\"parameters\":{},\"value\":6860},\"summary\":{\"parameters\":{},\"value\":\"M1 1st Exam\"},\"description\":{\"parameters\":{},\"value\":\"First exam of the M1 course during the semester 1920v\"},\"categories\":{\"parameters\":{},\"value\":\"Exam\"},\"created\":{\"parameters\":{},\"value\":\"2020-04-25T17:22:02Z\"},\"dtstamp\":{\"parameters\":{},\"value\":\"2020-04-25T17:22:02Z\"},\"dtstart\":{\"parameters\":{},\"value\":\"2020-06-18T17:00:00Z\"},\"dtend\":{\"parameters\":{},\"value\":\"2020-06-18T18:30:00Z\"}}},\"entities\":[{\"class\":[\"class\"],\"rel\":[\"/rel/class\"],\"properties\":{\"classId\":\"m1-s1920v\",\"termId\":\"s1920v\"},\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/m1/classes/s1920v\"},{\"rel\":[\"term\"],\"href\":\"/v0/terms/s1920v\"},{\"rel\":[\"course\"],\"href\":\"/v0/courses/m1\"}]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/m1/classes/s1920v/calendar/components/6860\"},{\"rel\":[\"about\"],\"href\":\"/v0/courses/m1/classes/s1920v\"}]}"
private val m12ndExam = "{\"class\":[\"event\"],\"properties\":{\"type\":\"event\",\"properties\":{\"uid\":{\"parameters\":{},\"value\":6861},\"summary\":{\"parameters\":{},\"value\":\"M1 2nd Exam\"},\"description\":{\"parameters\":{},\"value\":\"Second exam of the M1 course during the semester 1920v\"},\"categories\":{\"parameters\":{},\"value\":\"Exam\"},\"created\":{\"parameters\":{},\"value\":\"2020-04-25T17:22:02Z\"},\"dtstamp\":{\"parameters\":{},\"value\":\"2020-04-25T17:22:02Z\"},\"dtstart\":{\"parameters\":{},\"value\":\"2020-07-18T17:00:00Z\"},\"dtend\":{\"parameters\":{},\"value\":\"2020-07-18T18:30:00Z\"}}},\"entities\":[{\"class\":[\"class\"],\"rel\":[\"/rel/class\"],\"properties\":{\"classId\":\"m1-s1920v\",\"termId\":\"s1920v\"},\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/m1/classes/s1920v\"},{\"rel\":[\"term\"],\"href\":\"/v0/terms/s1920v\"},{\"rel\":[\"course\"],\"href\":\"/v0/courses/m1\"}]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/m1/classes/s1920v/calendar/components/6861\"},{\"rel\":[\"about\"],\"href\":\"/v0/courses/m1/classes/s1920v\"}]}"

private val e1stExam = "{\"class\":[\"event\"],\"properties\":{\"type\":\"event\",\"properties\":{\"uid\":{\"parameters\":{},\"value\":6862},\"summary\":{\"parameters\":{},\"value\":\"E 1st Exam\"},\"description\":{\"parameters\":{},\"value\":\"First exam of the Eletronic course during the semester 1920v\"},\"categories\":{\"parameters\":{},\"value\":\"Exam\"},\"created\":{\"parameters\":{},\"value\":\"2020-04-25T17:22:02Z\"},\"dtstamp\":{\"parameters\":{},\"value\":\"2020-04-25T17:22:02Z\"},\"dtstart\":{\"parameters\":{},\"value\":\"2020-06-22T17:00:00Z\"},\"dtend\":{\"parameters\":{},\"value\":\"2020-06-22T18:30:00Z\"}}},\"entities\":[{\"class\":[\"class\"],\"rel\":[\"/rel/class\"],\"properties\":{\"classId\":\"e-s1920v\",\"termId\":\"s1920v\"},\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/e/classes/s1920v\"},{\"rel\":[\"term\"],\"href\":\"/v0/terms/s1920v\"},{\"rel\":[\"course\"],\"href\":\"/v0/courses/e\"}]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/e/classes/s1920v/calendar/components/6862\"},{\"rel\":[\"about\"],\"href\":\"/v0/courses/e/classes/s1920v\"}]}"
private val e2ndExam = "{\"class\":[\"event\"],\"properties\":{\"type\":\"event\",\"properties\":{\"uid\":{\"parameters\":{},\"value\":6863},\"summary\":{\"parameters\":{},\"value\":\"E 2nd Exam\"},\"description\":{\"parameters\":{},\"value\":\"Second exam of the Eletronic course during the semester 1920v\"},\"categories\":{\"parameters\":{},\"value\":\"Exam\"},\"created\":{\"parameters\":{},\"value\":\"2020-04-25T17:22:02Z\"},\"dtstamp\":{\"parameters\":{},\"value\":\"2020-04-25T17:22:02Z\"},\"dtstart\":{\"parameters\":{},\"value\":\"2020-07-22T17:00:00Z\"},\"dtend\":{\"parameters\":{},\"value\":\"2020-07-22T18:30:00Z\"}}},\"entities\":[{\"class\":[\"class\"],\"rel\":[\"/rel/class\"],\"properties\":{\"classId\":\"e-s1920v\",\"termId\":\"s1920v\"},\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/e/classes/s1920v\"},{\"rel\":[\"term\"],\"href\":\"/v0/terms/s1920v\"},{\"rel\":[\"course\"],\"href\":\"/v0/courses/e\"}]}],\"links\":[{\"rel\":[\"self\"],\"href\":\"/v0/courses/e/classes/s1920v/calendar/components/6863\"},{\"rel\":[\"about\"],\"href\":\"/v0/courses/e/classes/s1920v\"}]}"
