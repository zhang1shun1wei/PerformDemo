package com.example.performdemo.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import kotlin.jvm.internal.Intrinsics;

public class ReflectHelperUtil {
    public static ReflectHelperUtil mReflectHelperUtil;

    private ReflectHelperUtil() {

    }

    public static ReflectHelperUtil getInstance() {
        if (mReflectHelperUtil == null) {
            synchronized (ReflectHelperUtil.class) {
                if (mReflectHelperUtil == null) {
                    mReflectHelperUtil = new ReflectHelperUtil();
                }
            }
        }
        return mReflectHelperUtil;
    }

    public Object reflectField(@Nullable Object instance, @NotNull String name) {
        if (instance == null) {
            return null;
        } else {
            try {
                Field field = instance.getClass().getDeclaredField(name);
                Intrinsics.checkExpressionValueIsNotNull(field, "field");
                field.setAccessible(true);
                return field.get(instance);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public final Method reflectMethod(@Nullable Object instance, @NotNull String name, @NotNull Class... argTypes) {
        if (instance == null) {
            return null;
        } else {
            try {
                Method method = instance.getClass().getDeclaredMethod(name, Arrays.copyOf(argTypes, argTypes.length));
                method.setAccessible(true);
                return method;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
