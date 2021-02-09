LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
LOCAL_SRC_FILES:=\
bpf_filter.c\
bpf_image.c\
etherent.c\
grammar.c\
fad-gifc.c\
gencode.c\
scanner.c\
sf-pcap-ng.c\
sf-pcap.c\
pcap-common.c\
pcap-netfilter-linux.c\
pcap.c\
inet.c\
optimize.c\
pcap.c\
pcap-linux.c\
nametoaddr.c\
savefile.c
LOCAL_CFLAGS:=-O2 -g
LOCAL_CFLAGS+=-DHAVE_CONFIG_H -D_U_="__attribute__((unused))" -Dlinux -D__GLIBC__ -D_GNU_SOURCE
LOCAL_MODULE:= libpcap
include $(BUILD_STATIC_LIBRARY)
