package entities;

/**
 * SMTPMS 实体类
 */
public class SmtpmsEntity {
    /**
     * 机型
     */
    private String machineNo;
    /**
     * 批次代码
     */
    private String batchNumber;
    /**
     * A面程序
     */
    private String programA;
    /**
     * B面程序
     */
    private String programB;
    /**
     * 状态B
     */
    private String statusB;
    /**
     * 计划数量
     */
    private int planned;
    /**
     * 维备件
     */
    private int repairableSpares;
    /**
     * 状态
     */
    private String statusA;
    /**
     * 当日产量
     */
    private int onDayProduction;
    /**
     * 累计产量
     */
    private int cumulativeProduction;
    /**
     * 定额产量
     */
    private String ratedProduction;

    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getProgramA() {
        return programA;
    }

    public void setProgramA(String programA) {
        this.programA = programA;
    }

    public String getProgramB() {
        return programB;
    }

    public void setProgramB(String programB) {
        this.programB = programB;
    }

    public String getStatusB() {
        return statusB;
    }

    public void setStatusB(String statusB) {
        this.statusB = statusB;
    }

    public int getPlanned() {
        return planned;
    }

    public void setPlanned(int planned) {
        this.planned = planned;
    }

    public int getRepairableSpares() {
        return repairableSpares;
    }

    public void setRepairableSpares(int repairableSpares) {
        this.repairableSpares = repairableSpares;
    }

    public String getStatusA() {
        return statusA;
    }

    public void setStatusA(String statusA) {
        this.statusA = statusA;
    }

    public int getOnDayProduction() {
        return onDayProduction;
    }

    public void setOnDayProduction(int onDayProduction) {
        this.onDayProduction = onDayProduction;
    }

    public int getCumulativeProduction() {
        return cumulativeProduction;
    }

    public void setCumulativeProduction(int cumulativeProduction) {
        this.cumulativeProduction = cumulativeProduction;
    }

    public String getRatedProduction() {
        return ratedProduction;
    }

    public void setRatedProduction(String ratedProduction) {
        this.ratedProduction = ratedProduction;
    }

}
