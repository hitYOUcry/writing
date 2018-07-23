package com.tencent.strategy;

/**
 * @author nemoqjzhang
 * @date 2018/7/23 21:50.
 */

public class StrategyTest {


    /**
     * 车类型
     */
    public enum CarType {
        EXPRESS_CAR,
        SPECIAL_CAR,
        LUXURY_CAR,
        FREE_RIDE_CAR,
        SHARING_CAR
    }

    /**
     * @param distance km
     * @return
     */
    private float calPrice(float distance, CarType carType) {
        float price = 0.0f;
        switch (carType) {
            case LUXURY_CAR:
                price = 2 * distance;
                break;
            case EXPRESS_CAR:
                price = 1 * distance;
                break;
            case SHARING_CAR:
                price = 0.75f * distance;
                break;
            case SPECIAL_CAR:
                price = 1.8f * distance;
                break;
            case FREE_RIDE_CAR:
                price = 0.9f * distance;
                break;
            default:
                price = distance;
                break;
        }
        return price;
    }

}
