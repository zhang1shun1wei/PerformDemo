#ifndef CLIENT_JVMTI_HANDLER_H
#define CLIENT_JVMTI_HANDLER_H

#include "jni.h"

#ifdef __cplusplus
extern "C" {
#endif

void register_community_handler(JNIEnv *env, jobject obj);

void notifyMessage(JNIEnv *env, const char *msg);

#ifdef __cplusplus
}
#endif

#endif
