package it.develhope.controllerprotection.auth.entities;

import it.develhope.controllerprotection.user.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRTO {

    private User user;
    private String JWT;


}
