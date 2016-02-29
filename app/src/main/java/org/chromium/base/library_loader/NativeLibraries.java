



package org.chromium.base.library_loader;

import org.chromium.base.annotations.SuppressFBWarnings;

@SuppressFBWarnings
public class NativeLibraries {
    public static boolean sUseLinker = false;
    public static boolean sUseLibraryInZipFile = false;
    public static boolean sEnableLinkerTests = false;
    public static final String[] LIBRARIES =
      {"content_shell_content_view","osmesa"};
    static String sVersionNumber =
      "";
}
