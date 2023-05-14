package br.com.srgenex.range;

import lombok.Data;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Data
public class IntegerRange {

    private final Integer min, max;

    public Integer getRandom(){
        return Objects.equals(min, max) ? max : ThreadLocalRandom.current().nextInt(min, max);
    }

}
