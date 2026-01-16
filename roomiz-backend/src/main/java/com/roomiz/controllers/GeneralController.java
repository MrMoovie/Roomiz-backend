package com.roomiz.controllers;

import com.roomiz.entities.*;
import com.roomiz.responses.*;
import com.roomiz.service.Persist;
import com.roomiz.utils.GeneralUtils;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;

import java.util.*;

import static com.roomiz.utils.Constants.USER_TYPE_CLIENT;
import static com.roomiz.utils.Errors.*;

@RestController
public class GeneralController {
    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {
    }



    @RequestMapping("/addApartment")
    public BasicResponse addApartment(String token, String address){
        try {
            UserEntity userEntity = persist.getUserByToken(token);
            if(userEntity.getApartment()!=null){
                return new BasicResponse(false, ERROR_APARTMENT_ALREADY_EXIST);
            }
            if(userEntity!=null){
                ApartmentEntity apartment = new ApartmentEntity();
                apartment.setAddress(address);
                // *
                Random rnd = new Random();
                int passcode = rnd.nextInt(10000,99999);
                // *
                apartment.setPasscode(passcode);
                userEntity.setApartment(apartment);
                persist.save(apartment);
                persist.save(userEntity);
                return new BasicResponse(true, 0);
            }else{
                return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/get-my-apartment")
    public BasicResponse getMyApartment(String token){
        UserEntity user = persist.getUserByToken(token);
        if(user!=null){
            System.out.println(user.getUsername()+" logged in");
            ApartmentEntity apartment = user.getApartment();
            if(apartment!=null) {
                return new ApartmentResponse(true, null, apartment);
            }else{
                return new BasicResponse(false, ERROR_APARTMENT_NOT_FOUND);
            }
        }else{
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
    }

    @RequestMapping("/addRoommate")
    public BasicResponse addRoommate(String token, String username){
        UserEntity admin = persist.getUserByToken(token);
        if(admin!=null){
            UserEntity user = persist.getUserByUsername(username);
            if(user!=null){
                if(user.getApartment()!=null){
                    return new BasicResponse(false, ERROR_APARTMENT_ALREADY_EXIST);
                }
                user.setApartment(admin.getApartment());
                persist.save(user);
                return new BasicResponse(true, null);
            }
            return new BasicResponse(false, ERROR_USER_NOT_FOUND);
        }
        return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
    }

    @RequestMapping("/get-my-roommates")
    public BasicResponse getMyRoommates(String token){
        UserEntity user = persist.getUserByToken(token);
        if(user!=null){
            if(user.getApartment()!=null){
                List<UserEntity> roommates = persist.getUsersByApartmentId(user.getApartment().getId());
                List<UserDTO> safeUser = new ArrayList<>();
                for(UserEntity roommate: roommates){
                    safeUser.add(new UserDTO(roommate));
                }

                return new RoommatesResponse(true, null, safeUser);
            }else{
                return new BasicResponse(false, ERROR_APARTMENT_NOT_FOUND);
            }
        }else{
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
    }

//    @RequestMapping("/get-user-posts")
//    public BasicResponse getUserPosts(String token) {
//        ClientEntity clientEntity = persist.getClientByToken(token);
//        if (clientEntity != null) {
//            List<PostEntity> posts = persist.getPostsByClientId(clientEntity.getId()).stream().filter(post -> !post.isDeleted()).toList();
//            List<BidEntity> myProposals = persist.getProposalsByClientId(clientEntity.getId());
//            return new ClientPostsResponse(true, null, posts, myProposals);
//        } else {
//            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//    @RequestMapping("/get-all-posts")
//    public BasicResponse getAllPosts(String token) {
//        ProffesionalEntity proffesionalEntity = persist.getProfessionalByToken(token);
//        if (proffesionalEntity != null) {
//            List<PostEntity> posts = persist.getAllPost().stream().filter(post -> !post.isDeleted()).toList();
//            return new ProffesionalPostsResponse(true, null, posts);
//        } else {
//            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
//        }
//    }
//    @RequestMapping("/get-all-categories")
//    public BasicResponse getAllCategories(String token) {
//        BasicUser basicUser = persist.getUserByToken(token);
//        if (basicUser != null) {
//            List<CategoryEntity> categories = persist.getAllCategories().stream().filter(category -> !category.isDeleted()).toList();
//            return new CategoriesResponse(true, null, categories);
//        } else {
//            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//    @RequestMapping("/get-default-params")
//    public BasicResponse getDefaultParams (String token) {
//        BasicUser basicUser = persist.getUserByToken(token);
//        if (basicUser != null) {
//            return new DefaultParamResponse(true, null, basicUser);
//        } else {
//            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//    @RequestMapping("/add-post")
//    public BasicResponse addPost (String token, String text, String fileLink, String area,int categoryId) {
//        ClientEntity clientEntity = persist.getClientByToken(token);
//        CategoryEntity categoryEntity = persist.getCategoryByCategoryId(categoryId);
//        if (clientEntity != null) {
//            PostEntity postEntity = new PostEntity();
//            postEntity.setClientEntity(clientEntity);
//            postEntity.setText(text);
//            postEntity.setArea(area);
//            postEntity.setFileLink(fileLink);
//            if (categoryEntity != null) {
//                postEntity.setCategoryEntity(categoryEntity);
//            }
//            persist.save(postEntity);
//            return new BasicResponse(true, null);
//        } else {
//            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//    @RequestMapping("/delete-post")
//    public BasicResponse deletePost (String token, int postId) {
//        ClientEntity clientEntity = persist.getClientByToken(token);
//        if (clientEntity != null) {
//            PostEntity postEntity = persist.getPostByPostId(postId);
//            if (clientEntity.getId()==postEntity.getClientEntity().getId()) {
//                postEntity.setDeleted(true);
//                persist.save(postEntity);
//                return new BasicResponse(true, null);
//            }else {
//                return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
//            }
//        } else {
//            return new BasicResponse(false,ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//
//    @RequestMapping ("/get-all-users")
//    public List<ClientEntity> getAllUsers () {
//        return persist.loadList(ClientEntity.class);
//    }
//
//
//
//    @RequestMapping("/make-bid")
//    public BasicResponse makeBid (String token, int postId, float proposedPrice, String description) {
//        ProffesionalEntity proffesionalEntity = persist.getProfessionalByToken(token);
//        if (proffesionalEntity != null) {
//            PostEntity postEntity = persist.getPostByPostId(postId);
//            if (postEntity != null) {
//                BidEntity bidEntity = new BidEntity();
//                bidEntity.setProffesionalEntity(proffesionalEntity);
//                bidEntity.setPostEntity(postEntity);
//                bidEntity.setProposedPrice(proposedPrice);
//                bidEntity.setStatus(0);
//                bidEntity.setDescription(description);
//                persist.save(bidEntity);
//                return new BasicResponse(true, null);
//            } else {
//                return new BasicResponse(false, ERROR_POST_NOT_FOUND);
//            }
//        } else {
//            return new BasicResponse(false,ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//
//    @RequestMapping("/my-bids")
//    public BasicResponse myBids (String token) {
//        ProffesionalEntity proffesionalEntity = persist.getProfessionalByToken(token);
//        if (proffesionalEntity != null) {
//            List<BidEntity> myBids = persist.getBidsByProfessionalId(proffesionalEntity.getId());
//            return new BidsResponseModel(true, null, myBids);
//        } else {
//            return new BasicResponse(false,ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//    @RequestMapping("/my-proposals")
//    public BasicResponse myProposals (String token) {
//        ClientEntity clientEntity = persist.getClientByToken(token);
//        if (clientEntity != null) {
//            List<BidEntity> myProposals = persist.getProposalsByClientId(clientEntity.getId());
//            return new BidsResponseModel(true, null, myProposals);
//        } else {
//            return new BasicResponse(false,ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//    @RequestMapping("/get-post")
//    public BasicResponse getUserPosts(String token, int id) {
//        ClientEntity clientEntity = persist.getClientByToken(token);
//        if (clientEntity != null) {
//            PostEntity postEntity = persist.loadObject(PostEntity.class, id);
//            List<BidEntity> myProposals = persist.getProposalsByClientId(clientEntity.getId());
//            return new ClientPostResponse(true, null, postEntity, myProposals);
//        } else {
//            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//    @RequestMapping("/get-bid")
//    public BasicResponse getBid(String token, int id) {
//        ClientEntity clientEntity = persist.getClientByToken(token);
//        if (clientEntity != null) {
//            BidEntity bidEntity = persist.loadObject(BidEntity.class, id);
//            List<MessageEntity> conversation = persist.getConversation(bidEntity.getId());
//            Collections.reverse(conversation);
//            return new BidResponse(true, null, bidEntity, conversation);
//        } else {
//            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//    @RequestMapping("/send-message")
//    public BasicResponse sendMessage(String token, String newMessage, int bidId) {
//        ClientEntity clientEntity = persist.getClientByToken(token);
//        if (clientEntity != null) {
//            BidEntity bidEntity = persist.loadObject(BidEntity.class, bidId);
//            MessageEntity messageEntity = new MessageEntity();
//            messageEntity.setBidEntity(bidEntity);
//            messageEntity.setContent(newMessage);
//            messageEntity.setClient(true);
//            messageEntity.setCreationDate(new Date());
//            persist.save(messageEntity);
//            return new BasicResponse(true, null);
//        } else {
//            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//

}
