package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private int id;
    @PastOrPresent
    private Date birthday
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "^\\S*")
    private String login;
    private String name;
    
    public Set<Integer> getLikes() {
        if (this.likes == null) {
            return new HashSet<>();
        }
        return likes;
    }
}
