package com.roomiz.service;


import com.roomiz.entities.*;
import org.apache.catalina.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Transactional
@Component
@SuppressWarnings("unchecked")
public class Persist {

    private static final Logger LOGGER = LoggerFactory.getLogger(Persist.class);

    private final SessionFactory sessionFactory;


    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    public <T> void saveAll(List<T> objects) {
        for (T object : objects) {
            sessionFactory.getCurrentSession().saveOrUpdate(object);
        }
    }

    public <T> void remove(Object o){
        sessionFactory.getCurrentSession().remove(o);
    }

    public Session getQuerySession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(Object object) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(object);
    }

    public <T> T loadObject(Class<T> clazz, int oid) {
        return this.getQuerySession().get(clazz, oid);
    }

    public <T> List<T> loadList(Class<T> clazz)
    {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM " + clazz.getSimpleName()).list();
    }

    public UserEntity getUserByEmail(String email) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM UserEntity WHERE email = :email ", UserEntity.class)
                .setParameter("email", email)
                .uniqueResult();
    }
//    public BasicUser getUserByUsername(String username) {
//        BasicUser user = getClientByUsername(username);
//        if (user == null) {
//            user = getProffesionalByUsername(username);
//        }
//        return user;
//    }
    public UserEntity getUserByUsername(String username) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM UserEntity " + " WHERE username = :username ", UserEntity.class)
                .setParameter("username", username)
                .uniqueResult();
    }
    public ProffesionalEntity getProffesionalByUsername(String username) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM ProffesionalEntity " + " WHERE username = :username ", ProffesionalEntity.class)
                .setParameter("username", username)
                .uniqueResult();
    }

    public List<UserEntity> getUsersByCity(String city) {
        return null;
    }

    public UserEntity getUserByUsernameAndPassword(String username, String password) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM UserEntity  " +
                        "WHERE username = :username " +
                        "AND password = :password", UserEntity.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
    }

    public ApartmentEntity getApartmentByClientId(int clientId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM ApartmentEntity " +
                        "WHERE clientEntity.id = :clientId", ApartmentEntity.class)
                .setParameter("clientId", clientId)
                .uniqueResult();
    }

    public List<PostEntity> getAllPost() {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM PostEntity", PostEntity.class)
                .list();
    }
    public List<CategoryEntity> getAllCategories() {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM CategoryEntity", CategoryEntity.class)
                .list();
    }

    public PostEntity getPostByPostId(int id) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM PostEntity " +
                        "WHERE id = :id", PostEntity.class)
                .setParameter("id", id)
                .uniqueResult();
    }
    public CategoryEntity getCategoryByCategoryId(int id) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM CategoryEntity " +
                        "WHERE id = :id", CategoryEntity.class)
                .setParameter("id", id)
                .uniqueResult();
    }



    public UserEntity getUserByToken(String token) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM UserEntity " +
                        "WHERE token = :token", UserEntity.class)
                .setParameter("token", token)
                .uniqueResult();
    }


    public ProffesionalEntity getProffesionalByUsernameAndPassword(String username, String password) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM ProffesionalEntity " +
                        "WHERE username = :username " +
                        "AND password = :password", ProffesionalEntity.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
    }



    public List<UserEntity> getUsersByApartmentId(int apartmentID) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM UserEntity user " +
                        "WHERE user.apartment.id = :apartmentID", UserEntity.class)
                .setParameter("apartmentID", apartmentID)
                .list();
    }

    public List<BidEntity> getProposalsByClientId(int clientId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM BidEntity bid " +
                                "WHERE bid.postEntity.clientEntity.id = :clientId", BidEntity.class)
                .setParameter("clientId", clientId)
                .list();
    }

    public List<MessageEntity> getConversation (int bidId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery(
                        "FROM MessageEntity msg " +
                                "WHERE msg.bidEntity.id = :bidId " +
                                "ORDER BY msg.id DESC ",
                        MessageEntity.class)
                .setParameter("bidId", bidId)
                .setMaxResults(10)
                .list();
    }





}