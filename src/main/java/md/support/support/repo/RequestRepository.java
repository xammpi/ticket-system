package md.support.support.repo;

import md.support.support.models.Problem;
import md.support.support.models.Request;

import md.support.support.models.Worker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface RequestRepository extends CrudRepository<Request, Long> {

    @Query("SELECT Count(state) FROM Request u where state=1 and shop=:shopSelect")
    Integer findByCountRequestStateOneAndShop(@Param("shopSelect") String shop);

    @Query(value = "SELECT u FROM Request u WHERE id = :id")
    List<Request> findByIdAll(@Param("id") Long id);

    //View search
    @Query(value = "SELECT u FROM Request u WHERE u.shop = :shop and state=1 and date_created = :dateSort order by id desc")
    Page<Request> findByShopAndDateSort(@Param("shop") String shop, @Param("dateSort") String dateSort, Pageable pageable);

    @Query(value = "SELECT u FROM Request u WHERE u.shop = :shop and state = 1 order by id desc")
    Page<Request> findByShop(@Param("shop") String shop, Pageable pageable);

    @Query(value = "SELECT u FROM Request u WHERE date_created = :dateSort and state = 1 order by id desc")
    List<Request> findByDateSort(@Param("dateSort") String dateSort);

    @Query(value = "SELECT u FROM Request u WHERE date_created = :dateSort and state = 1 order by id desc")
    Page<Request> findByDateSort(@Param("dateSort") String dateSort, Pageable pageable);

    Page<Request> findByWorkerIdAndState(Long id, Integer state, Pageable pageable);

    Page<Request> findByProblemNameAndState(String problem, Integer state, Pageable pageable);

    @Query(value = "SELECT u FROM Request u WHERE (date_created = :dateSort and shop= :shop and state = 1) order by id desc")
    Page<Request> findByDateSortAndShop(@Param("dateSort") String dateSort, String shop, Pageable pageable);

    @Query(value = "SELECT u FROM Request u WHERE (date_close = :currentDay and state = 1) order by id desc")
    Page<Request> findByCurrentDay(@Param("currentDay") String currentDay, Pageable pageable);

    //View search Support
    @Query(value = "SELECT u FROM Request u WHERE (date_close = :currentDay and state = 1 and departmentId= :departmentId) order by id desc")
    Page<Request> findByCurrentDayAndDepartmentId(@Param("currentDay") String currentDay, @Param("departmentId") Long departmentId, Pageable pageable);

    @Query(value = "SELECT u FROM Request u WHERE (u.shop = :shop and state = 1 and departmentId= :departmentId) order by id desc")
    Page<Request> findByShopAndDepartmentId(@Param("shop") String shop, @Param("departmentId") Long departmentId, Pageable pageable);

    @Query(value = "SELECT u FROM Request u WHERE (date_created = :dateSort and state = 1 and departmentId= :departmentId) order by id desc")
    Page<Request> findByDateSortAndDepartmentId(@Param("dateSort") String dateSort, @Param("departmentId") Long departmentId, Pageable pageable);

    //View to Admin
    @Query("SELECT u FROM Request u WHERE state = 0 or state=3 or state=2 or state=4 order by id desc ")
    List<Request> findByState();

    @Query("SELECT u FROM Request u WHERE state = 0 order by id desc ")
    List<Request> findByStateZero();

    @Query("SELECT u FROM Request u WHERE state = 1 order by id desc ")
    List<Request> findByStateOne();

    @Query("SELECT u FROM Request u WHERE state = 2 order by id desc ")
    List<Request> findByStateTwo();

    @Query("SELECT u FROM Request u WHERE state = 3 order by id desc ")
    List<Request> findByStateThree();

    @Query("SELECT u FROM Request u WHERE state = 4 order by id desc ")
    List<Request> findByStateFour();

    //View to Support
    @Query("SELECT u FROM Request u WHERE departmentId = :departmentId and (state = 0 or state=3 or state=2 or state=4) order by id desc ")
    List<Request> findByDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT u FROM Request u WHERE state = 0 and departmentId = :departmentId order by id desc ")
    List<Request> findByStateZeroAndDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT u FROM Request u WHERE state = 1 and departmentId = :departmentId order by id desc ")
    List<Request> findByStateOneAndDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT u FROM Request u WHERE state = 2 and departmentId = :departmentId order by id desc ")
    List<Request> findByStateTwoAndDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT u FROM Request u WHERE state = 3 and departmentId = :departmentId order by id desc ")
    List<Request> findByStateThreeAndDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT u FROM Request u WHERE state = 4 and departmentId = :departmentId order by id desc ")
    List<Request> findByStateFourAndDepartmentId(@Param("departmentId") Long departmentId);


    //View shop to user
    @Query(value = "SELECT u FROM Request u WHERE (date_close = :currentDay and shop =:shop and state = 1) order by id desc")
    Page<Request> findByCurrentDayAndShop(@Param("currentDay") String currentDay, String shop, Pageable pageable);

    @Query("SELECT u FROM Request u WHERE shop=:shop and ( state = 0 or state = 3 or state = 2 or state = 4 )order by id desc ")
    List<Request> findByStateAndShop(@Param("shop") String shop);

    @Query("SELECT u FROM Request u WHERE shop=:shop and state = 0 order by id desc ")
    List<Request> findByStateZeroAndShop(@Param("shop") String shop);

    @Query("SELECT u FROM Request u WHERE shop=:shop and state = 1 order by id desc ")
    List<Request> findByStateOneAndShop(@Param("shop") String shop);

    @Query("SELECT u FROM Request u WHERE shop=:shop and state = 2 order by id desc ")
    List<Request> findByStateTwoAndShop(@Param("shop") String shop);

    @Query("SELECT u FROM Request u WHERE shop=:shop and state = 3 order by id desc ")
    List<Request> findByStateThreeAndShop(@Param("shop") String shop);

    @Query("SELECT u FROM Request u WHERE shop=:shop and state = 4 order by id desc ")
    List<Request> findByStateFourAndShop(@Param("shop") String shop);

    //View count to user
    @Query("SELECT Count(state) FROM Request u where state=0 and shop = :shop")
    Integer findByCountRequestStateZeroAndShop(@Param("shop") String shop);

    @Query("SELECT Count(state) FROM Request u where state=3 and shop = :shop")
    Integer findByCountRequestStateThreeAndShop(@Param("shop") String shop);

    @Query("SELECT Count(state) FROM Request u where state=1 and shop = :shop")
    Integer findByCountRequestStateOne(@Param("shop") String shop);

    @Query("SELECT Count(state) FROM Request u where state=2 and shop = :shop")
    Integer findByCountRequestStateTowAndShop(@Param("shop") String shop);

    @Query("SELECT Count(state) FROM Request u where state=4 and shop = :shop")
    Integer findByCountRequestStateFourAndShop(@Param("shop") String shop);

    @Query("SELECT Count(state) FROM Request where shop = :shop")
    Integer findByCountRequestTotalAndShop(@Param("shop") String shop);

    //View count to Admin
    @Query("SELECT Count(state) FROM Request u where state=0")
    Integer findByCountRequestStateZero();

    @Query("SELECT Count(state) FROM Request u where state=3")
    Integer findByCountRequestStateThree();

    @Query("SELECT Count(state) FROM Request u where state=1")
    Integer findByCountRequestStateOne();

    @Query("SELECT Count(state) FROM Request u where state=2")
    Integer findByCountRequestStateTow();

    @Query("SELECT Count(state) FROM Request u where state=4")
    Integer findByCountRequestStateFour();

    @Query("SELECT Count(state) FROM Request")
    Integer findByCountRequestTotal();

    //View count to Support
    @Query("SELECT Count(state) FROM Request u where departmentId = :departmentId and state=0")
    Integer findCountByStateZeroAndDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT Count(state) FROM Request u where state=3 and departmentId = :departmentId")
    Integer findCountByStateThreeAndDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT Count(state) FROM Request u where state=1 and departmentId = :departmentId")
    Integer findCountByStateOneAndDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT Count(state) FROM Request u where (state=1 and departmentId = :departmentId and shop = :shop)")
    Integer findCountByStateAndDepartmentIdAndShop(@Param("departmentId") Long departmentId,@Param("shop") String shop);


    @Query("SELECT Count(state) FROM Request u where state=2 and departmentId = :departmentId")
    Integer findCountByStateTowAndDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT Count(state) FROM Request u where state=4 and departmentId = :departmentId")
    Integer findCountByStateFourAndDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT Count(state) FROM Request where departmentId = :departmentId ")
    Integer findCountTotalByDepartmentId(@Param("departmentId") Long departmentId);


}
