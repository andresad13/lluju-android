package com.zheng.liuju.bean;

public class EventMeager {

    public  boolean upData;
    public  String push;
    public  boolean  upUser;
    public   String  finishs;
    public  String  lanXinPay;
    public  String   coupon;
    public  boolean rentalInformation;

    public  boolean home;

    public boolean isHome() {
        return home;
    }

    public void setHome(boolean home) {
        this.home = home;
    }



    public boolean isAddCardSuccess() {
        return addCardSuccess;
    }

    public void setAddCardSuccess(boolean addCardSuccess) {
        this.addCardSuccess = addCardSuccess;
    }

    private boolean addCardSuccess;


    public boolean isUpRental() {
        return upRental;
    }

    public void setUpRental(boolean upRental) {
        this.upRental = upRental;
    }

    public   boolean  upRental;

    public int isSwitchPages() {
        return switchPages;
    }

    public void setSwitchPages(int switchPages) {
        this.switchPages = switchPages;
    }

    public  int switchPages;

    public boolean isRechargeSuccessful() {
        return rechargeSuccessful;
    }

    public void setRechargeSuccessful(boolean rechargeSuccessful) {
        this.rechargeSuccessful = rechargeSuccessful;
    }

    public  boolean  rechargeSuccessful;

    public boolean isRentalSuccess() {
        return rentalSuccess;
    }

    public void setRentalSuccess(boolean rentalSuccess) {
        this.rentalSuccess = rentalSuccess;
    }

    public  boolean  rentalSuccess;

    public boolean isRentalInformation() {
        return rentalInformation;
    }

    public void setRentalInformation(boolean rentalInformation) {
        this.rentalInformation = rentalInformation;
    }
    public String getPush() {
        return push;
    }
    public boolean getUpUser() {
        return upUser;
    }
    public void setUpUser(boolean upUser) {
        this.upUser = upUser;
    }
    public void setFinishs(String finishs) {
        this.finishs = finishs;
    }
    public String getFinishs() {
        return finishs;
    }
    public  void  setupDataCard(boolean upData){
        this.upData =upData;
    }
    public  boolean  getupDataCard(){
        return  upData;
    }



}
