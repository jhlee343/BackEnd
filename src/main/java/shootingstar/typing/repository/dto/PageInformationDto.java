package shootingstar.typing.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.List;

@Data
public class PageInformationDto {
    private Long totalRecord;
    private Long currentPage;
    private Long totalPage;

    @QueryProjection
    public PageInformationDto(Long totalRecord, Long currentPage, Long totalPage) {
        this.totalRecord = totalRecord;
        this.currentPage = currentPage;
        this.totalPage = totalPage;
    }
}
