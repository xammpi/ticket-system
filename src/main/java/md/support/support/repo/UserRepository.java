package md.support.support.repo;

import md.support.support.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("SELECT u FROM User u where name = :name")
    List<User> findUserByName(@Param("name") String name);

    List<User> findAllByOrderByDepartment();

}
