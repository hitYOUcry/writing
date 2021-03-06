package com.tencent.strategy;

import android.util.Log;

/**
 * @author nemoqjzhang
 * @date 2018/7/23 21:50.
 */

public class StrategyTestNew {


    public StrategyTestNew(){
        Log.i("DexTest","<init> " + getClass().getName());
    }

    /**
     * 定义一个价格计算接口
     */
    public interface IPriceCalculator {
        float getPrice(float distance);
    }

    /**
     * 快车价格计算
     */
    static class ExpressCarPriceCalculator implements IPriceCalculator {

        @Override
        public float getPrice(float distance) {
            return 1 * distance;
        }
    }

    /**
     * 专车价格计算
     */
    static class SpecialCarPriceCalculator implements IPriceCalculator {

        @Override
        public float getPrice(float distance) {
            return 1.8f * distance;
        }
    }

    /**
     * 豪华车车价格计算
     */
    static class LuxuryCarPriceCalculator implements IPriceCalculator {

        @Override
        public float getPrice(float distance) {
            return 2 * distance;
        }
    }

    /**
     * 顺风车价格计算
     */
    static class FreeRideCarPriceCalculator implements IPriceCalculator {

        @Override
        public float getPrice(float distance) {
            return 0.9f * distance;
        }
    }

    /**
     * 拼车价格计算
     */
    static class SharingCarPriceCalculator implements IPriceCalculator {

        @Override
        public float getPrice(float distance) {
            return 0.75f * distance;
        }
    }

    /**
     * 整合各类车的价格计算
     */
    static class CarPriceCalculatorManager{
        private IPriceCalculator calculator;

        public void setCalculator(IPriceCalculator calculator) {
            this.calculator = calculator;
        }

        public float getPrice(float distance){
            return calculator.getPrice(distance);
        }
    }


    public static void main(String[] args) {
        CarPriceCalculatorManager priceManager = new CarPriceCalculatorManager();
        float distance = 12.5f;
        priceManager.setCalculator(new ExpressCarPriceCalculator());
        System.out.println("ExpressCarPrice:" + priceManager.getPrice(distance));

        priceManager.setCalculator(new SpecialCarPriceCalculator());
        System.out.println("SpecialCarPrice:" + priceManager.getPrice(distance));

        priceManager.setCalculator(new LuxuryCarPriceCalculator());
        System.out.println("LuxuryCarPrice:" + priceManager.getPrice(distance));

        priceManager.setCalculator(new FreeRideCarPriceCalculator());
        System.out.println("FreeRideCarPrice:" + priceManager.getPrice(distance));

        priceManager.setCalculator(new SharingCarPriceCalculator());
        System.out.println("SharingCarPrice:" + priceManager.getPrice(distance));
    }

}
