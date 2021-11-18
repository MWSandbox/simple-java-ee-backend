#!/bin/bash
# This script will be executed when starting the container. Add your initialization logic in here
# Add secret to Payara passwordFile for noninteractive alias creation. Secret needs to be provided as environment variable db_password
sed -i '/create-password-alias db-password/d' $POSTBOOT_COMMANDS
if [[ ! -z "$db_password" ]]; then
  echo "AS_ADMIN_ALIASPASSWORD=${db_password}" >> $PASSWORD_FILE
  echo "create-password-alias db-password --passwordfile ${PASSWORD_FILE}" >> $POSTBOOT_COMMANDS
  unset db_password
else
  echo "No DB password provided"
fi
