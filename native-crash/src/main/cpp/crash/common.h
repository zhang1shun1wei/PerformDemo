
#include "android/log.h"

#ifndef RABBIT_CLIENT_COMMON_H
#define RABBIT_CLIENT_COMMON_H


#define LOG_D(...)  __android_log_print(ANDROID_LOG_DEBUG,"mt-native",__VA_ARGS__)

#define ERROR_CODE_INT -1
#define SUCCESS_CODE_INT 1

#endif //RABBIT_CLIENT_COMMON_H
