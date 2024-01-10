package shootingstar.typing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public Text(CodeLanguage lang, String title, String description, String desText, String typingText) {
        this.lang = lang;
        this.title = title;
        this.description = description;
        this.desText = desText;
        this.typingText = typingText;
    }
}
