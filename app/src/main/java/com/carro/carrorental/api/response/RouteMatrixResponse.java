package com.carro.carrorental.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.carro.carrorental.model.DistanceModel;

import java.util.List;

public class RouteMatrixResponse {

    @SerializedName("rows")
    @Expose
    private List<DistanceModel> rows;

    public List<DistanceModel> getRows() {
        return rows;
    }

    public void setRows(List<DistanceModel> rows) {
        this.rows = rows;
    }
}
