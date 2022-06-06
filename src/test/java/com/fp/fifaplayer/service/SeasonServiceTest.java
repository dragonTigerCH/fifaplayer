package com.fp.fifaplayer.service;

import com.fp.fifaplayer.domain.Season;
import com.fp.fifaplayer.file.FileStore;
import com.fp.fifaplayer.file.UploadFile;
import com.fp.fifaplayer.form.SeasonForm;
import com.fp.fifaplayer.repository.SeasonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SeasonServiceTest {

    @Autowired
    SeasonService seasonService;
    @Autowired
    SeasonRepository seasonRepository;


    @Test
    @DisplayName("시즌 저장")
    public void seasonSave() throws Exception {
        //given
        MockMultipartFile testFile = new MockMultipartFile("img_file",
                "test.png",
                "image/png",
                new FileInputStream("C:\\fifaPrj\\fifaplayer\\src\\main\\resources\\static\\img\\배경.png"));
        SeasonForm seasonForm = new SeasonForm("테스트시즌",testFile,null);
        FileStore fileStore = new FileStore();
        UploadFile uploadFile = fileStore.SeasonStoreFile(testFile);

        //when
        seasonService.save(seasonForm, uploadFile.getStoreFileName());
        Season season = seasonRepository.findByName(seasonForm.getName()).orElse(null);
        //then
        assertEquals(seasonForm.getName(),season.getName());

    }
}