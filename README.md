https://support.google.com/webmasters/thread/165942353/counts-mismatch-between-search-console-dashboard-data-down-from-search-console-api?hl=en

https://support.google.com/webmasters/answer/7042828?sjid=15758032398493584895-EU

https://developers.google.com/webmaster-tools/v1/searchanalytics/query?hl=ru

https://googleapis.dev/java/google-api-services-searchconsole/latest/allclasses-noframe.html

uibootster пул реквекст

мб грууви

у либы не применяется text area стили flatlaf

перепеисать на суммирующий метод 

```
SearchAnalyticsQueryRequest query = new SearchAnalyticsQueryRequest()
    .setStartDate(startDate)
    .setEndDate(endDate)
    .setDimensions(singletonList("device"))
    .setDimensionFilterGroups(asList(
            new ApiDimensionFilterGroup()
                    .setFilters(asList(
                            new ApiDimensionFilter()
                                    .setDimension("device")
                                    .setOperator("equals")
                                    .setExpression("ios")
                    ))
    ));
```