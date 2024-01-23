package shootingstar.typing.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import shootingstar.typing.entity.CodeLanguage;
import shootingstar.typing.entity.SortingType;
import shootingstar.typing.repository.dto.*;

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
                        text.description,
                        text.createDate,
                        text.author))
                .from(text)
                .where(langEq(language))
                .orderBy()
                .fetch();
    }

    /**
     * P2: 언어별 페이지
     */
    @Override
    public Page<FindAllTextsByLangDto> findAllTextsByLang(CodeLanguage language, SortingType sortingType, String search, Pageable pageable) {
        List<FindAllTextsByLangDto> content = queryFactory
                .select(new QFindAllTextsByLangDto(
                        text.id,
                        text.title,
                        text.description,
                        text.createDate,
                        text.author))
                .from(text)
                .where(langEq(language), containTitle(search))
                .orderBy(createOrderSpecifier(sortingType))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(text.count())
                .from(text)
                .where(langEq(language), containTitle(search));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    /**
     * P2: 정렬 방식
     */
    private OrderSpecifier createOrderSpecifier(SortingType sortingType) {
        return switch (sortingType) {
            case TITLE_ASC -> new OrderSpecifier<>(Order.ASC, text.title);
            case TITLE_DESC -> new OrderSpecifier<>(Order.DESC, text.title);
            case DATE_ASC -> new OrderSpecifier<>(Order.DESC, text.id);
            case DATE_DESC -> new OrderSpecifier<>(Order.ASC, text.id);
        };
    }

    /**
     * P3: 설명 페이지
     */
    @Override
    public FindDesTextByIdDto findDesTextByLangAndId(CodeLanguage language, Long id) {
        return queryFactory
                .select(new QFindDesTextByIdDto(
                        text.title,
                        text.description,
                        text.desText,
                        text.author))
                .from(text)
                .where(langEq(language), text.id.eq(id))
                .fetchOne();
    }

    @Override
    public FindTypingTextDto findTypingTextByLangAndId(CodeLanguage language, Long id) {
        return queryFactory
                .select(new QFindTypingTextDto(
                        text.typingText,
                        text.author))
                .from(text)
                .where(langEq(language), text.id.eq(id))
                .fetchOne();
    }

    private BooleanExpression langEq(CodeLanguage language) {
        return language != null ? text.lang.eq(language) : null;
    }
    private BooleanExpression containTitle(String search) {
        return StringUtils.hasText(search) ? text.title.contains(search) : null;
    }
}
