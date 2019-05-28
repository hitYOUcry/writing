

/*
 * Original work Copyright (c) 2016, Lody
 * Modified work Copyright (c) 2016, Alibaba Mobile Infrastructure (Android) Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dark;

import android.util.Log;


public class Runtime {

    private static final String TAG = "Runtime";

    private volatile static Boolean isThumb = null;

    private volatile static boolean g64 = false;
    private volatile static boolean isArt = true;

    static {
        try {
            g64 = (boolean) Class.forName("dalvik.system.VMRuntime").getDeclaredMethod("is64Bit").invoke(Class.forName("dalvik.system.VMRuntime").getDeclaredMethod("getRuntime").invoke(null));
        } catch (Exception e) {
            Log.e(TAG, "get is64Bit failed, default not 64bit!", e);
            g64 = false;
        }
        isArt = System.getProperty("java.vm.version").startsWith("2");
        Log.i(TAG, "is64Bit: " + g64 + ", isArt: " + isArt);
    }

    public static boolean is64Bit() {
        return g64;
    }

}
