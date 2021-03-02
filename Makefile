.PHONY: help all build install clean
.DEFAULT_GOAL := help

ifeq (,$(shell which cmake3))
	CMAKE_CMD := cmake
else
	CMAKE_CMD := cmake3
endif

FAISS_ENABLE_GPU := OFF
FAISS_OPT_LEVEL := generic
CMAKE_ARGS := -DCMAKE_INSTALL_PREFIX=dist -DFAISS_ENABLE_GPU=${FAISS_ENABLE_GPU} -DFAISS_ENABLE_PYTHON=OFF -DBUILD_TESTING=ON -DFAISS_OPT_LEVEL=${FAISS_OPT_LEVEL} -DCMAKE_BUILD_TYPE=Release

help: ## Print the help
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

build: build_faiss build_jni build_java
test: test_java

build_faiss:
	@cd faiss && ${CMAKE_CMD} . -B build ${CMAKE_ARGS} && make -C build -j && make -C build install

build_jni: build_faiss
	@cd jni && make build

build_java: build_jni
	mvn package

test_java: build_jni
	mvn clean test -Dtest=FaissTestRunner