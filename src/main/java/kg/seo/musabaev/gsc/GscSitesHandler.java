package kg.seo.musabaev.gsc;

/**
 * Основной обработчик для сбора данных из Google Search Console
 * и создания Excel отчета
 */
@Deprecated
class GscSitesHandler {
/*
    private static final Logger log = LoggerFactory.getLogger(GscSitesHandler.class);

    private final SearchConsoleService searchConsoleService;
    private final ExcelReportBuilder excelBuilder;
    private final String startDate;
    private final String endDate;

    public GscSitesHandler(GoogleSearchConsole searchConsole, LocalDate startDate, LocalDate endDate) {
        this.searchConsoleService = new SearchConsoleService(searchConsole);
        this.excelBuilder = new ExcelReportBuilder();
        this.startDate = startDate.toString();
        this.endDate = endDate.toString();
    }

    *//**
     * Запускает процесс сбора данных из GSC и формирования отчета
     *
     * @return CompletableFuture для асинхронного выполнения
     *//*
    public CompletableFuture<Void> start() {
        log.info("Начат сбор данных из GSC за период {} - {}", startDate, endDate);

        return CompletableFuture.runAsync(() -> {
            List<WmxSite> sites = searchConsoleService.getSites();
            log.info("Получено сайтов для обработки: {}", sites.size());

            int processedCount = 0;
            int skippedCount = 0;

            for (WmxSite site : sites) {
                String siteUrl = site.getSiteUrl();
                log.info("Обработка сайта: {}", siteUrl);

                try {
                    SearchAnalyticsQueryResponse response = searchConsoleService.getAnalytics(
                            siteUrl,
                            null,
                            null
                    );

                    if (response == null) {
                        log.warn("Сайт {} пропущен - не подтвержден", siteUrl);
                        skippedCount++;
                        continue;
                    }

                    SiteMetrics metrics = searchConsoleService.getMetrics(siteUrl, response);
                    excelBuilder.addSiteMetrics(metrics);

                    processedCount++;
                    log.info("Сайт {} успешно обработан ({}/{})",
                            siteUrl, processedCount, sites.size());

                } catch (Exception e) {
                    log.error("Ошибка при обработке сайта: {}", siteUrl, e);
                    skippedCount++;
                }
            }

            excelBuilder.autoSizeColumns();

            log.info("Сбор данных завершен. Обработано: {}, Пропущено: {}",
                    processedCount, skippedCount);
        });
    }

    *//**
     * Сохраняет Excel файл по указанному пути
     *
     * @param path путь для сохранения файла
     *//*
    public void saveExcelFile(File path) {
        excelBuilder.save(path);
    }*/
}