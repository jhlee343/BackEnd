package shootingstar.typing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shootingstar.typing.entity.CodeLanguage;
import shootingstar.typing.repository.TextRepository;
import shootingstar.typing.repository.dto.BoardContentDto;
import shootingstar.typing.repository.dto.BoardResponseDto;
import shootingstar.typing.service.BoardService;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/api/{lang}/list")
    public ResponseEntity<BoardResponseDto> getContentsList(CodeLanguage lang){

        List<BoardContentDto> boardList = boardService.getBoardList();
        BoardResponseDto res = BoardResponseDto.builder()
                .result("result")
                .data(boardList)
                .build();

        return ResponseEntity.ok().body(res);

    }
}