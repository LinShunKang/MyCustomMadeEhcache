package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class WorkExperience implements Serializable {

    private static final long serialVersionUID = -8978217958411651814L;

    public static final int PUBLIC_CLOSE = 1; //该经历对公司的boss不可见

    private long workEduId;

    private long id;

    private long userId;

    private String company;

    private int industryCode;

    private String industryCatgroy;

    private String industry;

    private int positon;

    private int position;

    private int positonLv2;

    private int positionLv2;

    private String positionCatgroy;//为用户所选的position对应的name

    private String positonName;

    private String positionName;//为用户手写的职位名称

    private int officeYears;

    private int isPublic;

    private String department;

    private String responsibility;

    private String startDate;

    private String startDateMonth;//这里只给老版本的web端用，4.6加了月分

    private String endDate;

    private String endDateMonth;//这里只给老版本的web端用，4.6加了月分

    private long companyId;

    private String workEmphasis;

    private List<String> workEmphasisArr;

    private String workEmphasisStr;

    private String workPerformance;

    private long customPositionId;

    private long customIndustryId;

    private Date addTime;

    private Date updateTime;

//    private List<Person> personList;

    public static int getPublicClose() {
        return PUBLIC_CLOSE;
    }

    public long getWorkEduId() {
        return workEduId;
    }

    public void setWorkEduId(long workEduId) {
        this.workEduId = workEduId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(int industryCode) {
        this.industryCode = industryCode;
    }

    public String getIndustryCatgroy() {
        return industryCatgroy;
    }

    public void setIndustryCatgroy(String industryCatgroy) {
        this.industryCatgroy = industryCatgroy;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public int getPositon() {
        return positon;
    }

    public void setPositon(int positon) {
        this.positon = positon;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPositonLv2() {
        return positonLv2;
    }

    public void setPositonLv2(int positonLv2) {
        this.positonLv2 = positonLv2;
    }

    public int getPositionLv2() {
        return positionLv2;
    }

    public void setPositionLv2(int positionLv2) {
        this.positionLv2 = positionLv2;
    }

    public String getPositionCatgroy() {
        return positionCatgroy;
    }

    public void setPositionCatgroy(String positionCatgroy) {
        this.positionCatgroy = positionCatgroy;
    }

    public String getPositonName() {
        return positonName;
    }

    public void setPositonName(String positonName) {
        this.positonName = positonName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public int getOfficeYears() {
        return officeYears;
    }

    public void setOfficeYears(int officeYears) {
        this.officeYears = officeYears;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartDateMonth() {
        return startDateMonth;
    }

    public void setStartDateMonth(String startDateMonth) {
        this.startDateMonth = startDateMonth;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndDateMonth() {
        return endDateMonth;
    }

    public void setEndDateMonth(String endDateMonth) {
        this.endDateMonth = endDateMonth;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getWorkEmphasis() {
        return workEmphasis;
    }

    public void setWorkEmphasis(String workEmphasis) {
        this.workEmphasis = workEmphasis;
    }

    public List<String> getWorkEmphasisArr() {
        return workEmphasisArr;
    }

    public void setWorkEmphasisArr(List<String> workEmphasisArr) {
        this.workEmphasisArr = workEmphasisArr;
    }

    public String getWorkEmphasisStr() {
        return workEmphasisStr;
    }

    public void setWorkEmphasisStr(String workEmphasisStr) {
        this.workEmphasisStr = workEmphasisStr;
    }

    public String getWorkPerformance() {
        return workPerformance;
    }

    public void setWorkPerformance(String workPerformance) {
        this.workPerformance = workPerformance;
    }

    public long getCustomPositionId() {
        return customPositionId;
    }

    public void setCustomPositionId(long customPositionId) {
        this.customPositionId = customPositionId;
    }

    public long getCustomIndustryId() {
        return customIndustryId;
    }

    public void setCustomIndustryId(long customIndustryId) {
        this.customIndustryId = customIndustryId;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

//    public List<Person> getPersonList() {
//        return personList;
//    }
//
//    public void setPersonList(List<Person> personList) {
//        this.personList = personList;
//    }

    @Override
    public String toString() {
        return "WorkExperience{" +
                "workEduId=" + workEduId +
                ", id=" + id +
                ", userId=" + userId +
                ", company='" + company + '\'' +
                ", industryCode=" + industryCode +
                ", industryCatgroy='" + industryCatgroy + '\'' +
                ", industry='" + industry + '\'' +
                ", positon=" + positon +
                ", position=" + position +
                ", positonLv2=" + positonLv2 +
                ", positionLv2=" + positionLv2 +
                ", positionCatgroy='" + positionCatgroy + '\'' +
                ", positonName='" + positonName + '\'' +
                ", positionName='" + positionName + '\'' +
                ", officeYears=" + officeYears +
                ", isPublic=" + isPublic +
                ", department='" + department + '\'' +
//                ", responsibility='" + responsibility + '\'' +
                ", startDate='" + startDate + '\'' +
                ", startDateMonth='" + startDateMonth + '\'' +
                ", endDate='" + endDate + '\'' +
                ", endDateMonth='" + endDateMonth + '\'' +
                ", companyId=" + companyId +
                ", workEmphasis='" + workEmphasis + '\'' +
                ", workEmphasisArr=" + workEmphasisArr +
                ", workEmphasisStr='" + workEmphasisStr + '\'' +
//                ", workPerformance='" + workPerformance + '\'' +
                ", customPositionId=" + customPositionId +
                ", customIndustryId=" + customIndustryId +
                ", addTime=" + addTime +
                ", updateTime=" + updateTime +
//                ", personList=" + personList +
                '}';
    }

    public static WorkExperience getIntance(long id) {
        WorkExperience result = new WorkExperience();
        result.setId(id);
        result.setUserId(222872L);
        result.setCompany("Boss直聘");
        result.setIndustryCode(520);
        result.setPosition(1000000);
        result.setPositionCatgroy("Java");
        result.setPositionName("Java");
        result.setOfficeYears(2);
        result.setIsPublic(1);
        result.setDepartment("Boss直聘server");
//        result.setResponsibility("举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n");
        result.setResponsibility("举例来说，某一个 Bean 使用 Kryo 序列化后，结果被放到 Redis 里做了缓存，如果某次上线增加/删除了这个 Bean 中的一个字段，则缓存中的数据进行反序列化时会报错；作为缓存功能的开发者，此时应该 catch 住异常，清除这条缓存，然后返回 “缓存未命中” 信息给上层调用者。\n");
        result.setStartDate("20150806");
        result.setStartDate("20170806");
        result.setWorkEmphasis("数据库#&#Redis#&#哈哈哈");
        result.setWorkPerformance("Kryo 是一个快速序列化/反序列化工具，其使用了字节码生成机制（底层依赖了 ASM 库），因此具有比较好的运行速度。\n" +
                "Kryo 序列化出来的结果，是其自定义的、独有的一种格式，不再是 JSON 或者其他现有的通用格式；而且，其序列化出来的结果是二进制的（即 byte[]；而 JSON 本质上是字符串 String）；二进制数据显然体积更小，序列化、反序列化时的速度也更快。\n" +
                "Kryo 一般只用来进行序列化（然后作为缓存，或者落地到存储设备之中）、反序列化，而不用于在多个系统、甚至多种语言间进行数据交换 —— 目前 kryo 也只有 java 实现。\n" +
                "像 Redis 这样的存储工具，是可以安全地存储二进制数据的，所以可以直接把 Kryo 序列化出来的数据存进去。\n" +
                "当然，如果你希望用 String 的形式存储、传输 Kryo 序列化之后的数据，也可以通过 Base64 等编码方式来实现。但这会降低程序的运行速度，一定程度上违背了使用 kryo 的初衷。\n" +
                "Kryo 在使用时，需要根据使用场景进行一定的设置；如果设置不当，会导致一些严重的错误。（这些问题的原因参见第 2 节）\n" +
                "附件中提供了我们部门封装的 KryoUtil ，其根据分布式 Web 应用的一般场景，进行了配置及封装；可以在自己的项目里安全地使用此工具类。");
        result.setCustomPositionId(10033L);
        result.setCustomIndustryId(10033L);
        result.setAddTime(new Date());
        result.setUpdateTime(new Date());
//
//        List<Person> personList = new ArrayList<>(2);
//        personList.add(Person.getKryoInstance(id + 1));
//        personList.add(Person.getKryoInstance(id + 11));
//        result.setPersonList(personList);

        return result;
    }

    public static WorkExperience getIntance(long userId, long id) {
        WorkExperience result = getIntance(id);
        result.setUserId(userId);
        return result;
    }
}
