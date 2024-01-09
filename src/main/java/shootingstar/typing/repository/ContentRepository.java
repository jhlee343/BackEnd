package shootingstar.typing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shootingstar.typing.entity.Text;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Text, Long> {

}