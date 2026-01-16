package com.roomiz.controllers;

import com.roomiz.entities.*;
import com.roomiz.responses.*;
import com.roomiz.service.Persist;
import com.roomiz.utils.GeneralUtils;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.roomiz.utils.Constants.USER_TYPE_CLIENT;
import static com.roomiz.utils.Errors.*;

@RestController
public class LoginController {
    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {
    }

    @RequestMapping("/login")
    public BasicResponse getUser (String username, String password){
        System.out.println(username+" "+ password+" logged in");
        try {
            if(username!=null && password!=null){
                UserEntity userEntity = persist.getUserByUsernameAndPassword(username, password);
                if(userEntity!=null){
                    String token = GeneralUtils.hashMd5(username, password);
                    userEntity.setToken(token);
                    persist.save(userEntity);
                    return new LoginResponse(true,null, token, userEntity.getId());
                }else {
                    return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
                }
            }else {
                return new BasicResponse(false, ERROR_MISSING_USERNAME_OR_PASSWORD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


//    @RequestMapping ("/login")
//    public BasicResponse getUser (String username, String password, int selectedType) {
//        System.out.println(username+" "+password+" "+selectedType);
//        try {
//            if (username != null && password != null) {
//                if (selectedType == USER_TYPE_CLIENT) {
//                    ClientEntity clientEntity = persist.getUserByUsernameAndPassword(username, password);
//                    if (clientEntity != null) {
//                        String token = GeneralUtils.hashMd5(username, password);
//                        clientEntity.setToken(token);
//                        persist.save(clientEntity);
//                        return new LoginResponse(true, null, 1, token, clientEntity.getId(), selectedType);
//                    } else {
//                        return new BasicResponse(false,  ERROR_WRONG_CREDENTIALS);
//                    }
//                } else {
//                    ProffesionalEntity proffesionalEntity = persist.getProffesionalByUsernameAndPassword(username, password);
//                    if (proffesionalEntity != null) {
//                        String token = GeneralUtils.hashMd5(username, password);
//                        proffesionalEntity.setToken(token);
//                        persist.save(proffesionalEntity);
//                        return new LoginResponse(true, null, 1, token, proffesionalEntity.getId(), selectedType);
//                    } else {
//                        return new BasicResponse(false,  ERROR_WRONG_CREDENTIALS);
//                    }
//
//                }
//            } else {
//                return new BasicResponse(false, ERROR_MISSING_USERNAME_OR_PASSWORD);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
    @RequestMapping("/signup")
    public BasicResponse addUser(String username, String password){
        try {
            if(username!=null && password!=null){
                UserEntity userEntity = persist.getUserByUsernameAndPassword(username, password);
                if(userEntity==null){
                    userEntity = new UserEntity();
                    userEntity.setUsername(username);
                    userEntity.setPassword(password);
                    String token = GeneralUtils.hashMd5(username, password);
                    userEntity.setToken(token);
                    persist.save(userEntity);
                    return new LoginResponse(true, null, token, userEntity.getId());
                }else {
                    return new BasicResponse(false, ERROR_USERNAME_ALREADY_EXISTS);
                }
            }else {
                return new BasicResponse(false, ERROR_MISSING_USERNAME_OR_PASSWORD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




//    @RequestMapping("/signup")
//    public BasicResponse addUser(int selectedType, String username,String password) {
//        try {
//            if (username != null  && password != null) {
//                BasicUser userEntity = persist.getUserByUsername(username);
//                if (userEntity != null) {
//                    return new BasicResponse(false,ERROR_USERNAME_ALREADY_EXISTS);
//                }
//                else {
//                    if (selectedType == USER_TYPE_CLIENT) {
//
//                        ClientEntity clientEntity = new ClientEntity();
//                        clientEntity.setUsername(username);
//                        clientEntity.setPassword(password);
//                        String token = GeneralUtils.hashMd5(username, password);
//                        clientEntity.setToken(token);
//                        persist.save(clientEntity);
//                        return new LoginResponse(true, null, 1, token, clientEntity.getId(), selectedType);
//
//                    }
//                    else {
//
//                            ProffesionalEntity proffesionalEntity = new ProffesionalEntity();
//                            proffesionalEntity.setUsername(username);
//                            proffesionalEntity.setPassword(password);
//                            String token = GeneralUtils.hashMd5(username, password);
//                            proffesionalEntity.setToken(token);
//                            persist.save(proffesionalEntity);
//                            return new LoginResponse(true, null, 1, token, proffesionalEntity.getId(), selectedType);
//
//                    }
//                }
//            }else {
//                return new BasicResponse(false, ERROR_MISSING_VALUES);
//            }
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }

}
