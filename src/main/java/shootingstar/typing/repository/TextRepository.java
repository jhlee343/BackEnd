package shootingstar.typing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shootingstar.typing.entity.Text;

public interface TextRepository extends JpaRepository<Text, Long>, TextRepositoryCustom {
}
