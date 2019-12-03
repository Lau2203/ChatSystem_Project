

BUILD_DIR=./build/
SRC_PATH=.
FLAGS=-Xlint -d $(BUILD_DIR) --source-path $(SRC_PATH)
COMP=javac $(FLAGS)

PACKAGE_PATH=./chatsystem/

OBJ=$(PACKAGE_PATH)/Client.java
BASE_OBJS=$(PACKAGE_PATH)/*.java

NETWORK_OBJS=$(PACKAGE_PATH)/network/*.java
UTIL_OBJS=$(PACKAGE_PATH)/util/*.java
GUI_OBJS=$(PACKAGE_PATH)/gui/*.java

LOG_FILE=./logs

ALL_OBJS=$(OBJ) $(BASE_OBJS) $(NETWORK_OBJS) $(UTIL_OBJS) $(GUI_OBJS)

all: $(ALL_OBJS)
	$(COMP) $(ALL_OBJS)

main: $(OBJ)
	$(COMP) $(OBJ)	

network: $(NETWORK_OBJS)
	$(COMP) $(NETWORK_OBJS)

util: $(UTIL_OBJS)
	$(COMP) $(UTIL_OBJS)

gui: $(GUI_OBJS)
	$(COMP) $(GUI_OBJS)

.PHONY: clean

clean:
	rm -rf $(BUILD_DIR) $(LOG_FILE)

