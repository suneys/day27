package entities;

/**
 * @author Administrator
 * @date 2018-12-21
 */
public class RecordEntity {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 批次号
     */
    private String batchNumber;
    /**
     * 程序名
     */
    private String programName;
    /**
     * 当日产量
     */
    private String onDayProduction;
    /**
     * 记录时间
     */
    private String recordTime;

    /**
     * 机床
     */
    private String lineNumber;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getOnDayProduction() {
        return onDayProduction;
    }

    public void setOnDayProduction(String onDayProduction) {
        this.onDayProduction = onDayProduction;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }
}
