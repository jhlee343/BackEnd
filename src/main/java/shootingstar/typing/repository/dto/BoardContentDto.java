package shootingstar.typing.repository.dto;

import lombok.*;
import shootingstar.typing.entity.Text;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BoardContentDto {

    private Long id;
    private String title;

    public static BoardContentDto of(Text text) {
        return BoardContentDto.builder().id(text.getId())
                .title(text.getTitle())
                .build();
    }
}