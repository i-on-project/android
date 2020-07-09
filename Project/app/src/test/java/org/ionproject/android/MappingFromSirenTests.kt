package org.ionproject.android

import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.ionproject.android.common.dto.MappingFromSirenException
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.ionwebapi.JacksonIonMapper
import org.ionproject.android.course_details.toCourse
import org.ionproject.android.programmes.toProgrammeSummaryList
import org.junit.Test

class MappingFromSirenTests {

    private val jacksonMapper = JacksonIonMapper(TestCoroutineDispatcher())

    @Test(expected = MappingFromSirenException::class)
    fun mappingSirenToWrongType() = runBlockingTest {

        val allProgrammesMock =
            "{ \"class\": [ \"collection\", \"programme\" ], \"entities\": [ { \"class\": [ \"programme\" ], \"rel\": [ \"item\" ], \"properties\": { \"id\": 1, \"acronym\": \"LEIC\" }, \"links\" : [ { \"rel\": [ \"self\" ], \"href\": \"/v0/programmes/1\" } ] }, { \"class\": [ \"Programme\" ], \"rel\": [ \"item\" ], \"properties\": { \"id\": 2, \"acronym\": \"MEIC\" }, \"links\" : [ { \"rel\": [ \"self\" ], \"href\": \"/v0/programmes/2\" } ] } ], \"actions\": [ { \"name\": \"add-programme\", \"title\": \"Add Programme\", \"method\": \"POST\", \"href\": \"/v0/programmes/\", \"type\": \"application/json\", \"fields\": [ { \"name\": \"ProgrammeAcr\", \"type\": \"text\"}, { \"name\": \"TermSize\", \"type\": \"number\"} ] } ], \"links\": [ { \"rel\": [ \"self\" ], \"href\": \"/v0/programmes\" } ] }"

        val sirenEntity = jacksonMapper.parse(allProgrammesMock, SirenEntity::class.java)

        sirenEntity.toCourse() // Should be a programmes list, but we are trying to map to courses
    }

    @Test
    fun validSirenButInvalidMapping() = runBlockingTest {
        val allProgrammesMock = "{\n" +
                "    \"class\": [ \"collection\", \"programme\" ],\n" +
                "    \"entities\": [\n" +
                "        {\n" +
                "            \"class\": [ \"programme\" ],\n" +
                "            \"rel\": [ \"item\" ],\n" +
                "            \"properties\": {\n" +
                "                \"id\": \"invalidId\",\n" +
                "                \"acronym\": \"LEIC\"\n" +
                "            },\n" +
                "            \"links\" : [\n" +
                "                { \"rel\": [ \"self\" ], \"href\": \"/v0/programmes/1\" }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"class\": [ \"programme\" ],\n" +
                "            \"rel\": [ \"item\" ],\n" +
                "            \"properties\": {\n" +
                "                \"id\": 1,\n" +
                "                \"acronym\": \"MEIC\"\n" +
                "            },\n" +
                "            \"links\" : [\n" +
                "                { \"rel\": [ \"self\" ], \"href\": \"/v0/programmes/2\" }\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"actions\": [\n" +
                "        {\n" +
                "            \"name\": \"add-programme\" ,\n" +
                "            \"title\": \"Add Programme\",\n" +
                "            \"method\": \"POST\",\n" +
                "            \"href\": \"/v0/programmes/\",\n" +
                "            \"type\": \"application/json\",\n" +
                "            \"fields\": [\n" +
                "                { \"name\": \"ProgrammeAcr\", \"type\": \"text\"},\n" +
                "                { \"name\": \"TermSize\", \"type\": \"number\"}\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"links\": [\n" +
                "        { \"rel\": [ \"self\" ], \"href\": \"/v0/programmes/\" }\n" +
                "    ]\n" +
                "}"

        val sirenEntity = jacksonMapper.parse(allProgrammesMock, SirenEntity::class.java)

        sirenEntity.toProgrammeSummaryList() // Should be a programmes list, but we are trying to map to courses
    }
}