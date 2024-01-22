package shootingstar.typing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shootingstar.typing.entity.CodeLanguage;
import shootingstar.typing.entity.Text;

public interface TextRepository extends JpaRepository<Text, Long>, TextRepositoryCustom {

    @Query(value = "select id from Text where Text.lang = :lang order by rand()")
    Long randomTrackByLang(@Param("lang") CodeLanguage language);
}
