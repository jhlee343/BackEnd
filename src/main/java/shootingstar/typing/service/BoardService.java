package shootingstar.typing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shootingstar.typing.entity.Text;
import shootingstar.typing.repository.ContentRepository;
import shootingstar.typing.repository.TextRepository;
import shootingstar.typing.repository.dto.BoardContentDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final ContentRepository contentRepository;
    private final TextRepository textRepository;

    public List<BoardContentDto> getBoardList(){

        List<Text> allContent = contentRepository.findAll();

        List<BoardContentDto> res = allContent.stream().map(BoardContentDto::of).toList();
        return res;
    }
}