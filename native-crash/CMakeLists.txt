# documentation: https://d.android.com/studio/projects/add-native-code.html
cmake_minimum_required(VERSION 3.4.1)

project (mt-crash)

message("src dir -> ${PROJECT_SOURCE_DIR}")

# 设置库文件的输出路径（LIBRARY_OUTPUT_PATH是全局变量）
set(LIBRARY_OUTPUT_PATH ${PROJECT_SOURCE_DIR}/libs)

# 查找当前目录所有源文件，并将名称保存到 DIR_SRCS 变量
# 不能查找子目录
#aux_source_directory(${PROJECT_SOURCE_DIR} DIR_SRCS)

#file(GLOB DIR_SRCS *.c *.cpp)

add_library(mt-crash
        # Sets the library as a shared library.
        SHARED
        # Provides a relative path to your source file(s).
        src/main/cpp/crash/unwind.cpp
        src/main/cpp/crash/dump.cpp
        src/main/cpp/crash/register.cpp
        src/main/cpp/crash/init.cpp)

find_library(
        log-lib
        log)

target_link_libraries(
        mt-crash
        ${log-lib})