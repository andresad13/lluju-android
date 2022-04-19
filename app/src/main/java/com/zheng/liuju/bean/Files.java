package com.zheng.liuju.bean;

public class Files {


    /**
     * data : {"fileName":"e45a4e50-b818-412d-938e-65f98b2548ac.png","path":"/2019/12/17/e45a4e50-b818-412d-938e-65f98b2548ac.png","url":"http://119.23.182.107:7001/img//2019/12/17/e45a4e50-b818-412d-938e-65f98b2548ac.png"}
     * message : success
     * retCode : 1
     */

    private DataBean data;
    private String msg;
    private int code;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setRetCode(int code) {
        this.code = code;
    }

    public static class DataBean {
        /**
         * fileName : e45a4e50-b818-412d-938e-65f98b2548ac.png
         * path : /2019/12/17/e45a4e50-b818-412d-938e-65f98b2548ac.png
         * url : http://119.23.182.107:7001/img//2019/12/17/e45a4e50-b818-412d-938e-65f98b2548ac.png
         */

        private String fileName;
        private String path;
        private String url;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
