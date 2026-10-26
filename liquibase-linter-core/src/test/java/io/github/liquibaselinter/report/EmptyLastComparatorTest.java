package io.github.liquibaselinter.report;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class EmptyLastComparatorTest {

    private final EmptyLastComparator comparator = new EmptyLastComparator();

    @Test
    void shouldSortEmptyValuesLast() {
        List<String> values = Arrays.asList("", "b", null, "a");

        values.sort(comparator);

        assertThat(values).startsWith("a", "b");
        assertThat(values.subList(2, 4)).containsExactlyInAnyOrder("", null);
    }

    @Test
    void shouldConsiderEmptyValuesEqual() {
        assertThat(comparator.compare(null, "")).isZero();
        assertThat(comparator.compare("", "a")).isPositive();
        assertThat(comparator.compare("a", null)).isNegative();
    }
}
