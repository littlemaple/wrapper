LOCAL_PATH := $(call my-dir)
LOCAL_MODULE := Launch
LOCAL_MODULE_FILENAME := libLaunch
LOCAL_LDLIBS += -llog
LOCAL_SHARED_LIBRARIES := liblog libcutils libandroid_runtime libnativehelper 
LOCAL_PRELINK_MODULE := false
LOCAL_SRC_FILES := com_example_demo_patch_Launch.c 
    
include $(BUILD_SHARED_LIBRARY)
include $(CLEAR_VARS)
