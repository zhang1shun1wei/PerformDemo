#include "android/log.h"

#ifndef CLIENT_COMMON_H
#define CLIENT_COMMON_H


#define LOG_D(...)  __android_log_print(ANDROID_LOG_DEBUG, "JvmTi",__VA_ARGS__)

#define ERROR_CODE_INT -1
#define SUCCESS_CODE_INT 1

#endif
