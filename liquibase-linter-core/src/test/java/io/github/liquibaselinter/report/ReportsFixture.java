package io.github.liquibaselinter.report;

import static io.github.liquibaselinter.report.ReportItem.ReportItemType.ERROR;
import static io.github.liquibaselinter.report.ReportItem.ReportItemType.IGNORED;
import static io.github.liquibaselinter.report.ReportItem.ReportItemType.PASSED;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import io.github.liquibaselinter.config.Config;
import io.github.liquibaselinter.config.RuleConfig;
import java.util.ArrayList;
import java.util.List;

public final class ReportsFixture {

    private ReportsFixture() {}

    public static Report fullReport() {
        List<ReportItem> items = new ArrayList<>();

        items.add(new ReportItem(null, null, "error-rule", ERROR, "No change log"));

        String dbChangeLog1 = "src/main/resources/ddl/first.xml";
        items.add(new ReportItem(dbChangeLog1, null, "error-rule", ERROR, "Error message 1"));
        items.add(new ReportItem(dbChangeLog1, null, "ignored-rule", IGNORED, "Ignored message 1"));
        items.add(new ReportItem(dbChangeLog1, null, "passed-rule", PASSED, "Passed message 1"));

        String changeSet1 = "2020010101";
        items.add(new ReportItem(dbChangeLog1, changeSet1, "error-rule", ERROR, "Error message 2"));
        items.add(new ReportItem(dbChangeLog1, changeSet1, "ignored-rule", IGNORED, "Ignored message 2"));
        items.add(new ReportItem(dbChangeLog1, changeSet1, "passed-rule", PASSED, "Passed message 2"));

        String changeSet2 = "2020010102";
        items.add(new ReportItem(dbChangeLog1, changeSet2, "error-rule", ERROR, "Error message 3.1"));
        items.add(new ReportItem(dbChangeLog1, changeSet2, "error-rule", ERROR, "Error message 3.2"));
        items.add(new ReportItem(dbChangeLog1, changeSet2, "ignored-rule", IGNORED, "Ignored message 3"));
        items.add(new ReportItem(dbChangeLog1, changeSet2, "passed-rule", PASSED, "Passed message 3"));

        String dbChangeLog2 = "src/main/resources/ddl/second.xml";
        String changeSet3 = "2020010103";
        items.add(new ReportItem(dbChangeLog2, changeSet3, "error-rule", ERROR, "Error message 4\nwith newline"));

        Config config = new Config.Builder()
            .withRules(
                ImmutableListMultimap.<String, RuleConfig>builder()
                    .put("a", RuleConfig.builder().withEnabled(false).build())
                    .put("a", RuleConfig.builder().withEnabled(true).build())
                    .put("b", RuleConfig.builder().withEnabled(false).build())
                    .build()
            )
            .build();

        return new Report(config, items);
    }

    public static Report emptyReport() {
        return new Report(new Config.Builder().withRules(ImmutableListMultimap.of()).build(), ImmutableList.of());
    }
}
