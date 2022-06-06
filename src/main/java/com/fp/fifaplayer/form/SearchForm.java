package com.fp.fifaplayer.form;

import lombok.Data;

import java.util.List;

@Data
public class SearchForm {


    private List<String> season;

    private List<String> position;

    private String searchPlayer;

}
