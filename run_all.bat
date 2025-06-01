@echo off
setlocal enabledelayedexpansion

REM Enhanced Java Compilation and Execution Script
REM Includes comprehensive error handling and input validation

echo Java Project Compiler and Runner
echo ====================================

REM 1. Ask for the folder containing the .java files with validation
:GET_FOLDER
set "JAVA_FOLDER="
set /p JAVA_FOLDER="Enter the folder containing the .java files (default: hello-world):"
if "%JAVA_FOLDER%"=="" set "JAVA_FOLDER=hello-world"

REM Sanitize folder path - remove potential dangerous characters
set "JAVA_FOLDER=%JAVA_FOLDER:"=%"
set "JAVA_FOLDER=%JAVA_FOLDER:|=%"
set "JAVA_FOLDER=%JAVA_FOLDER:&=%"
set "JAVA_FOLDER=%JAVA_FOLDER:>=%"
set "JAVA_FOLDER=%JAVA_FOLDER:<=%"

if "%JAVA_FOLDER%"=="" (
    echo "Error: Folder name cannot be empty."
    goto GET_FOLDER
)

REM Check if folder exists and is accessible
if not exist "%JAVA_FOLDER%" (
    echo ====================================
    echo Error: Folder "%JAVA_FOLDER%" does not exist.
    set /p RETRY="Try again? (y/n)": 
    if /i "!RETRY!"=="y" goto GET_FOLDER
    echo Exiting...
    exit /b 1
)

REM Check if it's actually a directory
pushd "%JAVA_FOLDER%" >nul 2>&1
if errorlevel 1 (
    echo Error: "%JAVA_FOLDER%" is not a valid directory or access denied.
    set /p RETRY="Try again? (y/n)": 
    if /i "!RETRY!"=="y" (
        popd >nul 2>&1
        goto GET_FOLDER
    )
    echo Exiting...
    exit /b 1
)

REM 2. List .java files and ask which one to run with improved validation
set i=0
echo.
echo Scanning for Java files...
for %%f in (*.java) do (
    set /a i+=1
    set "file[!i!]=%%f"
    echo Found: %%f
)

if %i%==0 (
    echo Error: No .java files found in "%JAVA_FOLDER%"!
    popd
    pause
    exit /b 1
)

echo.
echo Available .java files:
echo ========================
for /l %%j in (1,1,%i%) do echo %%j. !file[%%j]!
echo.

:GET_CHOICE
set "choice="
set /p choice=Select file number to run (1-%i%): 

REM Validate choice is not empty
if "%choice%"=="" (
    echo Error: Please enter a number.
    goto GET_CHOICE
)

REM Validate choice is numeric
set "valid=0"
for /l %%k in (1,1,%i%) do (
    if "%choice%"=="%%k" set "valid=1"
)

if "%valid%"=="0" (
    echo Error: Please enter a valid number between 1 and %i%.
    goto GET_CHOICE
)

set "SRC_FILE=!file[%choice%]!"
set "CLASS_NAME=%SRC_FILE:.java=%"
echo Selected: %SRC_FILE%
popd

REM 3. Setup directory structure and compile
echo.
echo Setting up compilation environment...

REM Create necessary directories if they don't exist
set "CLASS_DIR=%JAVA_FOLDER%\class"
@REM set "LIB_DIR=%JAVA_FOLDER%\..\lib"
set "LIB_DIR=lib"
set "TEST_DIR=%JAVA_FOLDER%\test"

if not exist "%CLASS_DIR%" (
    echo Creating class directory: %CLASS_DIR%
    mkdir "%CLASS_DIR%" 2>nul
    if errorlevel 1 (
        echo Error: Failed to create class directory. Check permissions.
        pause
        exit /b 1
    )
)

REM Check if lib directory exists (not critical)
if not exist "%LIB_DIR%" (
    echo Warning: Library directory "%LIB_DIR%" not found. Compiling without external libraries.
    set "CLASSPATH_LIBS="
) else (
    set "CLASSPATH_LIBS=%LIB_DIR%\*"
)

REM Compile with detailed error reporting
echo Compiling %SRC_FILE%...
if "%CLASSPATH_LIBS%"=="" (
    javac -d "%CLASS_DIR%" "%JAVA_FOLDER%\%SRC_FILE%" 2>&1
) else (
    javac -d "%CLASS_DIR%" -cp "%CLASSPATH_LIBS%" "%JAVA_FOLDER%\%SRC_FILE%" 2>&1
)

if errorlevel 1 (
    echo.
    echo ====================================
    echo COMPILATION FAILED!
    echo ====================================
    echo Check the error messages above for details.
    echo Common issues:
    echo - Syntax errors in the Java code
    echo - Missing import statements
    echo - Classpath issues
    echo - Missing external libraries
    pause
    exit /b 1
)
echo Compilation successful^!

REM 4. Ask for arguments with better handling
echo.
echo Enter arguments to run with (press Enter for none):
echo -------------------------------------
set "ARGS="
set /p ARGS=""
echo ------------------------------------- 
if not "%ARGS%"=="" echo Arguments: "%ARGS%"


REM 5. Run the class file with improved error handling
echo.
echo ====================================
echo RUNNING PROGRAM
echo ====================================

@REM echo Class directory: %CLASS_DIR%
@REM pushd "%CLASS_DIR%"
@REM if errorlevel 1 (
@REM     echo Error: Cannot access class directory "%CLASS_DIR%"^!
@REM     exit /b 1
@REM )
@REM pause

echo Executing: java %CLASS_NAME% %ARGS%
echo.

if "%CLASSPATH_LIBS%"=="" (
    java -cp "%CLASS_DIR%" %CLASS_NAME% %ARGS%
) else (
    java -cp "%CLASS_DIR%;%CLASSPATH_LIBS%" %CLASS_NAME% %ARGS%
)

set "EXEC_RESULT=%errorlevel%"
echo.
if %EXEC_RESULT% neq 0 (
    echo ====================================
    echo PROGRAM EXECUTION FAILED ^(Exit code: %EXEC_RESULT%^)
    echo ====================================
) else (
    echo ====================================
    echo PROGRAM COMPLETED SUCCESSFULLY
    echo ====================================
)

REM 6. Enhanced test file handling
echo.
set "TEST_FILE=%TEST_DIR%\%CLASS_NAME%Test.java"
set "TEST_CLASS=%CLASS_NAME%Test"

if exist "%TEST_FILE%" (
    echo Test file found: %TEST_CLASS%.java
    set /p RUN_TEST="Do you want to run the test? (y/n):" 
    
    if /i "!RUN_TEST!"=="y" (
        echo.
        echo Compiling test file...
        
        REM Compile test file with both main class and lib in classpath
        if "%CLASSPATH_LIBS%"=="" (
            javac -d "%CLASS_DIR%" -cp "%CLASS_DIR%" "%TEST_FILE%" 2>&1
        ) else (
            javac -d "%CLASS_DIR%" -cp "%CLASS_DIR%;%CLASSPATH_LIBS%" "%TEST_FILE%" 2>&1
        )
        
        if errorlevel 1 (
            echo ====================================
            echo TEST COMPILATION FAILED!
            echo ====================================
            echo Check the error messages above.
        ) else (
            echo Test compilation successful!
            echo.
            echo ====================================
            echo RUNNING TESTS
            echo ====================================
            
            if "%CLASSPATH_LIBS%"=="" (
                java -cp "%CLASS_DIR%" org.junit.platform.console.ConsoleLauncher --select-class %TEST_CLASS%
            ) else (
                java -cp "%CLASS_DIR%;%CLASSPATH_LIBS%" org.junit.platform.console.ConsoleLauncher --select-class %TEST_CLASS%
            )
            
            set "TEST_RESULT=!errorlevel!"
            echo.
            if !TEST_RESULT! neq 0 (
                echo ====================================
                echo "TESTS FAILED (Exit code: !TEST_RESULT!)"
                echo ====================================
            ) else (
                echo ====================================
                echo ALL TESTS PASSED
                echo ====================================
            )
        )
    )
) else (
    echo No test file found at: %TEST_FILE%
)

@REM popd

REM 7. Final summary
echo.
echo ====================================
echo EXECUTION SUMMARY
echo ====================================
echo Source file: %JAVA_FOLDER%\%SRC_FILE%
echo Class name: %CLASS_NAME%
echo Arguments: %ARGS%
echo Main program exit code: %EXEC_RESULT%
if exist "%TEST_FILE%" (
    echo Test file: %TEST_FILE%
    if defined TEST_RESULT echo Test exit code: %TEST_RESULT%
)
echo ====================================

pause
echo Script completed.