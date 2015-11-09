#include <stdio.h>
#include <string.h>
#include <com_example_demo_patch_Launch.h>

JNIEXPORT jstring JNICALL Java_com_example_demo_patch_Launch_getNative(JNIEnv *env,
		jobject obj) {
	return (*env)->NewStringUTF(env,"from jni");
}

