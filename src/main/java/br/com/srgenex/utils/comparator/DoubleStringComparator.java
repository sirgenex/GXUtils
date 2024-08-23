package br.com.srgenex.utils.comparator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DoubleStringComparator implements Comparator<String> {

    Map<String, Double> base;

    public DoubleStringComparator(HashMap<String, Double> map) {
        this.base = map;
    }

    public int compare(String a, String b) {
        return Objects.equals(base.get(a), base.get(b)) ? 0 :
                base.get(a) >= base.get(b) ? -1 : 1;
    }

}