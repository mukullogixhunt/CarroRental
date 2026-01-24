package com.carro.carrorental.utils;

import java.text.DecimalFormat;

public class IndianCurrencyFormat {

    public String inCuFormatText(String number){
        try{
            double num = Double.parseDouble(number);
            DecimalFormat dmf = new DecimalFormat("##,##,###.##");
            number = dmf.format(num);
        }catch (Exception e){
            number = "0";
        }
        return "\u20B9 "+number;
    }


}
