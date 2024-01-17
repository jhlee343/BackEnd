package shootingstar.typing.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.util.StringUtils;
import shootingstar.typing.entity.CodeLanguage;
import shootingstar.typing.entity.SortingType;
import shootingstar.typing.repository.dto.*;

import java.util.List;

import static shootingstar.typing.entity.QText.*;

public class TextRepositoryCustomImpl implements TextRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final int RECORD_PER_PAGE = 5;

    public TextRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * P1: 언어별 랜덤 지문 선택
     * 정렬없이 언어별 모든 지문 리스트 반환
     */
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
                .fetch();
    }

    /**
     * P2: 언어별 페이지
     */
    @Override
    public List<FindAllTextsByLangDto> findAllTextsByLang(CodeLanguage language, int pageNumber, SortingType sortingType, String search) {
        int firstIndex = (pageNumber - 1) * RECORD_PER_PAGE;
        OrderSpecifier orderSpecifier = createOrderSpecifier(sortingType);

        return queryFactory
                .select(new QFindAllTextsByLangDto(
                        text.id,
                        text.title,
                        text.description,
                        text.createDate,
                        text.author))
                .from(text)
                .where(langEq(language), containTitle(search))
                .orderBy(orderSpecifier)
                .offset(firstIndex)
                .limit(RECORD_PER_PAGE)
                .fetch();
    }

    /**
     * P2: 언어별 페이지 정보
     */
    @Override
    public PageInformationDto findPageInformation(CodeLanguage language, long currentPage, String search) {
        long totalRecord = queryFactory
                .select(text.count())
                .from(text)
                .where(langEq(language), containTitle(search))
                .fetchFirst();

        long totalPage = totalRecord / RECORD_PER_PAGE + 1;

        PageInformationDto pageInformationDto = new PageInformationDto(totalRecord, currentPage, totalPage);

        return pageInformationDto;
    }

    /**
     * P2: 정렬 방식
     */
    private OrderSpecifier createOrderSpecifier(SortingType sortingType) {
        return switch (sortingType) {
            case ID_ASC -> new OrderSpecifier<>(Order.ASC, text.id);
            case ID_DESC -> new OrderSpecifier<>(Order.DESC, text.id);
            case TITLE_ASC -> new OrderSpecifier<>(Order.ASC, text.title);
            case TITLE_DESC -> new OrderSpecifier<>(Order.DESC, text.title);
            case DATE_ASC -> new OrderSpecifier<>(Order.ASC, text.createDate);
            case DATE_DESC -> new OrderSpecifier<>(Order.DESC, text.createDate);
        };
    }

    /**
     * P3: 설명 페이지
     */
    @Override
    public FindDesTextByIdDto findDesTextById(Long id) {
        return queryFactory
                .select(new QFindDesTextByIdDto(
                        text.title,
                        text.description,
                        text.desText,
                        text.author))
                .from(text)
                .where(text.id.eq(id))
                .fetchOne();
    }

    private BooleanExpression langEq(CodeLanguage language) {
        return language != null ? text.lang.eq(language) : null;
    }
    private BooleanExpression containTitle(String search) {
        return StringUtils.hasText(search) ? text.title.contains(search) : null;
    }
}
