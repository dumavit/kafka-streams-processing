::staging_configure.bat

@echo off

if "%1"=="" goto :empty
if "%2"=="" goto :empty

set AWS_ACCESS_KEY_ID=%1
set AWS_SECRET_ACCESS_KEY=%2

ecs-cli --version || exit /b

ecs-cli configure --config-name ucu-class^
 --cluster ucu-class^
  --default-launch-type FARGATE^
   --region us-east-1^
    --compose-service-name-prefix service- || exit /b
ecs-cli configure default --config-name ucu-class || exit /b
ecs-cli configure profile^
 --access-key %AWS_ACCESS_KEY_ID%^
  --secret-key %AWS_SECRET_ACCESS_KEY%^
   --profile-name ucu-class || exit /b
ecs-cli configure profile default --profile-name ucu-class || exit /b

@rem login to ecr registry
aws ecr get-login --region us-east-1 --no-include-email > _login.bat
call _login.bat
del _login.bat

:empty
echo "Please supply AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY as arguments to this script"