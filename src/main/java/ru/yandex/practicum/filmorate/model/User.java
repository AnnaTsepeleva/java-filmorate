package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import lombok.Builder;
@Data
@Builder
public class User {
    private int id;
    @Email
    private String email;
    @NotBlank
    @NotNull
    @Pattern(regexp =  "^\\S*")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;

}