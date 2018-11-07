package kz.peep.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kz.peep.api.dto.auth.JwtAuthenticationResponse
import kz.peep.api.dto.auth.LoginRequest
import kz.peep.api.dto.user.UserPatchRequest
import kz.peep.api.dto.user.UserRegisterRequest
import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


@SpringBootTest
@RunWith(SpringRunner::class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var objMapper: ObjectMapper

    @Test
    fun checkUsername() {
        mvc.perform(MockMvcRequestBuilders.get("/users/check?username=hello"))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun createUser() {
        mvc.perform(MockMvcRequestBuilders.post("/users")
                .content(asJsonString(UserRegisterRequest("Natasha Romanoff", "natasha.romanoff@nu.edu.kz", "Hello12345")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated)
    }

    @Test
    fun updateUser() {
        val result = mvc.perform(MockMvcRequestBuilders.post("/users/login")
                .content(asJsonString(LoginRequest("natasha.romanoff@nu.edu.kz", "Hello12345")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        objMapper.registerKotlinModule()

        val response : JwtAuthenticationResponse = objMapper.readValue(result.response.contentAsString)

        Assertions.assertThat(response.accessToken).isNotNull().isNotEmpty()

        mvc.perform(MockMvcRequestBuilders.patch("/users/natasha.romanoff@nu.edu.kz")
                .content(asJsonString(UserPatchRequest("Natasha Romanoff", "Hello12345")))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${response.accessToken}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest)

        mvc.perform(MockMvcRequestBuilders.patch("/users/nurzhan.sakenov@nu.edu.kz")
                .content(asJsonString(UserPatchRequest("Natasha Romanoff", "Hello12345")))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${response.accessToken}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden)

        mvc.perform(MockMvcRequestBuilders.patch("/users/natasha.romanoff@nu.edu.kz")
                .content(asJsonString(UserPatchRequest("Natashka Kakashka", "Hello123432", "+77077987198")))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${response.accessToken}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }
}

fun asJsonString(obj: Any): String {
    try {
        val mapper = ObjectMapper()
        return mapper.writeValueAsString(obj)
    } catch (e: Exception) {
        throw RuntimeException(e)
    }

}