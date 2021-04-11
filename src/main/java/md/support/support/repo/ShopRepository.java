package md.support.support.repo;

import md.support.support.models.Shop;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShopRepository extends CrudRepository<Shop, Long> {

    Shop findByName(String shop);

}
