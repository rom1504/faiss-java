# Copyright (c) Facebook, Inc. and its affiliates.
#
# This source code is licensed under the MIT license found in the
# LICENSE file in the root directory of this source tree.

ifneq ($(strip $(NVCC)),)
	SWIGFLAGS += -DGPU_WRAPPER
endif


FAISS_INCLUDE=/usr/local/include
JAVACFLAGS = -I /usr/lib/jvm/java/include/ -I /usr/lib/jvm/java/include/linux/
SWIGJAVASRC = ../src/main/java/com/vectorsearch/faiss/swig
CPPFLAGS = -pthread -Wl,--sysroot=/ -Wsign-compare -DNDEBUG -g -fwrapv -O3 -Wstrict-prototypes -fPIC -DFINTEGER=int -std=c++11 -m64 -Wno-sign-compare -fopenmp -fdata-sections -ffunction-sections

LDFLAGS=-l:libopenblas.a -lgfortran -lgomp
CXXFLAGS=-fvisibility=hidden -fdata-sections -ffunction-sections
LDFLAGS= -pthread -shared -L /usr/local/lib -Wl,--no-as-needed -Wl,--sysroot=/ -fopenmp -lrt -s -Wl,--gc-sections -L/usr/lib64 -L/usr/local/lib -l:libfaiss.a -l:libopenblas.a -l:libgfortran.so.3 -lopenblas

all: build

# Also silently generates swigfaiss.py.
swigfaiss.cpp: swigfaiss.swig ../faiss/build/faiss/libfaiss.a
	mkdir -p $(SWIGJAVASRC)
	$(SWIG) -java -c++ -DSWIGWORDSIZE64 -Doverride= -I$(FAISS_INCLUDE) -I../faiss/faiss $(SWIGFLAGS) -package com.vectorsearch.faiss.swig -outdir $(SWIGJAVASRC) -o $@ $<

%.o: %.cpp
	$(CXX) $(CPPFLAGS) $(CXXFLAGS) $(CPUFLAGS) $(JAVACFLAGS) \
               -I$(FAISS_INCLUDE) -I../faiss/faiss -c $< -o $@

# Extension is .so even on OSX.
_%.so: %.o  ../faiss/build/faiss/libfaiss.a
	$(CXX) $(SHAREDFLAGS) $(LDFLAGS) -Wl,--export-dynamic,-rpath='$$ORIGIN' -o $@ $^ $(LIBS)
	chmod 755 $@

build: _swigfaiss.so
	$(eval _resources_path = ../src/main/resources)
	mkdir -p ${_resources_path}
	cp _swigfaiss.so ${_resources_path}
	ldd _swigfaiss.so
	ld _swigfaiss.so
	#cp _swigfaiss_avx2.so ${_resources_path}
	cp /usr/lib64/libgomp.so.1 ${_resources_path}
	cp /usr/lib64/libquadmath.so.0 ${_resources_path}
	cp /usr/lib64/libgfortran.so.3 ${_resources_path}
	cp /usr/lib64/libopenblas.so ${_resources_path}


clean:
	rm -f swigfaiss*.cpp swigfaiss*.o  _swigfaiss*.so
	rm -rf $(SWIGJAVASRC)


.PHONY: all build install clean