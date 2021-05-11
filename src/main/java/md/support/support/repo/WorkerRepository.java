package md.support.support.repo;

import md.support.support.models.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {
    Worker findByName(String name);

    List<Worker> findByDepartmentId(Long id);

    List<Worker> findWorkerById(Long workerId);
}
