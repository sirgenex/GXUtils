package br.com.srgenex.utils.range;

import lombok.Data;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
@Data
public class IntegerRange {

    private final Integer min, max;

    public Integer getRandom(){
        return Objects.equals(min, max) ? max : ThreadLocalRandom.current().nextInt(min, max);
    }

}
