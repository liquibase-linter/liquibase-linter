package io.github.liquibaselinter.report;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.Comparator;

public class EmptyLastComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        if (isEmpty(o1)) {
            return isEmpty(o2) ? 0 : 1;
        } else if (isEmpty(o2)) {
            return -1;
        }
        return o1.compareTo(o2);
    }
}
