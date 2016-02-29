// Copyright 2012 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.content_shell_apk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import org.chromium.base.CommandLine;
import org.chromium.base.PathUtils;
import org.chromium.base.annotations.SuppressFBWarnings;
import org.chromium.content.app.ContentApplication;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Entry point for the content shell application.  Handles initialization of information that needs
 * to be shared across the main activity and the child services created.
 */
public class ContentShellApplication extends ContentApplication {

    private static final String TAG = "ContentShellApplication";

    public static final String COMMAND_LINE_FILE = "/data/local/tmp/content-shell-command-line";
    private static final String PRIVATE_DATA_DIRECTORY_SUFFIX = "content_shell";

    public static Context context;
    public static WorkspotService workspotService = null;
    private static AtomicBoolean IS_APPLICATION_RUNNING_AS_ISOLATED_PROCESS = new AtomicBoolean(
            false);

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        boolean runningAsFullApplication = hasPermission("android.permission.ACCESS_NETWORK_STATE");
        if(runningAsFullApplication)
        {
            Log.d(TAG, "Running as full process");
        }
        else
        {
            Log.d(TAG, "Running as isolated process");
            IS_APPLICATION_RUNNING_AS_ISOLATED_PROCESS.set(true);
        }

        if (IS_APPLICATION_RUNNING_AS_ISOLATED_PROCESS.get() == false) {
            // start the Workspot service and initiate its async binding
            Intent workspotServiceIntent = new Intent(context,
                    WorkspotService.class);
            startService(workspotServiceIntent);
//            bindService(workspotServiceIntent, workspotServiceConnection,
//                    BIND_AUTO_CREATE);
        }
    }

    private boolean hasPermission(final String permission)
    {
        final int result = checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void initializeLibraryDependencies() {
        PathUtils.setPrivateDataDirectorySuffix(PRIVATE_DATA_DIRECTORY_SUFFIX, this);
    }

    @SuppressFBWarnings("DMI_HARDCODED_ABSOLUTE_FILENAME")
    @Override
    public void initCommandLine() {
        if (!CommandLine.isInitialized()) {
            CommandLine.initFromFile(COMMAND_LINE_FILE);
        }
    }

    public void cleanup() {
//        Log.e(TAG, "cleanup called " + Process.myPid());
//        if (workspotServiceConnection != null) {
//            Log.e(TAG, "cleanup inside if " + Process.myPid());
//            unbindService(workspotServiceConnection);
//        }
        Intent workspotServiceIntent = new Intent(this,
                WorkspotService.class);
        stopService(workspotServiceIntent);
        workspotService = null;
    }

}
