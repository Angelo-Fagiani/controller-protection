package it.develhope.controllerprotection.auth.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.develhope.controllerprotection.auth.entities.SignupActivationDTO;
import it.develhope.controllerprotection.auth.entities.SignupDTO;
import it.develhope.controllerprotection.notification.MailNotificationService;
import it.develhope.controllerprotection.user.entities.Role;
import it.develhope.controllerprotection.user.entities.User;
import it.develhope.controllerprotection.user.repository.RoleRepository;
import it.develhope.controllerprotection.user.repository.UserRepository;
import it.develhope.controllerprotection.user.utils.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class SignupService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private MailNotificationService mailNotificationService;

    public User signup(SignupDTO signupDTO) throws Exception{
        return this.signup(signupDTO, Roles.REGISTERED);
    }

    public User signup(SignupDTO signupDTO,String role)throws Exception{
        User userFromDB = userRepository.findByEmail(signupDTO.getEmail());
        if (userFromDB != null)throw new Exception("User already exists!");
        User user = new User();
        user.setName(signupDTO.getName());
        user.setSurname(signupDTO.getSurname());
        user.setEmail(signupDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        user.setActivationCode(UUID.randomUUID().toString());

        Set<Role> roles = new HashSet<>();
        Optional<Role> userRole = roleRepository.findByName(role.toUpperCase());
        if (!userRole.isPresent())throw new Exception("Role not found");
        roles.add(userRole.get());
        user.setRoles(roles);

        //Servizio di notifica che mando il codice per attivare l'account
        mailNotificationService.sendActivationEmail(user);
        //Salva l'utente nel DB
        return userRepository.save(user);
    }


    public User activate(SignupActivationDTO signupActivationDTO)throws Exception{
        User user = userRepository.findByActivationCode(signupActivationDTO.getActivationCode());
        if (user == null) throw new Exception("Activation code not found");
        user.setActive(true);
        user.setActivationCode(null);
        return userRepository.save(user);
    }



}
