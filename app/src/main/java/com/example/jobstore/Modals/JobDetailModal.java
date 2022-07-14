package com.example.jobstore.Modals;

public class JobDetailModal {
    private String companyName,packageAmount,jobType,endDate,jobLink, postedBy, jobId;
    private Integer upVote,downVote;

    public JobDetailModal() {
    }

    public JobDetailModal(String companyName, String packageAmount,
                          String jobType, String endDate, String jobLink, Integer upVote, Integer downVote, String postedBy) {
        this.companyName = companyName;
        this.packageAmount = packageAmount;
        this.jobType = jobType;
        this.endDate = endDate;
        this.jobLink = jobLink;
        this.postedBy = postedBy;
        this.upVote = upVote;
        this.downVote = downVote;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }



    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPackageAmount() {
        return packageAmount;
    }

    public void setPackageAmount(String packageAmount) {
        this.packageAmount = packageAmount;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getJobLink() {
        return jobLink;
    }

    public void setJobLink(String jobLink) {
        this.jobLink = jobLink;
    }

    public Integer getUpVote() {
        return upVote;
    }

    public void setUpVote(Integer upVote) {
        this.upVote = upVote;
    }

    public Integer getDownVote() {
        return downVote;
    }

    public void setDownVote(Integer downVote) {
        this.downVote = downVote;
    }
}
