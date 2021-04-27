package md.support.support.repo;

import md.support.support.models.Shop;
import md.support.support.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByName(String name);
}
