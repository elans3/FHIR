@echo off
@REM ----------------------------------------------------------------------------
@REM (C) Copyright IBM Corp. 2016,2017,2018,2019
@REM
@REM SPDX-License-Identifier: Apache-2.0
@REM ----------------------------------------------------------------------------

@echo off
@REM ----------------------------------------------------------------------------
@REM (C) Copyright IBM Corp. 2016,2017,2018,2019
@REM
@REM SPDX-License-Identifier: Apache-2.0
@REM ----------------------------------------------------------------------------

@echo off
@REM ----------------------------------------------------------------------------
@REM IBM Watson Health Cloud FHIR Server installation script
@REM
@REM Copyright IBM Corp. 2016
@REM The source code for this program is not published or other-
@REM wise divested of its trade secrets, irrespective of what has
@REM been deposited with the U.S. Copyright Office.
@REM 
@REM ----------------------------------------------------------------------------

echo Executing %0 to deploy the fhir-server web application...

@REM Make sure that JAVA_HOME is set
if -%JAVA_HOME%-==-- (
    echo Error: JAVA_HOME not set; make sure JAVA_HOME points to a Java 8 JVM and then re-try.
    set rc=1
    goto :exit
) else (
    set JAVA_CMD=%JAVA_HOME%\bin\java
)

@REM Determine the location of this script.
set BASEDIR=%~dp0

@REM Remove any trailing \ from BASEDIR
if %BASEDIR:~-1%==\ set BASEDIR=%BASEDIR:~0,-1%

@REM Default liberty install location
cd %BASEDIR%\..
set UNZIP_LOC=%CD%
set WLP_INSTALL_DIR=%UNZIP_LOC%\liberty-runtime

@REM Allow user to override default install location
if not -%1-==-- set WLP_INSTALL_DIR=%1

@REM Add a trailing \ to WLP_INSTALL_DIR if needed
if not %WLP_INSTALL_DIR:~-1%==\ set WLP_INSTALL_DIR=%WLP_INSTALL_DIR%\

set ERRORLEVEL=0

echo Deploying fhir-server in location: %WLP_INSTALL_DIR%

@REM If the liberty install directory doesnt exist, then create it.
if not exist %WLP_INSTALL_DIR% (
    echo The Websphere Liberty installation directory does not exist; will attempt to create it.
    md  %WLP_INSTALL_DIR%
    set rc=%ERRORLEVEL%
    if %rc% neq 0 (
        echo Error creating installation directory: %rc%
        goto :exit
    )
)

@REM Unzip liberty runtime zip
echo Extracting WebSphere Liberty runtime.
call :UnZip  %BASEDIR%\server-runtime\wlp-javaee7-8.5.5.9.zip\  %WLP_INSTALL_DIR%
if %rc% neq 0 (
    echo Error extracting liberty runtime: %rc%
    goto :exit
)

@REM Save the liberty home directory.
set WLP_ROOT=%WLP_INSTALL_DIR%wlp

@REM Install any required ifixes
echo Applying service to Websphere Liberty runtime located at: %WLP_ROOT%
%JAVA_CMD% -jar %BASEDIR%\ifixes\IFPI59782-8.5.5.9.jar --installLocation %WLP_ROOT% --suppressInfo
set rc=%ERRORLEVEL%
if %rc% neq 0 (
    echo Error installing ifixes: %rc%
    goto :exit
)

@REM Create our server
echo Creating Websphere Liberty server definition for fhir-server.
%COMSPEC% /c %WLP_ROOT%\bin\server.bat create fhir-server
echo Returned from server.bat!
set rc=%ERRORLEVEL%
if %rc% neq 0 (
    echo Error creating server definition: %rc%
    goto :exit
)

@REM Copy our server assets
echo Deploying fhir-server assets to server runtime environment.
xcopy /S /Y /Q %BASEDIR%\fhir\server\* %WLP_ROOT%
set rc=%ERRORLEVEL%
if %rc% neq 0 (
    echo Error deploying fhir-server assets to server runtime environment: %rc%
    goto :exit
)

echo The FHIR Server has been successfully deployed to the
echo Websphere Liberty runtime located at: %WLP_ROOT%
echo The following manual steps must be completed before the server can be started:
echo 1. Make sure that your selected database (e.g. Derby, DB2) is active and 
echo    ready to accept requests.
echo 2. Modify the server.xml and fhir-server-config.json files located at %WLP_ROOT%\usr\servers\fhir-server 
echo    to properly configure the server according to your requirements.
echo    This includes the definition of the server listener ports, as well as the selection 
echo    of the datastore and other associated configuration.
echo 3. The fhir-server application requires Java 8.
echo    Be sure to set the JAVA_HOME environment variable to point to your Java 8 installation
echo    before starting the server.
echo 4. You can start and stop the server with these commands:
echo    %WLP_ROOT%\bin\server start fhir-server
echo    %WLP_ROOT%\bin\server stop fhir-server
echo 5. The FHIR Server User's Guide can be found in the %WLP_ROOT%\fhir\docs directory.
set rc=0
goto :exit


@REM This function will unzip %1 into the directory %2
@REM by creating a VB script and executing it.
:UnZip
set vbs="%temp%\_.vbs"
if exist %vbs% del /f /q %vbs%
>%vbs% echo Set fso = CreateObject("Scripting.FileSystemObject")
>>%vbs% echo strDest = "%2"
>>%vbs% echo strZipFileName = "%1"
>>%vbs% echo If NOT fso.FolderExists(strDest) Then
>>%vbs% echo     fso.CreateFolder(strDest)
>>%vbs% echo End If
>>%vbs% echo set objShell = CreateObject("Shell.Application")
>>%vbs% echo set FilesInZip=objShell.NameSpace(strZipFileName).items
>>%vbs% echo objShell.NameSpace(strDest).CopyHere(FilesInZip)
>>%vbs% echo Set fso = Nothing
>>%vbs% echo Set objShell = Nothing
cscript //nologo %vbs%
set rc=%ERRORLEVEL%
if exist %vbs% del /f /q %vbs%
goto :eof

:exit
%COMSPEC% /c exit /b %rc%
