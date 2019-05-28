package com.tencent.strategy.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

/**
 * @author nemoqjzhang
 * @date 2018/8/21 10:13.
 */
@Database(entities = {Staff.class}, version = 3)
public abstract class MainDataBase extends RoomDatabase {
    public static final String TABLE_STAFF = "table_staff";

    public abstract StaffDao staffDao();

    public static final Migration MIGRATION_1_2 =  new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE table_staff "
                    + " ADD COLUMN gender INTEGER NOT NULL DEFAULT -1");
        }
    };

    public static final Migration MIGRATION_2_3 =  new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE table_staff "
                    + " ADD COLUMN age INTEGER NOT NULL DEFAULT -1");
        }
    };
}
