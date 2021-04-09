package md.support.support.repo;

import md.support.support.models.Problem;
import org.springframework.data.repository.CrudRepository;

public interface ProblemRepository extends CrudRepository<Problem, Long> {
}
