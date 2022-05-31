package io.johnsonlee.initializr.app;

import android.app.Application;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import io.johnsonlee.initializr.*;
import io.johnsonlee.initializr.annotation.ThreadType;
import io.johnsonlee.initializr.internal.*;

public class Test {

    static String resolveBuildType0(Application app) {
        try {
            return Class.forName(app.getPackageName() + ".BuildConfig").getDeclaredField("BUILD_TYPE").get((Object)null).toString();
        } catch (Throwable var2) {
            return 0 != (app.getApplicationInfo().flags & 2) ? "debug" : "release";
        }
    }

    public static String resolveBuildType(final Application var0) {
        try {
            return BuildConfig.BUILD_TYPE;
        } catch (Throwable var2) {
            return resolveBuildType0(var0);
        }
    }

    static void dep0() {
        new HashSet<>(Arrays.asList());
    }

    static void dep1() {
        new HashSet<>(Arrays.asList("dep1"));
    }

    static void dep2() {
        new HashSet<>(Arrays.asList("dep1", "dep2"));
    }

    static void dep3() {
        new HashSet<>(Arrays.asList("dep1", "dep2", "dep3"));
    }

    static void dep4() {
        new HashSet<>(Arrays.asList("dep1", "dep2", "dep3", "dep4"));
    }

    static void dep5() {
        new HashSet<>(Arrays.asList("dep1", "dep2", "dep3", "dep4", "dep5"));
    }

    static void dep6() {
        new HashSet<>(Arrays.asList("dep1", "dep2", "dep3", "dep4", "dep5", "dep6"));
    }

    static void dep7() {
        new HashSet<>(Arrays.asList("dep1", "dep2", "dep3", "dep4", "dep5", "dep6", "dep7"));
    }

    static void resolve(Application var0) {
        Initializer var1 = new InitializerWrapper("m0", ThreadType.MAIN, new M0());
        Initializer var2 = new InitializerWrapper("m0", ThreadType.MAIN, new M1());
        Initializer var3 = new InitializerWrapper("m0", ThreadType.MAIN, new M2());
        Initializer var4 = new InitializerWrapper("m0", ThreadType.MAIN, new M3());
        Initializer var5 = new InitializerWrapper("m0", ThreadType.MAIN, new M4());
        Initializer var6 = new InitializerWrapper("m0", ThreadType.MAIN, new M5());
    }

    static InitGraph resolveInitGraph(Application var0) {
        InitializerWrapper var1 = new InitializerWrapper("m0", ThreadType.MAIN, new M0());
        InitializerWrapper var2 = new InitializerWrapper("m1", ThreadType.MAIN, new M1());
        InitializerWrapper var3 = new InitializerWrapper("m2", ThreadType.MAIN, new M2());
        InitializerWrapper var4 = new InitializerWrapper("m3", ThreadType.MAIN, new M3());
        InitializerWrapper var5 = new InitializerWrapper("m4", ThreadType.MAIN, new M4());
        InitializerWrapper var6 = new InitializerWrapper("m5", ThreadType.MAIN, new M5());
        InitializerWrapper var7 = new InitializerWrapper("m6", ThreadType.MAIN, new M6());
        InitializerWrapper var8 = new InitializerWrapper("m7", ThreadType.MAIN, new M7());
        InitializerWrapper var9 = new InitializerWrapper("m8", ThreadType.MAIN, new M8());
        InitializerWrapper var10 = new InitializerWrapper("m9", ThreadType.MAIN, new M9());
        return (new InitGraph.Builder())
                .addEdge(new InitGraph.Vertex("m1", var2, Collections.singleton("m0"), ThreadType.MAIN), new InitGraph.Vertex("m0", var1, Collections.emptySet(), ThreadType.MAIN))
                .addEdge(new InitGraph.Vertex("m3", var4, Collections.singleton("m1"), ThreadType.MAIN), new InitGraph.Vertex("m1", var2, Collections.singleton("m0"), ThreadType.MAIN))
                .addEdge(new InitGraph.Vertex("m2", var3, Collections.singleton("m0"), ThreadType.MAIN), new InitGraph.Vertex("m0", var1, Collections.emptySet(), ThreadType.MAIN))
                .addEdge(new InitGraph.Vertex("m5", var6, Collections.singleton("m2"), ThreadType.MAIN), new InitGraph.Vertex("m2", var3, Collections.singleton("m0"), ThreadType.MAIN))
                .addEdge(new InitGraph.Vertex("m4", var5, new HashSet(Arrays.asList("m0", "m1", "m2")), ThreadType.MAIN), new InitGraph.Vertex("m0", var1, Collections.emptySet(), ThreadType.MAIN))
                .addEdge(new InitGraph.Vertex("m4", var5, new HashSet(Arrays.asList("m0", "m1", "m2")), ThreadType.MAIN), new InitGraph.Vertex("m1", var2, Collections.singleton("m0"), ThreadType.MAIN))
                .addEdge(new InitGraph.Vertex("m4", var5, new HashSet(Arrays.asList("m0", "m1", "m2")), ThreadType.MAIN), new InitGraph.Vertex("m2", var3, Collections.singleton("m0"), ThreadType.MAIN))
                .addEdge(new InitGraph.Vertex("m7", var8, Collections.singleton("m6"), ThreadType.MAIN), new InitGraph.Vertex("m6", var7, new HashSet(Arrays.asList("m3", "m4", "m5")), ThreadType.MAIN))
                .addEdge(new InitGraph.Vertex("m6", var7, new HashSet(Arrays.asList("m3", "m4", "m5")), ThreadType.MAIN), new InitGraph.Vertex("m3", var4, Collections.singleton("m1"), ThreadType.MAIN))
                .addEdge(new InitGraph.Vertex("m6", var7, new HashSet(Arrays.asList("m3", "m4", "m5")), ThreadType.MAIN), new InitGraph.Vertex("m4", var5, new HashSet(Arrays.asList("m0", "m1", "m2")), ThreadType.MAIN))
                .addEdge(new InitGraph.Vertex("m6", var7, new HashSet(Arrays.asList("m3", "m4", "m5")), ThreadType.MAIN), new InitGraph.Vertex("m5", var6, Collections.singleton("m2"), ThreadType.MAIN))
                .addEdge(new InitGraph.Vertex("m9", var10, Collections.singleton("m6"), ThreadType.MAIN), new InitGraph.Vertex("m6", var7, new HashSet(Arrays.asList("m3", "m4", "m5")), ThreadType.MAIN))
                .addEdge(new InitGraph.Vertex("m8", var9, Collections.singleton("m6"), ThreadType.MAIN), new InitGraph.Vertex("m6", var7, new HashSet(Arrays.asList("m3", "m4", "m5")), ThreadType.MAIN))
                .build();
    }
}
