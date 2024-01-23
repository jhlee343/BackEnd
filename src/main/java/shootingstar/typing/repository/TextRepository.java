package shootingstar.typing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shootingstar.typing.entity.CodeLanguage;
import shootingstar.typing.entity.Text;

public interface TextRepository extends JpaRepository<Text, Long>, TextRepositoryCustom {


    /**
     * P1: 언어별 랜덤 지문 선택
     * 정렬없이 언어별 모든 지문 리스트 반환
     */
    @Query(value = "select t.id from Text t where t.lang = :lang order by rand() limit 1")
    Long randomTrackByLang(@Param("lang") CodeLanguage language);
}
