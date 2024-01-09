package shootingstar.typing.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import shootingstar.typing.entity.CodeLanguage;
import shootingstar.typing.repository.dto.FindAllTextsByLangDto;
import shootingstar.typing.repository.dto.QFindAllTextsByLangDto;

import java.util.List;

import static shootingstar.typing.entity.QText.*;

public class TextRepositoryCustomImpl implements TextRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public TextRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<FindAllTextsByLangDto> findAllByLang(CodeLanguage language) {
        return queryFactory
                .select(new QFindAllTextsByLangDto(
                        text.id,
                        text.title,
                        text.description))
                .from(text)
                .where(langEq(language))
                .fetch();
    }



    private BooleanExpression langEq(CodeLanguage language) {
        return language != null ? text.lang.eq(language) : null;
    }
}
