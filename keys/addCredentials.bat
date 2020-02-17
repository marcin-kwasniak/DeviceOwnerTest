@echo off

cd ..

set /p password="Enter password for key: "
set /p alias="Enter alias for keystore: "
set /p passwordKeyStore="Enter password for keystore: "

call gradlew addCredentials --key installAppKey --value %password%
call gradlew addCredentials --key installAppKeyStoreAlias --value %alias%
call gradlew addCredentials --key installAppKeyStore --value %passwordKeyStore%

