package shootingstar.typing.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import shootingstar.typing.entity.CodeLanguage;
import shootingstar.typing.entity.QText;
import shootingstar.typing.entity.SortingType;
import shootingstar.typing.repository.dto.FindAllTextsByLangDto;
import shootingstar.typing.repository.dto.FindDesTextByIdDto;
import shootingstar.typing.repository.dto.QFindAllTextsByLangDto;
import shootingstar.typing.repository.dto.QFindDesTextByIdDto;

import java.util.List;
import java.util.Optional;

import static shootingstar.typing.entity.QText.*;

public class TextRepositoryCustomImpl implements TextRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final int RECORD_PER_PAGE = 5;

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

    @Override
    public List<FindAllTextsByLangDto> findAllByLangWithSorting(CodeLanguage language, int pageNumber, SortingType sortingType) {
        int firstIndex = (pageNumber - 1) * RECORD_PER_PAGE;
        OrderSpecifier orderSpecifier = createOrderSpecifier(sortingType);

        return queryFactory
                .select(new QFindAllTextsByLangDto(
                        text.id,
                        text.title,
                        text.description))
                .from(text)
                .where(langEq(language))
                .orderBy(orderSpecifier)
                .offset(firstIndex)
                .limit(RECORD_PER_PAGE)
                .fetch();
    }

    private OrderSpecifier createOrderSpecifier(SortingType sortingType) {
        return switch (sortingType) {
            case TITLE_ASC -> new OrderSpecifier<>(Order.ASC, text.title);
            case TITLE_DESC -> new OrderSpecifier<>(Order.DESC, text.title);
            case ID_DESC -> new OrderSpecifier<>(Order.DESC, text.id);
        };
    }

    @Override
    public FindDesTextByIdDto findDesTextById(Long id) {
        return queryFactory
                .select(new QFindDesTextByIdDto(
                        text.title,
                        text.description,
                        text.desText))
                .from(text)
                .where(text.id.eq(id))
                .fetchOne();
    }

    private BooleanExpression langEq(CodeLanguage language) {
        return language != null ? text.lang.eq(language) : null;
    }
}
