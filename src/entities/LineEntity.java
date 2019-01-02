package entities;
public class LineEntity {

	private String programName; // 程序名
	private String bathNumber; // 批号
	private int totalCount; // 总数
	private int okCount; // 确认数

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getBathNumber() {
		return bathNumber;
	}

	public void setBathNumber(String bathNumber) {
		this.bathNumber = bathNumber;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getOkCount() {
		return okCount;
	}

	public void setOkCount(int okCount) {
		this.okCount = okCount;
	}
}