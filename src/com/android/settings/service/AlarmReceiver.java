/*
 * Copyright (C) 2013 Android Open Kang Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    private final static String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (AutoSmsService.mWakeLock == null) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            AutoSmsService.mWakeLock = pm.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK, AutoSmsService.WAKE_TAG);
            if (!AutoSmsService.mWakeLock.isHeld()) {
                AutoSmsService.mWakeLock.acquire();
            }
        }
        final String action = intent.getAction();

        if (MessagingHelper.SMS_SERVICE_COMMAND.equals(action)) {
            context.startService(new Intent(context, AutoSmsService.class));
        } else {
            context.stopService(new Intent(context, AutoSmsService.class));
            MessagingHelper.scheduleService(context);
        }
    }

}
