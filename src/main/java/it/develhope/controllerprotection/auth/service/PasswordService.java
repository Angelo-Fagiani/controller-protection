package it.develhope.controllerprotection.auth.service;


import it.develhope.controllerprotection.auth.entities.RequestPasswordDTO;
import it.develhope.controllerprotection.auth.entities.RestorePasswordDTO;
import it.develhope.controllerprotection.notification.MailNotificationService;
import it.develhope.controllerprotection.user.entities.User;
import it.develhope.controllerprotection.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private MailNotificationService mailNotificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User request(RequestPasswordDTO requestPasswordDTO)throws Exception{
        User userFromDB = userRepository.findByEmail(requestPasswordDTO.getEmail());
        if (userFromDB == null) throw new Exception("Cannot find User");
        userFromDB.setPasswordResetCode(UUID.randomUUID().toString());
        mailNotificationService.sendPasswordResetMail(userFromDB);
        return userRepository.save(userFromDB);


    }

    public User restore(RestorePasswordDTO restorePasswordDTO)throws Exception{
        User userFromDB = userRepository.findByPasswordResetCode(restorePasswordDTO.getResetPasswordCode());
        if(userFromDB == null) throw new Exception("Cannot find user");
        userFromDB.setPassword(passwordEncoder.encode(restorePasswordDTO.getNewPassword()));
        userFromDB.setPasswordResetCode(null);

        //I am activating the user
        userFromDB.setActive(true);
        userFromDB.setActivationCode(null);

        return userRepository.save(userFromDB);
    }
}

