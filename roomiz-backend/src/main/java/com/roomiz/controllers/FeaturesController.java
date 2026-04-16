package com.roomiz.controllers;

import com.roomiz.entities.Product;
import com.roomiz.entities.UserEntity;
import com.roomiz.responses.BasicResponse;
import com.roomiz.responses.ScanResponse;
import com.roomiz.service.Persist;
import com.roomiz.service.Scanner;
import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.roomiz.utils.Errors.*;


@RestController
public class FeaturesController {
    @Autowired
    private Persist persist;

    @Autowired
    private Scanner recietScanner;

    @PostConstruct
    public void init() {
    }

    @RequestMapping("/scan")
    public BasicResponse scan(String token, @RequestBody Map<String, String> payload) {
        UserEntity user = persist.getUserByToken(token);
        if (user == null) {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }

        String base64Image = payload.get("image");
        if (base64Image == null || base64Image.isEmpty()) {
            return new BasicResponse(false, ERROR_MISSING_VALUES);
        }

        try {

            List<Product> products = recietScanner.scan(base64Image);

//            List<Product> products = new ArrayList<>();
//            products.add(new Product("חלב תנובה 3%", 1, 5.90));
//            products.add(new Product("לחם אחיד", 1, 7.50));
//            products.add(new Product("קוטג' 5%", 2, 9.80));

            return new ScanResponse(true, null, products);
        } catch (Exception e) {
            e.printStackTrace();
            return new BasicResponse(false, SOMETHING_WENT_WRONG);
        }

    }

    @RequestMapping("/confirm-scan")
    public BasicResponse confirmScan(String token, @RequestBody List<Product> confirmedProducts) {
        UserEntity user = persist.getUserByToken(token);
        if (user == null) {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
        if (confirmedProducts == null || confirmedProducts.isEmpty()) {
            return new BasicResponse(false, ERROR_MISSING_VALUES);
        }

        for (Product product : confirmedProducts) {
            user.getProducts().add(product);
            persist.save(product);
        }
        persist.save(user);
        return new BasicResponse(true, null);
    }


}
