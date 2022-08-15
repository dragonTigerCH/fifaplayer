package com.fp.fifaplayer.controller;

import com.fp.fifaplayer.domain.Player;
import com.fp.fifaplayer.domain.Season;
import com.fp.fifaplayer.file.FileStore;
import com.fp.fifaplayer.file.UploadFile;
import com.fp.fifaplayer.form.DatanForm;
import com.fp.fifaplayer.form.PlayerForm;
import com.fp.fifaplayer.form.SeasonForm;

import com.fp.fifaplayer.repository.DatanRepository;
import com.fp.fifaplayer.repository.PlayerRepository;
import com.fp.fifaplayer.repository.SeasonRepository;
import com.fp.fifaplayer.service.DatanService;
import com.fp.fifaplayer.service.PlayerService;
import com.fp.fifaplayer.service.SeasonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PlayerRepository playerRepository;
    private final PlayerService playerService;
    private final SeasonRepository seasonRepository;
    private final SeasonService seasonService;
    private final DatanService datanService;
    private final FileStore fileStore;


    @GetMapping("/player/new")
    public String players(@ModelAttribute PlayerForm playerForm, Model model) {
        model.addAttribute("playerForm", new PlayerForm());
        return "admin/player";
    }

    @PostMapping("/player/new")
    public String newPlayer(@Validated @ModelAttribute PlayerForm playerForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("errors{}", bindingResult);
            return "admin/player";
        }

        playerService.save(playerForm);
        redirectAttributes.addFlashAttribute("msg", "등록되었습니다.");
        return "redirect:/admin/player/new";
    }


    @GetMapping("/season/new")
    public String season(@ModelAttribute SeasonForm seasomForm, Model model) {
        model.addAttribute("seasonForm", new SeasonForm());
        return "admin/season";
    }

    @PostMapping("/season/new")
    public String newSeason(@Validated @ModelAttribute SeasonForm seasonForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest request) throws IOException {

        if (bindingResult.hasErrors()) {
            log.info("errors{}", bindingResult);
            return "admin/season";
        }

        UploadFile attachFile = fileStore.SeasonStoreFile(seasonForm.getAttachFile()); //파일한개일때 서버에 저장하기

        Long save = seasonService.save(seasonForm, attachFile.getStoreFileName());
        //List<UploadFile> imageFiles = fileStore.storFiles(seasonForm.getImageFiles()); //파일이 여러개일때


        redirectAttributes.addFlashAttribute("msg", "등록되었습니다.");
        return "redirect:/admin/season/new";
    }

    @GetMapping("/datan/new")
    public String datan(@ModelAttribute DatanForm datanForm, Model model) {
        List<Player> players = playerRepository.findAll();
        List<Season> seasons = seasonRepository.findAll();

        model.addAttribute("players", players);
        model.addAttribute("seasons", seasons);
        model.addAttribute("datanForm", new DatanForm());

        return "admin/datan";
    }

    @PostMapping("/datan/new")
    public String newDatan(@Validated @ModelAttribute DatanForm datanForm, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes, Model model) throws IOException {

        if (bindingResult.hasErrors()) {
            log.info("errors{}", bindingResult);
            List<Player> players = playerRepository.findAll();
            List<Season> seasons = seasonRepository.findAll();
            model.addAttribute("players", players);
            model.addAttribute("seasons", seasons);
            return "admin/datan";
        }

        Long datan = datanService.save(datanForm);
        if (datan == 0L) {
            redirectAttributes.addFlashAttribute("msg", "이미 같은 클래스 유형의 선수가 있습니다.");
            return "redirect:/admin/datan/new";
        }

        redirectAttributes.addFlashAttribute("msg", "등록되었습니다.");
        return "redirect:/admin/datan/new";
    }

}
