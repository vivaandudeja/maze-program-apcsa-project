MazeProgram - Build Instructions
=================================

Two ways to build this project, depending on what you need:

  build-installer.bat                     -> Local build (for testing on your own PC)
  .github/workflows/release.yml           -> Cloud build (for the public x64 download)

------------------------------------------------------------
CLOUD BUILD (for the public download on the website)
------------------------------------------------------------

The GitHub Pages site downloads the .exe from GitHub Releases.
Those release files are built automatically in the cloud whenever
you push a version tag - no need to run anything locally.

To publish a new public release:

  git tag v1.0.0
  git push origin v1.0.0

That triggers .github/workflows/release.yml on a free x64 Windows
runner. It compiles, jlinks, jpackages, and uploads:
  - MazeProgram-1.0.0.exe       (installer)
  - MazeProgram-portable.zip    (portable folder)
straight into the matching GitHub Release.

Watch the build at:
  https://github.com/vivaandudeja/maze-program-apcsa-project/actions

Why cloud? jpackage produces builds for whatever CPU it runs on,
so building locally on an ARM PC would produce an ARM-only .exe
that wouldn't work on most users' machines. The cloud runner is
x64 and works for everyone.

------------------------------------------------------------
LOCAL BUILD (for testing on your own PC)
------------------------------------------------------------

  1. Double-click build-installer.bat (or run from terminal)
  2. Wait 1-2 minutes
  3. Output: dist\MazeProgram-1.0.0.exe (a single installer file)
  4. Double-click to install on your machine

This is useful for quickly testing changes before pushing a tag.
The locally-built .exe will be for YOUR architecture only - don't
share it. For public distribution, use the cloud build above.

------------------------------------------------------------
ONE-TIME WIX TOOLSET SETUP (local builds only)
------------------------------------------------------------

WiX is the tool jpackage uses behind the scenes to build the
.exe installer. The cloud runner has it pre-installed, but for
local builds you need to install it once on your machine.

(Recipients of your installer do NOT need WiX.)

Steps:

  1. Go to: https://github.com/wixtoolset/wix3/releases
  2. Download wix311.exe (use WiX 3.x, NOT 4.x - jpackage does
     not support WiX 4 yet).
  3. Run wix311.exe and follow the installer.
  4. Add the WiX bin folder to your PATH:
       a. Press Windows key, type "environment variables", open
          "Edit the system environment variables"
       b. Click "Environment Variables..."
       c. Under "User variables", select "Path" and click "Edit..."
       d. Click "New" and add:
            C:\Program Files (x86)\WiX Toolset v3.11\bin
       e. Click OK on all dialogs
  5. Open a NEW terminal and verify:
       light --version
     You should see a version number.

------------------------------------------------------------
REQUIREMENTS (local builds only)
------------------------------------------------------------

  - JDK 14 or newer installed (you have JDK 25 - good)
  - jpackage on PATH (ships with the JDK)
  - WiX Toolset 3.x on PATH (see above)
  - MazeProgram.jar in this folder
  - myRuntime\ folder in this folder

To verify: open a new terminal and run
  jpackage --version
  light --version

------------------------------------------------------------
REBUILDING THE JAR (only if you change .java files)
------------------------------------------------------------

If you edit any .java file, recompile and re-jar before running
build-installer.bat:

  javac *.java
  jar cfe MazeProgram.jar MazeRunner *.class

(The cloud workflow does this automatically - only needed for
local builds.)

------------------------------------------------------------
TROUBLESHOOTING
------------------------------------------------------------

"jpackage not found"
    -> Install a JDK 14+ and add its bin folder to PATH.
       Open a new terminal after updating PATH.

"WiX toolset not found"
    -> See the WiX setup section above. Make sure you opened
       a new terminal after editing PATH.

"WiX version mismatch" or weird WiX errors
    -> jpackage requires WiX 3.x. WiX 4.x is NOT supported.
       Uninstall WiX 4 and install wix311.exe.

"Runtime image is not valid"
    -> Your myRuntime folder may be incomplete. Regenerate it:
       jlink --module-path "%JAVA_HOME%\jmods" ^
             --add-modules java.base,java.datatransfer,java.xml,java.prefs,java.desktop ^
             --output myRuntime

Recipient gets "Windows protected your PC" warning
    -> Normal for unsigned .exe files. Click "More info" then
       "Run anyway". Removing the warning requires a code-signing
       certificate (~$100/year), not worth it for a school project.

Recipient gets "This app can't run on your PC"
    -> Architecture mismatch. They're on a different CPU than
       what you built for. Push a tag to trigger the cloud build,
       which produces an x64 .exe that works on most PCs.
