package shootingstar.typing.repository.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * shootingstar.typing.repository.dto.QFindAllTextsByLangDto is a Querydsl Projection type for FindAllTextsByLangDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QFindAllTextsByLangDto extends ConstructorExpression<FindAllTextsByLangDto> {

    private static final long serialVersionUID = 1707477767L;

    public QFindAllTextsByLangDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> description) {
        super(FindAllTextsByLangDto.class, new Class<?>[]{long.class, String.class, String.class}, id, title, description);
    }

}

