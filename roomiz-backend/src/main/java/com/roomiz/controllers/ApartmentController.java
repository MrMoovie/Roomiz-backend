package com.roomiz.controllers;

import com.roomiz.entities.*;
import com.roomiz.responses.*;
import com.roomiz.service.Persist;
import com.roomiz.utils.GeneralUtils;
import org.apache.tomcat.jni.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.Basic;

import java.util.*;

import static com.roomiz.utils.Constants.USER_TYPE_CLIENT;
import static com.roomiz.utils.Errors.*;

@RestController
public class ApartmentController {
    private static final Logger log = LoggerFactory.getLogger(ApartmentController.class);
    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {
    }

    @RequestMapping("/add-apartment")
    public BasicResponse addApartment(String token, String address) {
        UserEntity userEntity = persist.getUserByToken(token);
        try {
            if (userEntity == null) {
                return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
            }
            if (userEntity.getApartment() != null) {
                return new BasicResponse(false, ERROR_APARTMENT_ALREADY_EXIST);
            }
            ApartmentEntity apartment = new ApartmentEntity();
            apartment.setAddress(address);
            // *
            Random rnd = new Random();
            int passcode = rnd.nextInt(10000, 99999);
            // *
            apartment.setPasscode(passcode);
            userEntity.setApartment(apartment);
            userEntity.setIsAdmin(true);
            persist.save(apartment);
            persist.save(userEntity);
            return new BasicResponse(true, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/get-my-apartment")
    public BasicResponse getMyApartment(String token) {
        UserEntity user = persist.getUserByToken(token);
        if (user == null) {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }

        System.out.println(user.getUsername() + " logged in");

        ApartmentEntity apartment = user.getApartment();
        if (apartment == null) {
            return new BasicResponse(false, ERROR_APARTMENT_NOT_FOUND);
        }
        return new ApartmentResponse(true, null, apartment);
    }

    @RequestMapping("/delete-apartment")
    public BasicResponse deleteApartment(String token) {
        UserEntity user = persist.getUserByToken(token);

        if (user == null) {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }

        if (user.getApartment() == null) {
            return new BasicResponse(false, ERROR_APARTMENT_NOT_FOUND);
        }

        user.getApartment().setDeleted(true);
        persist.save(user);

        List<UserEntity> roommates = persist.getUsersByApartmentId(user.getApartment().getId());
        for (UserEntity roommate : roommates) {
            roommate.setApartment(null);
            persist.save(roommate);
        }
        return new BasicResponse(true, 0);
    }



    @RequestMapping("/add-roommate")
    public BasicResponse addRoommate(String token, String username) {
        UserEntity admin = persist.getUserByToken(token);
        if (admin == null) {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
        if(admin.getApartment()==null){
            return new BasicResponse(false, ERROR_APARTMENT_NOT_FOUND);
        }
        UserEntity user = persist.getUserByUsername(username);
        if (user == null) {
            return new BasicResponse(false, ERROR_USER_NOT_FOUND);
        }
        if (user.getApartment() != null) {
            return new BasicResponse(false, ERROR_APARTMENT_ALREADY_EXIST);
        }
        user.setApartment(admin.getApartment());
        persist.save(user);
        return new BasicResponse(true, null);
    }

    @RequestMapping("/get-my-roommates")
    public BasicResponse getMyRoommates(String token) {
        UserEntity user = persist.getUserByToken(token);

        if (user == null) {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }

        if (user.getApartment() == null) {
            return new BasicResponse(false, ERROR_APARTMENT_NOT_FOUND);
        }

        List<UserEntity> roommates = persist.getUsersByApartmentId(user.getApartment().getId());
        List<UserDTO> safeUser = new ArrayList<>();
        for (UserEntity roommate : roommates) {
            safeUser.add(new UserDTO(roommate));
        }
        return new RoommatesResponse(true, null, safeUser);
    }

    @RequestMapping("/delete-roommate")
    public BasicResponse deleteRoommate(String token, String username){
        UserEntity admin = persist.getUserByToken(token);
        if(admin == null){
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
        if(!admin.getIsAdmin()){
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
        if(admin.getApartment()==null){
            return new BasicResponse(false, ERROR_APARTMENT_NOT_FOUND);
        }
        UserEntity user = persist.getUserByUsername(username);
        if(user==null){
            return new BasicResponse(false, ERROR_USER_NOT_FOUND);
        }
        if(user == admin){
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
        if (user.getApartment().equals(admin.getApartment())){
            return new BasicResponse(false, ERROR_USER_NOT_FOUND);
        }

        user.setApartment(null);
        user.setIsAdmin(false);
        persist.save(user);
        return new BasicResponse(true, 0);
    }



    @RequestMapping("/add-admin")
    public BasicResponse addAdmin(String token, String username) {
        UserEntity admin = persist.getUserByToken(token);
        if (admin == null) {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
        if(admin.getApartment()==null){
            return new BasicResponse(false, ERROR_APARTMENT_NOT_FOUND);
        }
        if (!admin.getIsAdmin()) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
        UserEntity user = persist.getUserByUsername(username);
        if (user == null) {
            return new BasicResponse(false, ERROR_USER_NOT_FOUND);
        }
        if (admin.getApartment().equals(user.getApartment())) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
        user.setIsAdmin(true);
        persist.save(user);
        return new BasicResponse(true, 0);
    }

    @RequestMapping("/get-is-admin")
    public BasicResponse getIsAdmin(String token){
        UserEntity user = persist.getUserByToken(token);
        if(user==null){
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
        if(user.getApartment()==null){
            return new BasicResponse(false, ERROR_APARTMENT_NOT_FOUND);
        }
        return new IsAdminResponse(true, 0, user.getIsAdmin());
    }

    @RequestMapping("/delete-admin")
    public BasicResponse deleteAdmin(String token, String username){
        UserEntity admin = persist.getUserByToken(token);
        if (admin == null) {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
        if (!admin.getIsAdmin()) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
        if(admin.getApartment()==null){
            return new BasicResponse(false, ERROR_APARTMENT_NOT_FOUND);
        }
        UserEntity user = persist.getUserByUsername(username);
        if (user == null) {
            return new BasicResponse(false, ERROR_USER_NOT_FOUND);
        }
        if (admin.getApartment().equals(user.getApartment())) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
        user.setIsAdmin(false);
        persist.save(user);
        return new BasicResponse(true, 0);
    }

    public BasicResponse permissionValidation(UserEntity admin, UserEntity user){
        if (admin == null) {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
        if(admin.getApartment()==null){
            return new BasicResponse(false, ERROR_APARTMENT_NOT_FOUND);
        }
        if (!admin.getIsAdmin()) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
        if (user == null) {
            return new BasicResponse(false, ERROR_USER_NOT_FOUND);
        }
        if (admin.getApartment().equals(user.getApartment())) {
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
        if(admin.equals(user)){
            return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
        }
        return new BasicResponse(true, 0);
    }






}
