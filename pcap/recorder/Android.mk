APP_ABI := armeabi-v7a,x86
 LOCAL_PATH := $(call my-dir)
 LOCAL_MULTILIB := 64 
  
  include $(CLEAR_VARS)
#LOCAL_C_INCLUDES=/armobj/jni,/armobj/jni/pcap
  LOCAL_MODULE:= pcap
  LOCAL_SRC_FILES := libpcap.a
  include $(PREBUILT_STATIC_LIBRARY)



LOCAL_PATH := $(call my-dir)
include ${CLEAR_VARS}
#APP_STL := stlport_static  
LOCAL_MODULE := test 
LOCAL_SRC_FILES := /armobj/jni/test.c
LOCAL_STATIC_LIBRARIES:=pcap

LOCAL_C_INCLUDES :=/armobj/jni\
		/ndk/android-ndk-r17/sysroot/usr/include/

#$(LOCAL_PATH)

#LOCAL_CFLAGS := $(L_CFLAGS)
#LOCAL_LDLIBS += -ldl
#LOCAL_LDLIBS := -llog -lz
include $(BUILD_EXECUTABLE)
#include $(BUILD_SHARED_LIBRARY)

