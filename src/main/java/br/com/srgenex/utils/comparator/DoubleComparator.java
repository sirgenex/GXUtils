package br.com.srgenex.utils.comparator;

import java.util.*;

public class DoubleComparator implements Comparator<UUID> {

    Map<UUID, Double> base;

    public DoubleComparator(HashMap<UUID, Double> map) {
        this.base = map;
    }

    public int compare(UUID a, UUID b) {
        return Objects.equals(base.get(a), base.get(b)) ? 0 :
                base.get(a) >= base.get(b) ? -1 : 1;
    }

}