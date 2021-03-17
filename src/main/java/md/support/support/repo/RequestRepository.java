package md.support.support.repo;

import md.support.support.models.Request;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface RequestRepository extends CrudRepository<Request, Long> {


    @Query("SELECT u FROM Request u WHERE state = 0 or state=3 order by id desc ")
    List<Request> findByState();

    @Query(value = "SELECT u FROM Request u WHERE u.shop = :shop and state=1 and date_created = :dateSort order by id desc")
    List<Request> findByShopAndDateSort(@Param("shop") String shop, @Param("dateSort") String dateSort);

    @Query(value = "SELECT u FROM Request u WHERE u.shop = :shop and state = 1 order by id desc")
    List<Request> findByShop(@Param("shop") String shop);

    @Query(value = "SELECT u FROM Request u WHERE date_created = :dateSort and state = 1 order by id desc")
    List<Request> findByDateSort(@Param("dateSort") String dateSort);

    @Query("SELECT Count(state) FROM Request u where state=0")
    Integer findByCountRequestStateZero();

    @Query("SELECT Count(state) FROM Request u where state=3")
    Integer findByCountRequestStateThree();

    @Query("SELECT Count(state) FROM Request u where state=1")
    Integer findByCountRequestStateOne();

    @Query("SELECT Count(state) FROM Request")
    Integer findByCountRequestTotal();

    @Query(value = "SELECT u FROM Request u WHERE id = :id")
    List<Request> findByIdAll(@Param("id") Long id);

}
