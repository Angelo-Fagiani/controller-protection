package it.develhope.controllerprotection.auth.controller;

import it.develhope.controllerprotection.auth.entities.SignupActivationDTO;
import it.develhope.controllerprotection.auth.entities.SignupDTO;
import it.develhope.controllerprotection.auth.service.SignupService;
import it.develhope.controllerprotection.user.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class SignupController {
    @Autowired
    private SignupService signupService;

    private static Logger logger = LoggerFactory.getLogger(SignupController.class);

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody SignupDTO signupDTO) throws Exception {
        try {
            logger.info("Registrazione Utente effettuata con successo!");
            return ResponseEntity.status(HttpStatus.OK).body(signupService.signup(signupDTO));
        } catch (Exception ex) {
            logger.error(ex.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/signup/activation")
    public ResponseEntity signup(@RequestBody SignupActivationDTO signupActivationDTO) throws Exception {
        try {
            logger.info("Attivazione Utente effettuata con successo!");
            return ResponseEntity.status(HttpStatus.OK).body(signupService.activate(signupActivationDTO));
        }catch (Exception ex){
            logger.error(ex.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }

    }
}
