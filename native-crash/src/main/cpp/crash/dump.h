
#include "android/log.h"
#include "errno.h"
#include "signal.h"

#ifndef CLIENT_UTLIS_H
#define CLIENT_UTLIS_H

static char *JAVA_CRASH_CALLBACK_METHOD_NAME = const_cast<char *>("onCaptureNativeCrash");
static char *JAVA_CRASH_CALLBACK_METHOD_SIGNATURE = const_cast<char *>("()V");
static char *JAVA_CLASS_NAME = const_cast<char *>("com/demo/native_crash/MTNativeCrashCaptor");


#ifdef __cplusplus
extern "C" {
#endif

void start_dump_crash(int sig, siginfo_t *si, void *uc);
void init_dump_thread(JavaVM *jvm);

typedef struct {
    int sig;
    siginfo_t *si;
    void *uc;
} crash_context;

#ifdef __cplusplus
}
#endif

#endif