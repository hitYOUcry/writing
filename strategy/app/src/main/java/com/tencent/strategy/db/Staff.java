package com.tencent.strategy.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * @author nemoqjzhang
 * @date 2018/8/21 10:09.
 */
@Entity(tableName = MainDataBase.TABLE_STAFF)
public class Staff {

    @PrimaryKey
    public long id = System.currentTimeMillis();

    public String name;

    public int price = -1;

    public int gender;

    public int age;

    @Override
    public String toString() {
        return "Name:" + name + ",Price:" + price + ",Gender:" + gender + ",Age :" + age;
    }
}
