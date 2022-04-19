package com.zheng.liuju.bean;

public class ServiceBean {

    /**
     * code : 1
     * msg : About us
     * data : {"tel":"13813939898","weChat":"hhlaohu1","webSite":"http://www.zhengdejishu.com/","logoUrl":"https://cdb.lluju.com/img2019/08/02/20190802095505.png","businessHours":"9:00--23:00"}
     * total : 1
     */

    private int code;
    private String msg;
    private DataBean data;
    private int total;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static class DataBean {
        /**
         * tel : 13813939898
         * weChat : hhlaohu1
         * webSite : http://www.zhengdejishu.com/
         * logoUrl : https://cdb.lluju.com/img2019/08/02/20190802095505.png
         * businessHours : 9:00--23:00
         */

        private String tel;
        private String weChat;
        private String webSite;
        private String logoUrl;
        private String businessHours;
        private String customerServiceEmail;

        public String getCustomerServiceEmail() {
            return customerServiceEmail;
        }

        public void setCustomerServiceEmail(String customerServiceEmail) {
            this.customerServiceEmail = customerServiceEmail;
        }



        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getWeChat() {
            return weChat;
        }

        public void setWeChat(String weChat) {
            this.weChat = weChat;
        }

        public String getWebSite() {
            return webSite;
        }

        public void setWebSite(String webSite) {
            this.webSite = webSite;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public String getBusinessHours() {
            return businessHours;
        }

        public void setBusinessHours(String businessHours) {
            this.businessHours = businessHours;
        }
    }
}
