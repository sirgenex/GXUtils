package br.com.srgenex.utils.progressbar;

import lombok.Data;

@Data
public class ProgressBar {

    private final Integer bars;
    private final String completed, uncompleted;

}
