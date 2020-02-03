#!/bin/bash

echo "Creating 'jar' directory..."
mkdir jar/ &> /dev/null
echo "Compiling from sources..."
make &> /dev/null && cd ./build && jar -cvf Aura.jar ./chatsystem/ &> /dev/null && mv ./Aura.jar ../jar/;
echo "Setup done."

