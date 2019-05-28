package com.tencent.strategy;

import android.util.Base64;
import android.util.Log;

import com.branch.v2.ChannelWrite;
import com.branch.v2.read.ChannelRead;
import com.branch.v2.read.model.ChannelModel;
import com.branch.v2.read.util.ApkUtils;
import com.branch.v2.read.zip.ZipFormatException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static com.tencent.strategy.StrategyTest.CarType.EXPRESS_CAR;
import static com.tencent.strategy.StrategyTest.CarType.FREE_RIDE_CAR;
import static com.tencent.strategy.StrategyTest.CarType.LUXURY_CAR;
import static com.tencent.strategy.StrategyTest.CarType.SHARING_CAR;
import static com.tencent.strategy.StrategyTest.CarType.SPECIAL_CAR;

/**
 * @author nemoqjzhang
 * @date 2018/7/23 21:50.
 */

public class StrategyTest {


    public StrategyTest() {
        Log.i("DexTest", "<init> " + getClass().getName());
    }

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
    private static float getPrice(float distance, CarType carType) {
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

    public static void main(String[] args) {
       /* float distance = 12.5f;
        float price = getPrice(distance,EXPRESS_CAR);
        System.out.println("ExpressCarPrice:" + price);

        price = getPrice(distance,SPECIAL_CAR);
        System.out.println("SpecialCarPrice:" + price);

        price = getPrice(distance,LUXURY_CAR);
        System.out.println("LuxuryCarPrice:" + price);

        price = getPrice(distance,FREE_RIDE_CAR);
        System.out.println("FreeRideCarPrice:" + price);

        price = getPrice(distance,SHARING_CAR);
        System.out.println("SharingCarPrice:" + price);*/
       /* try {
            ChannelWrite.writeChannel("C:\\Users\\nemoqjzhang\\Desktop\\writing\\strategy\\app\\app-debug.apk",
                    "C:\\Users\\nemoqjzhang\\Desktop\\writing\\strategy\\app\\with-channel",
                    "10086");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ZipFormatException e) {
            e.printStackTrace();
        } catch (ApkUtils.SignatureNotFoundException e) {
            e.printStackTrace();
        }*/
        //        ChannelModel channel = ChannelRead.getChannel("C:\\Users\\nemoqjzhang\\Desktop\\writing\\strategy\\app\\with-channelapp-debug-10086-2018-08-02.apk");
        //        System.out.println(channel);

        byte[] a = {1, 2, 124, 4, 5, 6, 7};
        System.out.println("before:" + Arrays.toString(a));
        String str = Base64.encodeToString(null, Base64.NO_WRAP);
        System.out.println("str:" + str);

        System.out.println("after:" + Arrays.toString(Base64.decode(str, Base64.NO_WRAP)));
    }

}
