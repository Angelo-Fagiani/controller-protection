package it.develhope.controllerprotection.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import it.develhope.controllerprotection.auth.entities.LoginDTO;
import it.develhope.controllerprotection.auth.entities.LoginRTO;
import it.develhope.controllerprotection.user.entities.User;
import it.develhope.controllerprotection.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Service
public class LoginService {

    public static final String JWT_SECRET="3de38a73-8e6c-4624-aa07-d2546c98ea88";

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public LoginRTO login(LoginDTO loginDTO) throws Exception {
        if(loginDTO == null) throw new Exception("loginDTO was null");
        User userFromDB = userRepository.findByEmail(loginDTO.getEmail());
        if(userFromDB == null) throw new Exception("User not in DB");
        if (!userFromDB.isActive()) throw new Exception("UserFromDB not active");
        boolean canLogin = this.canUserLogin(userFromDB, loginDTO.getPassword());
        if(!canLogin) throw new Exception("User can not login");

        String JWT = generateJWT(userFromDB);

        userFromDB.setPassword(null);
        LoginRTO out = new LoginRTO();
        out.setJWT(JWT);
        out.setUser(userFromDB);

        return out;
    }

    public boolean canUserLogin(User user, String password){
        return passwordEncoder.matches(password, user.getPassword());
    }

    //https://www.baeldung.com/java-date-to-localdate-and-localdatetime
    static Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static String getJWT(User user){
        Date expiresAt = convertToDateViaInstant(LocalDateTime.now().plusDays(15));
        String[] roles = user.getRoles().stream().map(role -> role.getName()).toArray(String[]::new);
        return JWT.create()
                .withIssuer("develhope-demo")
                .withIssuedAt(new Date())
                .withExpiresAt(expiresAt)
                .withClaim("id", user.getId())
                .sign(Algorithm.HMAC512(JWT_SECRET));

    }

    public String generateJWT(User user) {
        String JWT = getJWT(user);

        user.setJwtCreatedOn(LocalDateTime.now());
        userRepository.save(user);

        return JWT;
    }





}
