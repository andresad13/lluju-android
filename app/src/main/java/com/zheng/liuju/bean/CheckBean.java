package com.zheng.liuju.bean;

import java.util.List;

public class CheckBean {

    /**
     * message : success
     * retCode : 1
     * data : {"bankCardlist":[{"id":"fdf0dadcf6434580bdb9d09d778e5b5c","cardName":"FLAVIO TORRES GONZALEZ","cardNumberBin":"493158","cardNumberLast4":"6014","cardExpYear":"21","cardExpMonth":"11","cardBrand":"visa"}],"whetherToRecharge":false}
     */

    private String message;
    private int retCode;
    private DataBean data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * bankCardlist : [{"id":"fdf0dadcf6434580bdb9d09d778e5b5c","cardName":"FLAVIO TORRES GONZALEZ","cardNumberBin":"493158","cardNumberLast4":"6014","cardExpYear":"21","cardExpMonth":"11","cardBrand":"visa"}]
         * whetherToRecharge : false
         */

        private boolean whetherToRecharge;
        private List<BankCardlistBean> bankCardlist;

        public boolean isWhetherToRecharge() {
            return whetherToRecharge;
        }

        public void setWhetherToRecharge(boolean whetherToRecharge) {
            this.whetherToRecharge = whetherToRecharge;
        }

        public List<BankCardlistBean> getBankCardlist() {
            return bankCardlist;
        }

        public void setBankCardlist(List<BankCardlistBean> bankCardlist) {
            this.bankCardlist = bankCardlist;
        }

        public static class BankCardlistBean {
            /**
             * id : fdf0dadcf6434580bdb9d09d778e5b5c
             * cardName : FLAVIO TORRES GONZALEZ
             * cardNumberBin : 493158
             * cardNumberLast4 : 6014
             * cardExpYear : 21
             * cardExpMonth : 11
             * cardBrand : visa
             */

            private String id;
            private String cardName;
            private String cardNumberBin;
            private String cardNumberLast4;
            private String cardExpYear;
            private String cardExpMonth;
            private String cardBrand;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCardName() {
                return cardName;
            }

            public void setCardName(String cardName) {
                this.cardName = cardName;
            }

            public String getCardNumberBin() {
                return cardNumberBin;
            }

            public void setCardNumberBin(String cardNumberBin) {
                this.cardNumberBin = cardNumberBin;
            }

            public String getCardNumberLast4() {
                return cardNumberLast4;
            }

            public void setCardNumberLast4(String cardNumberLast4) {
                this.cardNumberLast4 = cardNumberLast4;
            }

            public String getCardExpYear() {
                return cardExpYear;
            }

            public void setCardExpYear(String cardExpYear) {
                this.cardExpYear = cardExpYear;
            }

            public String getCardExpMonth() {
                return cardExpMonth;
            }

            public void setCardExpMonth(String cardExpMonth) {
                this.cardExpMonth = cardExpMonth;
            }

            public String getCardBrand() {
                return cardBrand;
            }

            public void setCardBrand(String cardBrand) {
                this.cardBrand = cardBrand;
            }
        }
    }
}
