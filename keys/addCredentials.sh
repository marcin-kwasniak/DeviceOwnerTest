#!/bin/bash

cd ..

function askForPassword(){
    echo -n $1
    read -s $2
    echo
}

# Read password for key
askForPassword "Enter password for key: " password

# Read alias
askForPassword "Enter alias for keystore: " aliasKeyStore

# Read password for keystore
askForPassword "Enter password for keystore: " passwordKeyStore

./gradlew addCredentials --key installAppKey --value $password
./gradlew addCredentials --key installAppKeyStore --value aliasKeyStore
./gradlew addCredentials --key installKeyStore --value $passwordKeyStore

echo Done
