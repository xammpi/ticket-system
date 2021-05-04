package md.support.support.repo;

import md.support.support.models.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    List<Problem> findAllByDepartmentId(Long departmentId);

    Problem findByName(String name);
}
