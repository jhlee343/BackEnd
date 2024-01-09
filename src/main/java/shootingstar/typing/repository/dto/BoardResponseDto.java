package shootingstar.typing.repository.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BoardResponseDto {
    private String result;
    private Object data;
}
