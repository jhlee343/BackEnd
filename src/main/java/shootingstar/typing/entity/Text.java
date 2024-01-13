package shootingstar.typing.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private CodeLanguage lang;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    @Column(columnDefinition="LONGTEXT")
    private String desText;
    @NotNull
    @Column(columnDefinition="LONGTEXT")
    private String typingText;
    @NotNull
    private LocalDate createDate;

    public Text(CodeLanguage lang, String title, String description, String desText, String typingText) {
        this.lang = lang;
        this.title = title;
        this.description = description;
        this.desText = desText;
        this.typingText = typingText;
        this.createDate = LocalDate.now();
    }
}
