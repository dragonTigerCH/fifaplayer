package com.fp.fifaplayer.domain;

import com.fp.fifaplayer.file.UploadFile;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "file")
    private String attachFile;


    @Builder
    public Season(Long id, String name, String attachFile) {
        this.id = id;
        this.name = name;
        this.attachFile = attachFile;
    }
}
