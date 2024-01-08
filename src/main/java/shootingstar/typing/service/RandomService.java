package shootingstar.typing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shootingstar.typing.entity.CodeLanguage;
import shootingstar.typing.repository.TextRepository;
import shootingstar.typing.repository.dto.FindAllTextsByLangDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RandomService {
    private final TextRepository textRepository;
    public long getLangdomCount(CodeLanguage lang){
        //lang이 값은 값을 디비에서 뽑아서 리스트 저장
        List<FindAllTextsByLangDto> MathchingId = textRepository.findAllByLang(lang);
        //총 크기에서 랜덤 값 구하기
        int random = (int) ((Math.random())*MathchingId.size());
        long id = MathchingId.get(random).getId();
        return id;
    }
}
/* 내가 하려고 한 방법
    findall -햇을때 왜 안뜨지 반응이 없지 ㅠ

    1. 기존 findalltextbylangdto 리스트로 가져오기
    2. 사이즈 값 구해서 랜덤값 가져오기
    3. 해당 번호에 대한 id값 getid
 */
