#include <jni.h>
#include <windows.h>
#include "TimeUtils.h" // Este arquivo será gerado automaticamente

// Converte a estrutura FILETIME do Windows para um valor numérico (long)
long long fileTimeToLong(const FILETIME& ft) {
    ULARGE_INTEGER uli;
    uli.LowPart = ft.dwLowDateTime;
    uli.HighPart = ft.dwHighDateTime;
    return uli.QuadPart;
}

// Implementação do método nativo declarado em TimeUtils.java
JNIEXPORT jlongArray JNICALL Java_TimeUtils_getProcessTimes(JNIEnv *env, jclass cls) {
    FILETIME creationTime, exitTime, kernelTime, userTime;

    if (GetProcessTimes(GetCurrentProcess(), &creationTime, &exitTime, &kernelTime, &userTime)) {
        jlongArray result = env->NewLongArray(2);
        if (result == NULL) {
            return NULL; 
        }

        jlong times[2];
        times[0] = fileTimeToLong(userTime);
        times[1] = fileTimeToLong(kernelTime);

        env->SetLongArrayRegion(result, 0, 2, times);

        return result;
    }

    return NULL;
}