package com.fp.fifaplayer.controller;

import com.fp.fifaplayer.domain.Datan;
import com.fp.fifaplayer.domain.Datan_Comments;
import com.fp.fifaplayer.domain.Player;
import com.fp.fifaplayer.domain.Season;
import com.fp.fifaplayer.form.SearchForm;
import com.fp.fifaplayer.repository.*;
import com.fp.fifaplayer.repository.custom.DatanRepositoryCustom;
import com.fp.fifaplayer.repository.custom.Datan_CommentsRepositoryCustom;
import com.fp.fifaplayer.service.Datan_CommentsService;
import com.fp.fifaplayer.service.SeasonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/dataninfo")
@RequiredArgsConstructor
public class DatanInfoController {

    private final DatanRepository datanRepository;
    private final SeasonRepository seasonRepository;
    private final PlayerRepository playerRepository;
    private final SeasonService seasonService;
    private final DatanRepositoryCustom datanRepositoryCustom;
    private final Datan_CommentsRepository datan_commentsRepository;
    private final Datan_CommentsService datan_commentsService;
    private final Datan_CommentsRepositoryCustom datan_commentsRepositoryCustom;

    @GetMapping("/evaluation")
    public String evaluation(SearchForm searchForm, Model model, @PageableDefault(size = 10, sort = "regdate", direction = Sort.Direction.DESC) Pageable pageable) {

        //처음 comparison 페이지로딩후 불러올 리스트
        Page<Datan_Comments> datan_Comments = datan_commentsRepository.findAll(pageable);

        List<Season> seasons = seasonRepository.findAll();
        List<Player> players = playerRepository.findAll();

        //검색 조건에 따른 불러올 리스트
        if (!(searchForm.getSeason() == null && searchForm.getPosition() == null && searchForm.getSearchPlayer() == null)) {
            datan_Comments = datan_commentsRepositoryCustom.findBySeasonAndPositionAndSearchPlayer(searchForm.getSeason()
                    , searchForm.getPosition(), searchForm.getSearchPlayer(), pageable);
        }

        log.info("테스트 = {}", datan_Comments);

        int startPage = Math.max(1, datan_Comments.getPageable().getPageNumber() - 4);
        int endPage = Math.min(datan_Comments.getTotalPages(), datan_Comments.getPageable().getPageNumber() + 4);

        model.addAttribute("endPage", endPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("seasons", seasons);
        model.addAttribute("players", players);
        model.addAttribute("datan_Comments", datan_Comments);
        model.addAttribute("searchForm", new SearchForm());

        return "/dataninfo/evaluation";
    }

    @GetMapping("/comparison")
    public String comparison(SearchForm searchForm, Model model, @PageableDefault(size = 10, sort = "overall", direction = Sort.Direction.DESC) Pageable pageable) {

        //처음 comparison 페이지로딩후 불러올 리스트
        Page<Datan> datans = datanRepository.findAll(pageable);

        List<Season> seasons = seasonRepository.findAll();
        List<Player> players = playerRepository.findAll();

        //검색 조건에 따른 불러올 리스트
        if (!(searchForm.getSeason() == null && searchForm.getPosition() == null && searchForm.getSearchPlayer() == null)) {
            datans = datanRepositoryCustom.findBySeasonAndPositionAndSearchPlayer(searchForm.getSeason()
                    , searchForm.getPosition(), searchForm.getSearchPlayer(), pageable);
        }

        int startPage = Math.max(1, datans.getPageable().getPageNumber() - 4);
        int endPage = Math.min(datans.getTotalPages(), datans.getPageable().getPageNumber() + 4);

        model.addAttribute("endPage", endPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("seasons", seasons);
        model.addAttribute("players", players);
        model.addAttribute("datans", datans);
        model.addAttribute("searchForm", new SearchForm());

        return "/dataninfo/comparison";
    }

    @GetMapping("/comparison/orderby")
    public String comparison_orderBy(@RequestParam(value = "datanData[]") String[] datanData, String dataInfo, String orderBy, Model model) {

        log.info("datanData = {}", datanData);

        //받아온 문자열배열 값 long배열로 변환후 리스트변환
        List<Long> datanIds = new ArrayList<Long>();
        long[] longs = Arrays.stream(datanData).mapToLong(Long::parseLong).toArray();
        for (Long ids : longs) {
            datanIds.add(ids);
        }

        List<Datan> datans = null;
        if (orderBy.equals("desc")) {
            datans = datanRepositoryCustom.findByIdOrderByDesc(datanIds, dataInfo);
        } else if (orderBy.equals("asc")) {
            datans = datanRepositoryCustom.findByIdOrderByAsc(datanIds, dataInfo);
        }
        model.addAttribute("datans", datans);

        return "/dataninfo/comparison :: fragment-datanValue";
    }


}
