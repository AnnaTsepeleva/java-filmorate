package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private int id;
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "^\\S*")
    private String login;
    private String name;
    private Set<Integer> friends;
<<<<<<< HEAD
    @PastOrPresent
    private Date birthday;
=======
    @Past
    private LocalDate birthday;
>>>>>>> f3eb81d3554b0eb83a8808fa81035f1c022a668a

    public Set<Integer> getFriends() {
        if (this.friends == null) {
            return new HashSet<>();
        }
        return friends;
    }
}