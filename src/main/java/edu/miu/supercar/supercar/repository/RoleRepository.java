package edu.miu.supercar.supercar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.miu.supercar.supercar.model.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository <Role,Long>{
    @Query("SELECT a FROM Admin a WHERE a.username = ?1")
    Role findByName(String name);
}
