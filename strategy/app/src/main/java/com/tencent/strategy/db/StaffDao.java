package com.tencent.strategy.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import static com.tencent.strategy.db.MainDataBase.TABLE_STAFF;

/**
 * @author nemoqjzhang
 * @date 2018/8/21 10:16.
 */
@Dao
public interface StaffDao {

    @Query("SELECT * FROM " + TABLE_STAFF)
    List<Staff> getAll();

    @Query("SELECT * FROM " + TABLE_STAFF + " WHERE id=:id LIMIT 1")
    Staff getStuff(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(Staff staff);

    @Delete
    int delete(Staff staff);
}
