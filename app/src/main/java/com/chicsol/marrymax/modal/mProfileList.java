package com.chicsol.marrymax.modal;

import java.util.List;

public class mProfileList {


    public int page_no;
    public int count;
    public List<String> prfids;

    public int getPage_no() {
        return page_no;
    }

    public void setPage_no(int page_no) {
        this.page_no = page_no;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<String> getPrfids() {
        return prfids;
    }

    public void setPrfids(List<String> prfids) {
        this.prfids = prfids;
    }
}



