::staging_compose.bat

@echo off

@rem read envs
for /f "delims== tokens=1,2" %%G in (.env) do set %%G=%%H

set SERVICE_NAME=%1

if /I not "%SERVICE_NAME%"=="streaming-app" (
   if /I not "%SERVICE_NAME%"=="weather-provider" (
      if /I not "%SERVICE_NAME%"=="solar-panel-emulator" (
         echo "should supply name of the service [streaming-app|weather-provider|solar-panel-emulator]"
         exit /B 1
      )
   )
)

@rem get all but firsts arguments
echo all args: %*
for /f "tokens=1,* delims= " %%a in ("%*") do set ALL_BUT_FIRST=%%b
echo all but first: %ALL_BUT_FIRST%

@rem construct compose command
ecs-cli compose^
   --cluster-config ucu-class^
   --region us-east-1^
   --debug^
   --file staging-%SERVICE_NAME%.yml^
   --project-name %STUDENT_NAME%-%SERVICE_NAME%^
   service %ALL_BUT_FIRST%
