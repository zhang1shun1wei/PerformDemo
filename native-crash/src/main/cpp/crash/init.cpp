#include <jni.h>
#include <cstdio>
#include <cstdlib>
#include "android/log.h"
#include "dump.h"
#include "register.h"
#include "unwind.h"
#include "common.h"

using namespace std;

static const char *className = "com/demo/native_crash/MTNativeCrashCaptor";

//崩溃测试
void test_crash(int type) {

    int *a = nullptr;

    *a = type; // crash!
    (*a)++;
    type = *a;

    return;

}

static void makeCrash() {
    LOG_D("make crash question");
    test_crash(0);
}

static jstring init_native_crash_captor(JNIEnv *env, jobject obj) {

    int result = register_crash_signal_handler(env, obj);

    if (result == ERROR_CODE_INT) {
        return env->NewStringUTF("init native crash captor failed!");
    }

    return env->NewStringUTF("init native crash captor success!");
}

//JNI方法映射表
//参考链接: https://blog.csdn.net/qq_20404903/article/details/80662316
static JNINativeMethod native_methods[] = {
        {"nativeInitCaptor", "(Ljava/lang/String;)Ljava/lang/String;", (void *) init_native_crash_captor},
        {"nativeCrash",      "()V",                                    (void *) makeCrash}
};

//动态注册jni方法，so加载完成后，后自动调用这个方法
JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOG_D("JNI_OnLoad()");

    JNIEnv *env;

    if (vm == nullptr) return ERROR_CODE_INT;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return ERROR_CODE_INT;
    }

    if (env == nullptr) return ERROR_CODE_INT;

    jclass cls = env->FindClass(className);

    if (cls == nullptr) return ERROR_CODE_INT;

    jint native_methods_count = sizeof(native_methods) / sizeof(native_methods[0]);

    env->RegisterNatives(cls, native_methods, native_methods_count);

    LOG_D("register native method success!");

    init_unwind();

    init_dump_thread(vm);

    return JNI_VERSION_1_6;

}