package org.ionproject.android

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.ionwebapi.JacksonIonMapper
import org.junit.Test

import org.junit.Assert.*


class JacksonParserTests {

    private val jacksonMapper = JacksonIonMapper(TestCoroutineDispatcher())

    @Test
    fun extraProperty() = runBlockingTest {

        val allProgrammesMock = "{\n" +
                "    \"class\": [ \"collection\", \"programme\" ],\n" +
                "    \"entities\": [\n" +
                "        {\n" +
                "            \"class\": [ \"programme\" ],\n" +
                "            \"rel\": [ \"item\" ],\n" +
                "            \"properties\": {\n" +
                "                \"programmeId\": 1,\n" +
                "                \"acronym\": \"LEIC\"\n" +
                "            },\n" +
                "            \"links\" : [\n" +
                "                { \"rel\": [ \"self\" ], \"href\": \"/v0/programmes/1\" }\n" +
                "            ],\n" +
                "            \"extraProp\": \"should explode\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"class\": [ \"programme\" ],\n" +
                "            \"rel\": [ \"item\" ],\n" +
                "            \"properties\": {\n" +
                "                \"id\": 2,\n" +
                "                \"acronym\": \"MEIC\"\n" +
                "            },\n" +
                "            \"links\" : [\n" +
                "                { \"rel\": [ \"self\" ], \"href\": \"/v0/programmes/2\" }\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"actions\": [\n" +
                "        {\n" +
                "            \"name\": \"add-programme\",\n" +
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
        jacksonMapper.parse(allProgrammesMock,SirenEntity::class.java)

    }

    @Test
    fun invalidHrefProperty() = runBlockingTest {
        val allProgrammesMock = "{\n" +
                "    \"class\": [ \"collection\", \"programme\" ],\n" +
                "    \"entities\": [\n" +
                "        {\n" +
                "            \"class\": [ \"programme\" ],\n" +
                "            \"rel\": [ \"item\" ],\n" +
                "            \"properties\": {\n" +
                "                \"programmeId\": 1,\n" +
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
                "            \"name\": \"add-programme\",\n" +
                "            \"title\": \"Add Programme\",\n" +
                "            \"method\": \"POST\",\n" +
                "            \"href\": \"/v0/programmes/{?cool,param}\",\n" +
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
        val result = jacksonMapper.parse(allProgrammesMock,SirenEntity::class.java)
    }

    @Test
    fun unknownHttpMethod() = runBlockingTest {

        val allProgrammesMock = "{\n" +
                "    \"class\": [ \"collection\", \"programme\" ],\n" +
                "    \"entities\": [\n" +
                "        {\n" +
                "            \"class\": [ \"programme\" ],\n" +
                "            \"rel\": [ \"item\" ],\n" +
                "            \"properties\": {\n" +
                "                \"programmeId\": 1,\n" +
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
                "            \"method\": \"CONNECTT\",\n" +
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

        val result = jacksonMapper.parse(allProgrammesMock,SirenEntity::class.java)
    }

    @Test
    fun jsonIsValidButClassIsnt() = runBlockingTest {
        val jsonHomeMock = "{\n" +
                "  \"api\" : {\n" +
                "    \"title\" : \"i-on Core\",\n" +
                "    \"links\" : {\n" +
                "      \"describedBy\" : \"https://github.com/i-on-project/core/tree/master/docs/api\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"resources\" : {\n" +
                "    \"courses\" : {\n" +
                "      \"hrefTemplate\" : \"/v0/courses{?page,limit}\",\n" +
                "      \"hrefVars\" : {\n" +
                "        \"limit\" : \"/api-docs/params/limit\",\n" +
                "        \"page\" : \"/api-docs/params/page\"\n" +
                "      },\n" +
                "      \"hints\" : {\n" +
                "        \"allow\" : [ \"GET\" ],\n" +
                "        \"formats\" : {\n" +
                "          \"application/vnd.siren+json\" : { }\n" +
                "        },\n" +
                "        \"docs\" : \"https://github.com/i-on-project/core/tree/master/docs/api/courses.md\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"calendar-terms\" : {\n" +
                "      \"hrefTemplate\" : \"/v0/calendar-terms{?page,limit}\",\n" +
                "      \"hrefVars\" : {\n" +
                "        \"limit\" : \"/api-docs/params/limit\",\n" +
                "        \"page\" : \"/api-docs/params/page\"\n" +
                "      },\n" +
                "      \"hints\" : {\n" +
                "        \"allow\" : [ \"GET\" ],\n" +
                "        \"formats\" : {\n" +
                "          \"application/vnd.siren+json\" : { }\n" +
                "        },\n" +
                "        \"docs\" : \"https://github.com/i-on-project/core/tree/master/docs/api/calendar-terms.md\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"search\": {\n" +
                "      \"hrefTemplate\" : \"/v0/search{?query,types,page,limit}\",\n" +
                "      \"hrefVars\" : {\n" +
                "        \"query\" : \"/api-docs/params/query\",\n" +
                "        \"types\" : \"/api-docs/params/types\",\n" +
                "        \"limit\" : \"/api-docs/params/limit\",\n" +
                "        \"page\" : \"/api-docs/params/page\"\n" +
                "      },\n" +
                "      \"hints\" : {\n" +
                "        \"allow\" : [ \"GET\" ],\n" +
                "        \"formats\" : {\n" +
                "          \"application/vnd.siren+json\" : { }\n" +
                "        },\n" +
                "        \"docs\" : \"https://github.com/i-on-project/core/tree/master/docs/api/search.md\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}"
        val result = jacksonMapper.parse(jsonHomeMock,SirenEntity::class.java)
    }

    @Test(expected = JsonMappingException::class)
    fun invalidJson() = runBlockingTest {
        val invalidJson = "{\n" +
                "    \"class\": [ \"collection\", \"programme\" ],\n" +
                "    \"entities\": [\n" +
                "        {\n" +
                "            \"class\": [ \"programme\" ],\n" +
                "            \"rel\": [ \"item\" ],\n" +
                "            \"properties\": {\n" +
                "                \"programmeId\": 1,\n" +
                "                \"acronym\": \"LEIC\"\n" +
                "            },\n" +
                "            \"links\" : [\n" +
                "                { \"rel\": [ \"self\" ], \"href\": \"/v0/programmes/1\" }\n" +
                "            ],\n" +
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
        val result = jacksonMapper.parse(invalidJson,SirenEntity::class.java)
    }
}
