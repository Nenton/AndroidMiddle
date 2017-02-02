package com.nenton.androidmiddle.data.network.req;

import java.util.List;

/**
 * Created by serge on 01.02.2017.
 */

public class ProductEditReq {
    private boolean active;
    private List<?> comments = null;

    public ProductEditReq(boolean active) {
        this.active = active;
    }
}
