package com.pandacard.teavel.https.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class pandaInfo  implements Parcelable {

    /**
     * msg :
     * code : 0
     * extra : {"shop":"","trip":"","welcome":"","bananer":""}
     */

    private String msg;
    private int code;
    private ExtraBean extra;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static class ExtraBean implements Parcelable{
        /**
         * shop :
         * trip :
         * welcome :
         * bananer :
         */

        private String shop;
        private String trip;
        private String welcome;
        private String bananer;

        public String getShop() {
            return shop;
        }

        public void setShop(String shop) {
            this.shop = shop;
        }

        public String getTrip() {
            return trip;
        }

        public void setTrip(String trip) {
            this.trip = trip;
        }

        public String getWelcome() {
            return welcome;
        }

        public void setWelcome(String welcome) {
            this.welcome = welcome;
        }

        public String getBananer() {
            return bananer;
        }

        public void setBananer(String bananer) {
            this.bananer = bananer;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }
    }
}
