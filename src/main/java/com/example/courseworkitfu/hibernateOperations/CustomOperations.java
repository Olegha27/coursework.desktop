package com.example.courseworkitfu.hibernateOperations;

import com.example.courseworkitfu.model.Message;
import com.example.courseworkitfu.model.User;
import com.example.courseworkitfu.utils.PasswordUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

import static com.example.courseworkitfu.HelloApplication.emf;

public class CustomOperations extends GenericOperations {

    public CustomOperations(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public List<Message> getMessagesByOrderId(int orderId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "select m from Message m where m.order.id = :orderId order by m.sentAt asc",
                            Message.class
                    ).setParameter("orderId", orderId)
                    .getResultList();
        } finally {
            em.close();
        }
    }


    public User getUserByCredentials(String username, String password) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);

            query.select(root).where(
                    cb.equal(root.get("username"), username)
            );

            TypedQuery<User> typedQuery = entityManager.createQuery(query);
            User user = typedQuery.getResultStream().findFirst().orElse(null);

            if (user == null) {
                return null;
            }

            if (!PasswordUtils.checkPassword(password, user.getPassword())) {
                return null;
            }

            if (!PasswordUtils.isBcryptHash(user.getPassword())) {
                user.setPassword(PasswordUtils.hashPassword(password));
                update(user);
            }

            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
