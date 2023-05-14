package br.com.srgenex.comparator;

import java.util.*;

@SuppressWarnings("unused")
public class IntegerComparator implements Comparator<UUID> {

    Map<UUID, Integer> base;

    public IntegerComparator(HashMap<UUID, Integer> map) {
        this.base = map;
    }

    public int compare(UUID a, UUID b) {
        return Objects.equals(base.get(a), base.get(b)) ? 1 :
                base.get(a) >= base.get(b) ? -1 : 1;
    }

}