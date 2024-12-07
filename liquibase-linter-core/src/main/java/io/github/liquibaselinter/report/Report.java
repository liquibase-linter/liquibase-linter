package io.github.liquibaselinter.report;

import static java.util.Collections.emptyList;

import io.github.liquibaselinter.config.Config;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Report {

    private final Config config;
    private final List<ReportItem> items;

    public Report(Config config, List<ReportItem> items) {
        this.config = config;
        this.items = Optional.ofNullable(items).map(Collections::unmodifiableList).orElse(emptyList());
    }

    public Config getConfig() {
        return config;
    }

    public List<ReportItem> getItems() {
        return items;
    }
}
