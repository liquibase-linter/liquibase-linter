package io.github.liquibaselinter.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static io.github.liquibaselinter.report.ReportItem.ReportItemType.ERROR;
import static io.github.liquibaselinter.report.ReportItem.ReportItemType.IGNORED;

@JsonDeserialize(builder = ReporterConfig.Builder.class)
public class ReporterConfig {
    private final boolean enabled;
    private final String path;
    private final Set<ReportItem.ReportItemType> filter;

    public ReporterConfig(ReporterConfig.BaseBuilder<?> builder) {
        enabled = builder.isEnabled();
        path = builder.getPath();
        filter = builder.getFilter();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getPath() {
        return path;
    }

    public Set<ReportItem.ReportItemType> getFilter() {
        return filter;
    }

    public abstract static class BaseBuilder<B extends BaseBuilder<B>> {
        private boolean enabled = true;
        private String path;
        private Set<ReportItem.ReportItemType> filter = new LinkedHashSet<>(Arrays.asList(ERROR, IGNORED));

        @SuppressWarnings("unchecked")
        protected B self() {
            return (B) this;
        }

        public B withEnabled(boolean enabled) {
            this.enabled = enabled;
            return self();
        }

        public boolean isEnabled() {
            return enabled;
        }

        public B withPath(String path) {
            this.path = path;
            return self();
        }

        public String getPath() {
            return path;
        }

        @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        public B withFilter(ReportItem.ReportItemType... filter) {
            this.filter = ImmutableSet.copyOf(filter);
            return self();
        }

        public Set<ReportItem.ReportItemType> getFilter() {
            return filter;
        }

        public ReporterConfig build() {
            return new ReporterConfig(this);
        }
    }

    public static class Builder extends BaseBuilder<Builder> {
        // default concrete builder
    }

    public static Builder builder() {
        return new Builder();
    }
}
