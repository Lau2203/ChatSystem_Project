# Chat System

First, you will need to compile and install the software from the source. Go to the cloned git directory and be sure to let aura, aura_remote and setup.sh files be executables

```
cd ./ChatSystem_Project && chmod +x ./aura ./aura_remote ./setup.sh
```

You can now execute the setup shell script (setup.sh), it will basically compile the software from source, creating a jar file (which you will be able to take anywhere), and ask for a new account’s password if resources/witness file does not exist (which is the file containing the encrypted password’s key).

Casual execution of setup.sh :

```
Creating 'jar' directory...
Compiling from source...
Creating jar file...
[*] No account was found, do you want to create one ? [yes] : 
New password:
Confirm new password:
Removing compiled files...
rm -rf ./build/ ./logs*
Setup done.
```


## Remote server setup

As for the remote server, please stop it and start it again, since the servlet might have previous memory of users not online anymore, so that you have a clean and untouched new servlet instance. The war file is located in server/web-app/AURA-Server.war.
As you will be able to understand in the Future Improvements section, the servlet seems to be working fine, but the remote side of aura (we are talking about aura_remote, not aura script).


## Important files

In case of troubleshooting, please take into account the following files and their meaning for Aura.

### configs/config.cfg ###

This file is. You can directly modify it by hand if you will to, but be careful to respect the format ^key=value$.

```
witness-file=../resources/witness
nsl-port=54321
username=Cathy
remote-server-url=https://srv-gei-tomcat.insa-toulouse.fr/AURA-Server/AURA-Server
server-hostname=archlab
server-port=5555
log-file=../logs
message-history=../resources/history/history_backup.mh
log-filling=true
```

If log-filling is true, do not hesitate to have a look into the log file (its path is the value of the log-file key). message-history mentions the path of the message history xml file. As you can see, it can be useful to modify the remote-server-url, nsl-port or server-port, but keep in mind that all the instances of Aura will need to get their configuration file modified, since they all work with the same port numbers and remote server URL. For obvious reasons dealing with the witness file, this config file should only have root permission for writing.

### resources/history/ ###

This directory should contain all the XML-formatted history files. Aura will only use one of these files which is referenced in the configs/config.cfg file (value of the message-history key). By default it is history_backup.mh but you can use another file aswell, e.g. when that history file reaches a considerable size.

### aura & aura_remote ###

These are the shell scripts launching the Aura software. aura is supposed to be launched when in the company’s local network and aura_remote should be used while in a remote network.

