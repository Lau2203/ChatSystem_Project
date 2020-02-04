#!/bin/bash

configFilePath="./configs/config.cfg"
witnessFilePath="./resources/witness"

echo "Creating 'jar' directory..."
mkdir jar/ &> /dev/null
echo "Compiling from source..."
make &> /dev/null && cd ./build && echo "Creating jar file..." && jar -cvf Aura.jar ./chatsystem/ &> /dev/null && mv ./Aura.jar ../jar/;
cd ../

if [ ! -r "$witnessFilePath" ] ; then

	echo -n "[*] No account was found, do you want to create one ? [yes] : "

	read answer

	case "$answer" in
		no)
			;;
		*)
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

			cd ./jar && java -cp ./Aura.jar chatsystem.util.EncryptionHandler "$passwd" > ../resources/witness
			cd ../
			;;
	esac

fi

echo "Removing compiled files..."
make clean

echo "Setup done."

