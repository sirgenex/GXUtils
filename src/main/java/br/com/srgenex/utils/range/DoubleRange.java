package br.com.srgenex.utils.range;

import lombok.Data;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
@Data
public class DoubleRange {

    private final Double min, max;

    public Double getRandom(){
        return Objects.equals(min, max) ? max : ThreadLocalRandom.current().nextDouble(min, max);
    }

}
