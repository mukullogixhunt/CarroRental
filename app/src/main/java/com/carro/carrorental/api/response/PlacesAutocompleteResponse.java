package com.carro.carrorental.api.response;

import com.carro.carrorental.model.PredictionModel;

import java.util.List;

public class PlacesAutocompleteResponse {

    private List<PredictionModel> predictions;

    public List<PredictionModel> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<PredictionModel> predictions) {
        this.predictions = predictions;
    }


}

