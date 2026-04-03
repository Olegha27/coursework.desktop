package com.example.courseworkitfu.hibernateOperations;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("foodApp");

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
}