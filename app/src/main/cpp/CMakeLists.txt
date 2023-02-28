cmake_minimum_required(VERSION 3.22.1)
project(viper4android)

add_library(viper4android SHARED viper4android.cpp)

target_link_libraries(viper4android log)
