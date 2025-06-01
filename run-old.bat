@echo off
setlocal enabledelayedexpansion

REM 1. Ask for the folder containing the .java files
set "JAVA_FOLDER="
set /p JAVA_FOLDER=Enter the folder containing the .java files (default: hello-world): 
if "%JAVA_FOLDER%"=="" set "JAVA_FOLDER=hello-world"

REM 2. List .java files and ask which one to run
pushd "%JAVA_FOLDER%" || (echo Folder not found! & exit /b 1)
set i=0
for %%f in (*.java) do (
    set /a i+=1
    set "file[!i!]=%%f"
)
if %i%==0 (
    echo No .java files found!
    popd
    exit /b 1
)
echo Available .java files:
for /l %%j in (1,1,%i%) do echo %%j. !file[%%j]!
set /p choice=Select file number to run: 
if "%choice%"=="" (
    echo Invalid selection.
    popd
    exit /b 1
)
set "SRC_FILE=!file[%choice%]!"
if "%SRC_FILE%"=="" (
    echo Invalid selection.
    popd
    exit /b 1
)
set "CLASS_NAME=%SRC_FILE:.java=%"

REM 3. Compile the selected .java file to the class subfolder
popd
if not exist "%JAVA_FOLDER%\..\class" mkdir "%JAVA_FOLDER%\..\class"
javac -d "%JAVA_FOLDER%\..\class" -cp "%JAVA_FOLDER%\..\lib\*" "%JAVA_FOLDER%\%SRC_FILE%"
if errorlevel 1 (
    echo Compilation failed!
    exit /b 1
)

REM 4. Ask for arguments to run with (default: none)
set "ARGS="
set /p ARGS=Enter arguments to run with (default: none): 

REM 5. Run the class file
pushd "%JAVA_FOLDER%\..\class"
if errorlevel 1 (
    echo Class folder not found!
    exit /b 1
)
echo Running %CLASS_NAME%...
java -cp ".;..\lib\*" %CLASS_NAME% %ARGS%

REM 6. Check for and optionally run the test file
set "TEST_FILE=%JAVA_FOLDER%\..\test\%CLASS_NAME%Test.java"
if exist "%TEST_FILE%" (
    set /p RUN_TEST=Test file %CLASS_NAME%Test.java found. Run test? (y/n): 
    if /i "%RUN_TEST%"=="y" (
        javac -d . -cp ".;..\lib\*" "%TEST_FILE%"
        if errorlevel 1 (
            echo Test compilation failed!
        ) else (
            echo Running %CLASS_NAME%Test...
            java -cp ".;..\lib\*" %CLASS_NAME%Test
        )
    )
)
popd
