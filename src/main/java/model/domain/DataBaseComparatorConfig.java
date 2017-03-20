package model.domain;

public class DataBaseComparatorConfig {
    private String hostConfig;
    private String portConfig;
    private String sidConfig;
    private String userConfig;

    private String connectionTypeConfig;

    private String extractSeparator;

    private int fetchSize;

    private boolean exportTotalAnalysisData; //+
    private boolean exportSplitAnalysisData; //+

    private boolean exportTotalCompareData; //+
    private boolean exportSplitCompareData; //+

    private boolean exportTotalExtraMaster; //+
    private boolean exportSplitExtraMaster;

    private boolean exportTotalExtraTest; //+
    private boolean exportSplitExtraTest;

    private boolean exportTotalDeviationsData;
    private boolean exportSplitDeviationsData;

    private boolean exportTotalOriginalMaster;
    private boolean exportSplitOriginalMaster;
    private boolean exportTotalOriginalTest;
    private boolean exportSplitOriginalTest;

    public DataBaseComparatorConfig() {
    }

    public String getHostConfig() {
        return hostConfig;
    }
    public void setHostConfig(String hostConfig) {
        this.hostConfig = hostConfig;
    }
    public String getPortConfig() {
        return portConfig;
    }
    public void setPortConfig(String portConfig) {
        this.portConfig = portConfig;
    }
    public String getSidConfig() {
        return sidConfig;
    }
    public void setSidConfig(String sidConfig) {
        this.sidConfig = sidConfig;
    }
    public String getUserConfig() {
        return userConfig;
    }
    public void setUserConfig(String userConfig) {
        this.userConfig = userConfig;
    }

    public String getConnectionTypeConfig() {
        return connectionTypeConfig;
    }
    public void setConnectionTypeConfig(String connectionTypeConfig) {
        this.connectionTypeConfig = connectionTypeConfig;
    }

    public String getExtractSeparator() {
        return extractSeparator;
    }
    public void setExtractSeparator(String extractSeparator) {
        this.extractSeparator = extractSeparator;
    }

    public int getFetchSize() {
        return fetchSize;
    }
    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    public boolean isExportTotalAnalysisData() {
        return exportTotalAnalysisData;
    }
    public void setExportTotalAnalysisData(boolean exportTotalAnalysisData) {
        this.exportTotalAnalysisData = exportTotalAnalysisData;
    }
    public boolean isExportSplitAnalysisData() {
        return exportSplitAnalysisData;
    }
    public void setExportSplitAnalysisData(boolean exportSplitAnalysisData) {
        this.exportSplitAnalysisData = exportSplitAnalysisData;
    }
    public boolean isExportTotalCompareData() {
        return exportTotalCompareData;
    }
    public void setExportTotalCompareData(boolean exportTotalCompareData) {
        this.exportTotalCompareData = exportTotalCompareData;
    }
    public boolean isExportSplitCompareData() {
        return exportSplitCompareData;
    }
    public void setExportSplitCompareData(boolean exportSplitCompareData) {
        this.exportSplitCompareData = exportSplitCompareData;
    }
    public boolean isExportTotalExtraMaster() {
        return exportTotalExtraMaster;
    }
    public void setExportTotalExtraMaster(boolean exportTotalExtraMaster) {
        this.exportTotalExtraMaster = exportTotalExtraMaster;
    }
    public boolean isExportSplitExtraMaster() {
        return exportSplitExtraMaster;
    }
    public void setExportSplitExtraMaster(boolean exportSplitExtraMaster) {
        this.exportSplitExtraMaster = exportSplitExtraMaster;
    }
    public boolean isExportTotalExtraTest() {
        return exportTotalExtraTest;
    }
    public void setExportTotalExtraTest(boolean exportTotalExtraTest) {
        this.exportTotalExtraTest = exportTotalExtraTest;
    }
    public boolean isExportSplitExtraTest() {
        return exportSplitExtraTest;
    }
    public void setExportSplitExtraTest(boolean exportSplitExtraTest) {
        this.exportSplitExtraTest = exportSplitExtraTest;
    }
    public boolean isExportTotalDeviationsData() {
        return exportTotalDeviationsData;
    }
    public void setExportTotalDeviationsData(boolean exportTotalDeviationsData) {
        this.exportTotalDeviationsData = exportTotalDeviationsData;
    }
    public boolean isExportSplitDeviationsData() {
        return exportSplitDeviationsData;
    }
    public void setExportSplitDeviationsData(boolean exportSplitDeviationsData) {
        this.exportSplitDeviationsData = exportSplitDeviationsData;
    }
    public boolean isExportTotalOriginalMaster() {
        return exportTotalOriginalMaster;
    }
    public void setExportTotalOriginalMaster(boolean exportTotalOriginalMaster) {
        this.exportTotalOriginalMaster = exportTotalOriginalMaster;
    }
    public boolean isExportSplitOriginalMaster() {
        return exportSplitOriginalMaster;
    }
    public void setExportSplitOriginalMaster(boolean exportSplitOriginalMaster) {
        this.exportSplitOriginalMaster = exportSplitOriginalMaster;
    }
    public boolean isExportTotalOriginalTest() {
        return exportTotalOriginalTest;
    }
    public void setExportTotalOriginalTest(boolean exportTotalOriginalTest) {
        this.exportTotalOriginalTest = exportTotalOriginalTest;
    }
    public boolean isExportSplitOriginalTest() {
        return exportSplitOriginalTest;
    }
    public void setExportSplitOriginalTest(boolean exportSplitOriginalTest) {
        this.exportSplitOriginalTest = exportSplitOriginalTest;
    }

}
