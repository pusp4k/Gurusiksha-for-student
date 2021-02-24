package com.androweb.voyage.utils;

public class DataModel {

    String mStartDate;
    String mStartMnt;
    String mEndDate;


    String mEndMnt;
    String mEventTime;
    String mAmPm;
    String mSrcName;
    String mDestName;
    String mStatus;

    public DataModel(String startDate, String startMnt, String endDate, String endMnt, String eventTime, String amPm, String srcName, String destName, String status) {
        this.mStartDate = startDate;
        this.mStartMnt = startMnt;
        this.mEndDate = endDate;
        this.mEndMnt = endMnt;
        this.mEventTime = eventTime;
        this.mAmPm = amPm;
        this.mSrcName = srcName;
        this.mDestName = destName;
        this.mStatus = status;
    }

    public DataModel(String startDate, String startMnt, String srcName, String destName) {
        this.mStartDate = startDate;
        this.mStartMnt = startMnt;
        this.mSrcName = srcName;
        this.mDestName = destName;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public String getStartMnt() {
        return mStartMnt;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public String getEndMnt() {
        return mEndMnt;
    }

    public String getEventTime() {
        return mEventTime;
    }

    public String getAmPm() {
        return mAmPm;
    }

    public String getSrcName() {
        return mSrcName;
    }

    public String getDestName() {
        return mDestName;
    }

    public String getStatus() {
        return mStatus;
    }

}
