package web.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import web.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UsersRepositoryImpl implements UsersRepository {

    @PersistenceContext
    private EntityManager em;


    @Override
    public void addUser(User user) {
        em.persist(user);
    }

    @Override
    public void deleteUser(Long id) {
        User findUser = em.find(User.class, id);
        if (findUser != null) {
            em.remove(findUser);
        }
    }

    @Override
    public User findUser(Long id) {
        return em.find(User.class, id);
    }

    @Override
    public void updateUser(User user) {
        em.merge(user);
    }

    @Override
    public List<User> getAllUsers() {
        Query query = em.createQuery("FROM User", User.class);
        return query.getResultList();
    }

    @Override
    public List<User> getAllUsersWithRole() {
        return em.createQuery("SELECT DISTINCT u FROM User u JOIN FETCH u.roles", User.class).getResultList();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return em.createQuery("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList()
                .stream()
                .findFirst();
    }
}
