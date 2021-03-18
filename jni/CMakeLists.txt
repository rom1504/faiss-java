cmake_minimum_required(VERSION 3.17 FATAL_ERROR)

project(faiss-java
  VERSION 1.6.4
  DESCRIPTION "A java jni wrapper on top of faiss library"
  HOMEPAGE_URL "https://github.com/facebookresearch/faiss"
  LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_FLAGS_RELEASE "-O3")

find_package(SWIG REQUIRED COMPONENTS [java c++])
include(${SWIG_USE_FILE})
set(CMAKE_MODULE_PATH ../faiss/cmake)
set(CMAKE_POSITION_INDEPENDENT_CODE ON)
set(UseSWIG_TARGET_NAME_PREFERENCE STANDARD)
set(SWIG_SOURCE_FILE_EXTENSIONS swig)
set(CMAKE_SWIG_FLAGS -Doverride= -package com.vectorsearch.faiss.swig)
set(CMAKE_SWIG_OUTDIR ${PROJECT_SOURCE_DIR}/../src/main/java/com/vectorsearch/faiss/swig)

set(CMAKE_PREFIX_PATH ${PROJECT_SOURCE_DIR}/../faiss/dist/)

find_library(FAISS_LIBRARY faiss REQUIRED)
if (FAISS_LIBRARY)
  set(FAISS_INCLUDE_DIRS ${PROJECT_SOURCE_DIR}/../faiss/dist/include/)
  message (STATUS "USING FAISS LIBRARY: ${FAISS_LIBRARY}")
  message (STATUS "USING FAISS INCLUDE DIR: ${FAISS_INCLUDE_DIRS}")
endif()

set(FAISS_INCLUDE_DIRS ${PROJECT_SOURCE_DIR}/../faiss/dist/include/)

set_source_files_properties(swigfaiss.swig PROPERTIES
  OUTFILE_DIR ${PROJECT_SOURCE_DIR}
  CPLUSPLUS ON
  INCLUDE_DIRECTORIES ${FAISS_INCLUDE_DIRS}
)

if(NOT ${CMAKE_SYSTEM_NAME} MATCHES "Darwin" AND NOT WIN32)
  set_source_files_properties(swigfaiss.swig PROPERTIES
    SWIG_FLAGS -DSWIGWORDSIZE64
  )
endif()

if(WIN32)
  set_source_files_properties(swigfaiss.swig PROPERTIES
    SWIG_FLAGS -DSWIGWIN
  )
endif()

swig_add_library(swigfaiss
  TYPE SHARED
  LANGUAGE java
  SOURCES swigfaiss.swig
)

find_package(Java REQUIRED)
include(UseJava)
find_package(JNI REQUIRED)

if (DEFINED JNI_INCLUDE_DIRS)
    message (STATUS "JNI_INCLUDE_DIRS=${JNI_INCLUDE_DIRS}")
    message (STATUS "JNI_LIBRARIES=${JNI_LIBRARIES}")
endif()

find_package(OpenMP REQUIRED)
target_link_libraries(swigfaiss PRIVATE OpenMP::OpenMP_CXX)

find_package(MKL)
if(MKL_FOUND)
  target_link_libraries(swigfaiss PRIVATE ${MKL_LIBRARIES})
  message (STATUS "MKL_LIBRARIES=${MKL_LIBRARIES}")
else()
  find_package(BLAS REQUIRED)
  target_link_libraries(swigfaiss PRIVATE ${BLAS_LIBRARIES})
  message (STATUS "BLAS_LIBRARIES=${BLAS_LIBRARIES}")
  find_package(LAPACK REQUIRED)
  target_link_libraries(swigfaiss PRIVATE ${LAPACK_LIBRARIES})
  message (STATUS "LAPACK_LIBRARIES=${LAPACK_LIBRARIES}")
endif()




target_include_directories(swigfaiss PRIVATE ${JNI_INCLUDE_DIRS} ${FAISS_INCLUDE_DIRS})
target_link_libraries(swigfaiss PRIVATE ${FAISS_LIBRARY})

if (${CMAKE_SYSTEM_NAME} MATCHES "Darwin")
  set(SWIGFAISS_DEST darwin)
elseif(WIN32)
  set(SWIGFAISS_DEST win32)
else()
  set(SWIGFAISS_DEST linux)
  foreach (_file /usr/lib64/libgomp.so
  /usr/lib64/libgomp.so.1
  /usr/lib64/libquadmath.so.0
  /usr/lib64/libgfortran.so.3
  /usr/lib64/libopenblas.so.0)
  if(EXISTS ${_file})
    get_filename_component(_resolvedFile "${_file}" REALPATH)
    get_filename_component(_filename "${_file}" NAME)
    install(
      FILES ${_resolvedFile}
      DESTINATION ${PROJECT_SOURCE_DIR}/../src/main/resources/${SWIGFAISS_DEST}
      RENAME ${_filename})
  endif()
  endforeach()
endif()

install(TARGETS swigfaiss
  EXPORT faiss-targets
  LIBRARY DESTINATION ${PROJECT_SOURCE_DIR}/../src/main/resources/${SWIGFAISS_DEST}
)


