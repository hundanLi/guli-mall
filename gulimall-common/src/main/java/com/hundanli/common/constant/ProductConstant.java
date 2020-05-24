package com.hundanli.common.constant;

/**
 * @author li
 * @version 1.0
 * @date 2020-05-17 15:43
 **/
public class ProductConstant {

    public enum AttrConstant{
        /**
         * 1：基本属性
         * 0：销售属性
         */
        BASE(1, "base"), SALE(0,"sale");
        private int code;
        private String msg;

        AttrConstant(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

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
    }
}
