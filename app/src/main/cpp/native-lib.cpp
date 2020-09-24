#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_naitik_myndkdemo_Repository_ApiClient_baseurl(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "http://136.233.136.42:89/api/";

//
    return env->NewStringUTF(hello.c_str());
}


extern "C" JNIEXPORT jstring JNICALL
Java_com_naitik_myndkdemo_Repository_Repository_verifymobile(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "CustomerV1/CustomerMobileVerificationCode";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_naitik_myndkdemo_Repository_Repository_getCustomer(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "CustomerV1/GetCustomerOrderList";
    return env->NewStringUTF(hello.c_str());
}
