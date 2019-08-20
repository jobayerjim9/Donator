package com.teamjhj.donator_247blood;

import java.util.ArrayList;

public class AppData {

    static ArrayList<String> donners;
    static ArrayList<Integer> radiusList;
    static String mobileNumber;
    static NonEmergencyInfo nonEmergencyInfo;
    static UserProfile userProfile;
    static String postMessage;
    static ArrayList<NonEmergencyInfo> nonEmergencyInfos;
    static NonEmergencyInfo editPostInfo;

    public AppData() {
    }

    public static NonEmergencyInfo getEditPostInfo() {
        return editPostInfo;
    }

    public static void setEditPostInfo(NonEmergencyInfo editPostInfo) {
        AppData.editPostInfo = editPostInfo;
    }

    public static String getPostMessage() {
        return postMessage;
    }

    public static void setPostMessage(String postMessage) {
        AppData.postMessage = postMessage;
    }

    public static NonEmergencyInfo getNonEmergencyInfo() {
        return nonEmergencyInfo;
    }

    public static void setNonEmergencyInfo(NonEmergencyInfo nonEmergencyInfo) {
        AppData.nonEmergencyInfo = nonEmergencyInfo;
    }

    public static UserProfile getUserProfile() {
        return userProfile;
    }

    public static void setUserProfile(UserProfile userProfile) {
        AppData.userProfile = userProfile;
    }

    static String getMobileNumber() {
        return mobileNumber;
    }

    static void setMobileNumber(String mobileNumber) {
        AppData.mobileNumber = mobileNumber;
    }

    static ArrayList<String> getDonners() {
        return donners;
    }

    static void setDonners(ArrayList<String> donners) {
        AppData.donners = donners;
    }

    public static ArrayList<Integer> getRadiusList() {
        return radiusList;
    }

    public static void setRadiusList(ArrayList<Integer> radiusList) {
        AppData.radiusList = radiusList;
    }

    public static ArrayList<NonEmergencyInfo> getNonEmergencyInfos() {
        return nonEmergencyInfos;
    }

    public static void setNonEmergencyInfos(ArrayList<NonEmergencyInfo> nonEmergencyInfos) {
        AppData.nonEmergencyInfos = nonEmergencyInfos;
    }
}
