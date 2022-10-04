package it.develhope.controllerprotection.auth.controller;


import it.develhope.controllerprotection.auth.entities.RequestPasswordDTO;
import it.develhope.controllerprotection.auth.entities.RestorePasswordDTO;
import it.develhope.controllerprotection.auth.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/password")
public class PasswordRestoreController {

    @Autowired
    private PasswordService passwordService;

    @PostMapping("/request")
    public void request(@RequestBody RequestPasswordDTO requestPasswordDTO)throws Exception{
        try {
            passwordService.request(requestPasswordDTO);
        }catch (Exception e){
        }
    }

    @PostMapping("/restore")
    public void restore(@RequestBody RestorePasswordDTO restorePasswordDTO)throws Exception{
        passwordService.restore(restorePasswordDTO);
    }
}


