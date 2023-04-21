package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class FilmorateApplicationTests {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;


    @Test
    void AddFilm() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("Test")
                .description("descr")
                .duration(100)
                .releaseDate(LocalDate.of(1991, 7, 30))
                .build();
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn();
    }
    @Test
    void notAddNullNameFilm() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("descr")
                .duration(100)
                .releaseDate(LocalDate.of(1991, 7, 30))
                .build();
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
    }

    @Test
    void notAddMaxDescrFilm() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("descrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescr")
                .duration(100)
                .releaseDate(LocalDate.of(1991, 7, 30))
                .build();
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
    }

    @Test
    void notAddEarlyFilm() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("rdescr")
                .duration(100)
                .releaseDate(LocalDate.of(1894, 7, 30))
                .build();
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
    }

    @Test
    void notAddZeroDurationFilm() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("rdescr")
                .duration(0)
                .releaseDate(LocalDate.of(1991, 7, 30))
                .build();
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
    }

    @Test
    void notAddBadDurationFilm() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("rdescr")
                .duration(-1)
                .releaseDate(LocalDate.of(1991, 7, 30))
                .build();
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
    }

    @Test
    void AddUser() throws Exception {
        User user = User.builder()
                .id(1)
                .name("")
                .email("Nyasha@mail.ru")
                .login("Nyasha")
                .birthday(LocalDate.of(1991, 7, 30))
                .build();
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn();
    }

    @Test
    void notAddBadEmailUser() throws Exception {
        User user = User.builder()
                .id(1)
                .name("")
                .email("NyashaSOBAKAmail.ru")
                .login("Nyasha")
                .birthday(LocalDate.of(1991, 7, 30))
                .build();
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
    }

    @Test
    void notAddNullLoginUser() throws Exception {
        User user = User.builder()
                .id(1)
                .name("")
                .email("Nyasha@mail.ru")
                .login("")
                .birthday(LocalDate.of(1991, 7, 30))
                .build();
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
    }

    @Test
    void notAddBadLoginUser() throws Exception {
        User user = User.builder()
                .id(1)
                .name("")
                .email("Nyasha@mail.ru")
                .login("Nya SHA")
                .birthday(LocalDate.of(1991, 7, 30))
                .build();
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
    }

    @Test
    void notAddFutureBDUser() throws Exception {
        User user = User.builder()
                .id(1)
                .name("")
                .email("Nyasha@mail.ru")
                .login("NyaSHA")
                .birthday(LocalDate.of(2991, 7, 30))
                .build();
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
    }

}
