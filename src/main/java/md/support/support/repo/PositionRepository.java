package md.support.support.repo;

import md.support.support.models.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position,Long> {
    Position findByName(String name);
}
