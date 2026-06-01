MazeProgram - Build Instructions
=================================

This folder contains two build scripts:

  build.bat            -> Portable folder (no install, no WiX needed)
  build-installer.bat  -> Real Windows .exe installer (needs WiX)

Pick whichever fits how you want to share the app.

------------------------------------------------------------
OPTION 1: PORTABLE FOLDER (recommended for quick sharing)
------------------------------------------------------------

  1. Double-click build.bat
  2. Wait ~30 seconds
  3. Output: dist\MazeProgram\MazeProgram.exe
  4. To share: zip the dist\MazeProgram folder and send it.
     Recipient unzips and runs MazeProgram.exe - no install.

Pros: no WiX needed, no install on recipient's machine,
      runs from anywhere (USB stick, Desktop, Downloads).
Cons: bigger to send (~40 MB zipped), no Start menu entry.

------------------------------------------------------------
OPTION 2: REAL .EXE INSTALLER (for a polished release)
------------------------------------------------------------

  1. Install WiX Toolset (one-time setup - see below)
  2. Double-click build-installer.bat
  3. Wait 1-2 minutes
  4. Output: dist\MazeProgram-1.0.0.exe (a single installer file)
  5. To share: send that one .exe file.
     Recipient double-clicks it, walks through Setup wizard,
     gets a Start menu shortcut and an entry in Add/Remove Programs.

Pros: single file to share, proper Windows install experience,
      Start menu shortcut, clean uninstall via Settings > Apps.
Cons: requires installing WiX once on YOUR machine,
      recipient has to "install" before running.

------------------------------------------------------------
ONE-TIME WIX TOOLSET SETUP (for build-installer.bat only)
------------------------------------------------------------

WiX is the open-source tool that jpackage uses behind the scenes
to actually build .exe / .msi installer files. You only need to
install it on YOUR machine - the recipients of your installer
do NOT need WiX.

Steps:

  1. Go to: https://github.com/wixtoolset/wix3/releases
  2. Download the latest WiX 3.x installer (NOT 4.x - jpackage
     does not support WiX 4 yet). The file is named like:
       wix311.exe
  3. Run wix311.exe and follow the installer.
  4. Add the WiX bin folder to your PATH:
       a. Press Windows key, type "environment variables", open
          "Edit the system environment variables"
       b. Click "Environment Variables..."
       c. Under "User variables", select "Path" and click "Edit..."
       d. Click "New" and add:
            C:\Program Files (x86)\WiX Toolset v3.11\bin
       e. Click OK on all dialogs
  5. Open a NEW terminal (existing terminals won't see the
     updated PATH) and verify:
       light --version
     You should see a version number. If "not recognized", the
     PATH update didn't take effect - try logging out and back in.

Once that's done, build-installer.bat will work.

------------------------------------------------------------
WHAT EACH BUILD PRODUCES
------------------------------------------------------------

build.bat output:
  dist\
  └── MazeProgram\
      ├── MazeProgram.exe       <- launcher, double-click to run
      ├── app\
      │   └── MazeProgram.jar
      └── runtime\              <- bundled JRE

build-installer.bat output:
  dist\
  └── MazeProgram-1.0.0.exe     <- single installer file
                                   (run once to install the app
                                    into Program Files [per-user])

Both share the same JAR and runtime - the only difference is
how they're packaged for delivery.

------------------------------------------------------------
REQUIREMENTS (both scripts)
------------------------------------------------------------

  - JDK 14 or newer installed (you have JDK 25 - good)
  - jpackage on PATH (ships with the JDK)
  - MazeProgram.jar in this folder
  - myRuntime\ folder in this folder

To verify: open a new terminal and run
  jpackage --version

------------------------------------------------------------
REBUILDING THE JAR (only if you change .java files)
------------------------------------------------------------

If you edit any .java file, recompile and re-jar before running
either build script:

  javac *.java
  jar cfe MazeProgram.jar MazeRunner *.class

------------------------------------------------------------
TROUBLESHOOTING
------------------------------------------------------------

"jpackage not found"
    -> Install a JDK 14+ and add its bin folder to PATH.
       Open a new terminal after updating PATH.

"WiX toolset not found" (only for build-installer.bat)
    -> See the WiX setup section above. Make sure you opened
       a new terminal after editing PATH.

"WiX version mismatch" or weird WiX errors
    -> jpackage requires WiX 3.x. WiX 4.x is NOT supported.
       Uninstall WiX 4 and install wix311.exe.

"App-image fails: runtime image is not valid"
    -> Your myRuntime folder may be incomplete. Regenerate it:
       jlink --module-path "%JAVA_HOME%\jmods" ^
             --add-modules java.base,java.datatransfer,java.xml,java.prefs,java.desktop ^
             --output myRuntime

Installer install fails with permissions error
    -> The script uses --win-per-user-install (no admin needed).
       If you removed that flag, the installer will need admin
       rights to write to Program Files.

Recipient gets "Windows protected your PC" warning when running
    -> Unsigned .exe files always trigger this. Click "More info"
       then "Run anyway". To remove the warning entirely you'd
       need a code-signing certificate (~$100/year), not worth
       it for a school project.
