package com.zheng.liuju.bean;

import java.util.List;

public class CommonBean {


    /**
     * code : 1
     * msg : success
     * data : [{"id":58,"classId":1,"content":"信小程序體現方法：進入@@@微信小程序，點擊您自己的頭像進入個人中心，點擊【我的錢包】，選擇右下角的【提現】按鈕，在頁面中輸入提現金額，點擊體現按鈕即可提現。如果您是使用微信零錢充值，提現後，押金秒到您的微信個人賬戶，如果是使用微信調用銀行卡充值，押金將原路退回您的銀行卡，到銀行卡的時間為1至3個工作日。","title":"押金怎麼提現"},{"id":59,"classId":1,"content":"您好，如果您歸還充電寶的時候，有出現將充電寶反過來插進機櫃，機櫃會識別不了充電寶，請您重新將充電寶按照指示插入機櫃，即可正常歸還。如果歸還時候，機櫃沒有播報歸還成功的語音，請不要著急，在我們的小程序首頁右下角有客服按鈕，點擊即可與在線客服溝通處理。","title":"充電寶插反了"},{"id":60,"classId":1,"content":"您好，如果您掃碼付費後，沒有彈出充電寶，請進入個人中心查看當前訂單的狀態，如果訂單狀態顯示【已取消】或者【已完成】可等待3-5分鐘後重新掃碼借用，如顯示【租借中】又未彈出充電寶，請在我們的小程序首頁右下角有客服按鈕，點擊即可與在線客服溝通處理。","title":"掃碼後未彈出充電寶"},{"id":61,"classId":1,"content":"您好，如果您借出的充電寶無法正常充電或是電源線損壞，建議您盡快歸還至機櫃，歸還後請在我們的小程序首頁右下角有客服按鈕，點擊即可與在線客服溝通處理。","title":"借出充電寶無法充電"},{"id":62,"classId":2,"content":"如果您在租借或歸還中機櫃等跳躍閃動顯示無網絡鏈接，請您重開啟機櫃的電源，等待二維碼燈常亮後進行掃描租借或歸還。如果您歸還時，所在店舖網點的機櫃沒有可歸還的充電寶，您可在我們小程序首頁查看附件哪有離您最近的店舖網點進行歸還。","title":"機櫃顯示無網絡怎麼辦"},{"id":63,"classId":2,"content":"答：您好，訂單一直在租借中的原因有可能是網絡延遲造成的，請確認充電寶是否已經歸還，若已經歸還，還是現實在租借中，系統過一段時間會自動識別充電寶是否已在機櫃，自動會結束訂單。","title":"訂單為什麼一直在租借中"},{"id":65,"classId":4,"content":"您好，如果您確定您所租借的充電寶已遺失，請進入個人中心，點擊【租借訂單】找到您當次租借的充電寶訂單，點開訂單明細詳情，在詳情頁面，點擊【充電寶已遺失】按鈕，系統會立即扣除您當次所繳納的押金金額，同時結束訂單。該操作無法恢復，請謹慎操作。","title":"充電寶丟了怎麼辦"},{"id":64,"classId":4,"content":"您好，感謝您對我們的支持，如您需要申請設備合作，請與我們在線客服取得聯繫，謝謝！","title":"合作申請"}]
     * total : 8
     * page : 1
     * maxpage : 1
     */

    private int code;
    private String msg;
    private int total;
    private int page;
    private int maxpage;
    private List<DataBean> data;

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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getMaxpage() {
        return maxpage;
    }

    public void setMaxpage(int maxpage) {
        this.maxpage = maxpage;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 58
         * classId : 1
         * content : 信小程序體現方法：進入@@@微信小程序，點擊您自己的頭像進入個人中心，點擊【我的錢包】，選擇右下角的【提現】按鈕，在頁面中輸入提現金額，點擊體現按鈕即可提現。如果您是使用微信零錢充值，提現後，押金秒到您的微信個人賬戶，如果是使用微信調用銀行卡充值，押金將原路退回您的銀行卡，到銀行卡的時間為1至3個工作日。
         * title : 押金怎麼提現
         */

        private int id;
        private int classId;
        private String content;
        private String title;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getClassId() {
            return classId;
        }

        public void setClassId(int classId) {
            this.classId = classId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
