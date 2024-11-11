package io.github.liquibaselinter.report;

public interface Reporter {
    void processReport(Report report);
    boolean isEnabled();

    interface Factory<R extends Reporter, C extends ReporterConfig> {
        boolean supports(String name);
        Class<? extends C> getConfigClass();
        R create(C config);
        R create(boolean enabled);
        R create(String path);
    }
}
