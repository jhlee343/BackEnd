package shootingstar.typing.repository.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * shootingstar.typing.repository.dto.QFindDesTextByIdDto is a Querydsl Projection type for FindDesTextByIdDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QFindDesTextByIdDto extends ConstructorExpression<FindDesTextByIdDto> {

    private static final long serialVersionUID = 1405371388L;

    public QFindDesTextByIdDto(com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> description, com.querydsl.core.types.Expression<String> desText) {
        super(FindDesTextByIdDto.class, new Class<?>[]{String.class, String.class, String.class}, title, description, desText);
    }

}

