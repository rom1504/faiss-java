/* Primitive stdint types */

%typemap(jni) uint64_t,          const uint64_t &               "jlong"
%typemap(jni) int64_t,           const int64_t &               "jlong"
%typemap(jni) uint32_t,          const uint32_t &               "jint"
%typemap(jni) int32_t,           const int32_t &               "jint"
%typemap(jni) size_t,           const size_t &               "jlong"

%typemap(jtype) uint64_t,        const uint64_t &               "long"
%typemap(jtype) int64_t,        const int64_t &               "long"
%typemap(jtype) uint32_t,          const uint32_t &               "int"
%typemap(jtype) int32_t,           const int32_t &               "int"
%typemap(jtype) size_t,           const size_t &               "long"

%typemap(jstype) uint64_t,        const uint64_t &               "long"
%typemap(jstype) int64_t,        const int64_t &               "long"
%typemap(jstype) uint32_t,          const uint32_t &               "int"
%typemap(jstype) int32_t,           const int32_t &               "int"
%typemap(jstype) size_t,           const size_t &               "long"

%typemap(jboxtype) uint64_t,        const uint64_t &               "Long"
%typemap(jboxtype) int64_t,        const int64_t &               "Long"
%typemap(jboxtype) uint32_t,          const uint32_t &               "Integer"
%typemap(jboxtype) int32_t,           const int32_t &               "Integer"
%typemap(jboxtype) size_t,           const size_t &               "Long"

%typemap(in) uint64_t, int64_t, uint32_t, int32_t, size_t
%{ $1 = ($1_ltype)$input; %}

%typemap(directorout) uint64_t, int64_t, uint32_t, int32_t, size_t
%{ $result = ($1_ltype)$input; %}

%typemap(directorin, descriptor="J") uint64_t "$input = (jlong) $1;"
%typemap(directorin, descriptor="J") int64_t "$input = (jlong) $1;"
%typemap(directorin, descriptor="I") uint32_t "$input = (jint) $1;"
%typemap(directorin, descriptor="I") int32_t "$input = (jint) $1;"
%typemap(directorin, descriptor="J") size_t "$input = (jlong) $1;"

%typemap(javadirectorin) uint64_t, int64_t, uint32_t, int32_t, size_t "$jniinput"
%typemap(javadirectorout) uint64_t, int64_t, uint32_t, int32_t, size_t "$javacall"

%typemap(out) uint64_t   %{ $result = (jlong)$1; %}
%typemap(out) int64_t    %{ $result = (jlong)$1; %}
%typemap(out) uint32_t   %{ $result = (jint)$1; %}
%typemap(out) int32_t    %{ $result = (jint)$1; %}
%typemap(out) size_t     %{ $result = (jlong)$1; %}

/* primitive types by reference */

%typemap(in) const int64_t& ($*1_ltype temp),
        const uint64_t& ($*1_ltype temp),
        const int32_t& ($*1_ltype temp),
        const uint32_t& ($*1_ltype temp),
        const size_t& ($*1_ltype temp)
%{ temp = ($*1_ltype)$input;
$1 = &temp; %}

%typemap(directorout,warning=SWIGWARN_TYPEMAP_THREAD_UNSAFE_MSG) const int64_t &,
        const uint64_t &,
        const int32_t &,
        const uint32_t &,
        const size_t &
%{ static $*1_ltype temp;
   temp = ($*1_ltype)$input;
   $result = &temp; %}

%typemap(directorin, descriptor="J") const uint64_t & "$input = (jlong)$1;"
%typemap(directorin, descriptor="J") const int64_t & "$input = (jlong)$1;"
%typemap(directorin, descriptor="I") const uint32_t & "$input = (jint)$1;"
%typemap(directorin, descriptor="I") const int32_t & "$input = (jint)$1;"
%typemap(directorin, descriptor="J") const size_t & "$input = (jlong)$1;"

%typemap(javadirectorin) const int64_t & ($*1_ltype temp),
        const uint64_t & ($*1_ltype temp),
        const int32_t & ($*1_ltype temp),
        const uint32_t & ($*1_ltype temp),
        const size_t & ($*1_ltype temp)
        "$jniinput"

%typemap(javadirectorout) const int64_t & ($*1_ltype temp),
        const uint64_t & ($*1_ltype temp),
        const int32_t & ($*1_ltype temp),
        const uint32_t & ($*1_ltype temp),
        const size_t & ($*1_ltype temp)
        "$javacall"

%typemap(out) const uint64_t &  %{ $result = (jlong)*$1; %}
%typemap(out) const int64_t &  %{ $result = (jlong)*$1; %}
%typemap(out) const uint32_t &  %{ $result = (jint)*$1; %}
%typemap(out) const int32_t &  %{ $result = (jint)*$1; %}
%typemap(out) const size_t &  %{ $result = (jlong)*$1; %}

%typemap(javain) uint64_t, const uint64_t &,
        int64_t, const int64_t &,
        uint32_t, const uint32_t &,
        int32_t, const int32_t &,
        size_t, const size_t &
    "$javainput"

%typemap(javaout) uint64_t, const uint64_t &,
        int64_t, const int64_t &,
        uint32_t, const uint32_t &,
        int32_t, const int32_t &,
        size_t, const size_t &
 { return $jnicall; }