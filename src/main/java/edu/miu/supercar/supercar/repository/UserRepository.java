package edu.miu.supercar.supercar.repository;


import edu.miu.supercar.supercar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    @Query("SELECT u FROM User u WHERE u.username = ?1")
    User findByUsername(String name);
}
