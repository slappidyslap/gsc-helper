package kg.seo.musabaev.gui.api;

import kg.seo.musabaev.api.table.XlsxTableBuilder;
import kg.seo.musabaev.gui.api.domain.SiteMetricsList;

public interface GscMetricsXlsxTableProcessor {

    XlsxTableBuilder getXlsxTableBuilder();

    byte[] process(SiteMetricsList metrics);
}
