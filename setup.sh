#!/bin/bash

echo "Creating 'jar' directory..."
mkdir jar/ &> /dev/null
echo "Compiling from sources..."
make &> /dev/null && cd ./build && jar -cvf Aura.jar ./chatsystem/ &> /dev/null && mv ./Aura.jar ../jar/;

passwd=""
passwd2="a"

while [ "$passwd" != "$passwd2" ] ; do
	echo -n "New password: "
	read -s passwd
	echo ""
	echo -n "Comfirm new password: "
	read -s passwd2
	echo ""

	if [ "$passwd" != "$passwd2" ] ; then
		echo "Wrong password, please do it again"
	fi
done


cd ../jar/ && java -cp ./Aura.jar chatsystem.util.EncryptionHandler "$passwd" > ../resources/witness

echo "Removing compiled files..."
cd ../ && make clean

echo "Setup done."

