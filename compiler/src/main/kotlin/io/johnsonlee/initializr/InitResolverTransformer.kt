package io.johnsonlee.initializr

import com.didiglobal.booster.graph.Graph
import com.didiglobal.booster.transform.Supervisor
import com.didiglobal.booster.transform.TransformContext
import com.didiglobal.booster.transform.asm.ClassTransformer
import com.google.auto.service.AutoService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import java.util.concurrent.ConcurrentHashMap

@AutoService(ClassTransformer::class)
class InitResolverTransformer : ClassTransformer {

    private val config = ConcurrentHashMap<String, MutableSet<InitConfig>>()

    private val parser = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
            .adapter(InitConfig::class.java)

    override fun onPreTransform(context: TransformContext) {
        context.registerCollector(object : Supervisor {
            override fun accept(name: String): Boolean = name.startsWith("${GENERATED_PREFIX}/")
            override fun collect(name: String, data: () -> ByteArray) {
                val spec = parser.fromJson(String(data())) ?: error("Deserializing data from $name failed")
                config.getOrPut(context.name, ::mutableSetOf).add(spec)
            }
        })
    }

    override fun transform(context: TransformContext, klass: ClassNode): ClassNode = klass.apply {
        if (klass.name != FQ_INIT_RESOLVER) return klass
        generateResolveBuildType(context, klass)
        generateResolveBuildFlavor(context, klass)
        generateResolveInitGraph(context, klass)
    }

    private fun generateResolveBuildType(context: TransformContext, klass: ClassNode) {
        val m = klass.methods.singleOrNull { RESOLVE_BUILD_TYPE == "${it.name}${it.desc}" }?.also {
            it.name = RESOLVE_BUILD_TYPE_NAME_0
        } ?: throw RuntimeException("Method $RESOLVE_BUILD_TYPE not found")

        klass.visitMethod(m.access, RESOLVE_BUILD_TYPE_NAME, m.desc, m.signature, m.exceptions.toTypedArray()).apply {
            val tryStart = Label()
            val tryEnd = Label()
            val catchStart = Label()
            val catchEnd = Label()

            // try {
            visitTryCatchBlock(tryStart, tryEnd, catchStart, Type.getInternalName(Throwable::class.java))
            visitLabel(tryStart)
            visitFieldInsn(Opcodes.GETSTATIC, "${context.originalApplicationId.replace('.', '/')}/BuildConfig", "BUILD_TYPE", "L${FQ_JAVA_LANG_STRING};")
            visitInsn(Opcodes.ARETURN)
            visitLabel(tryEnd)
            // } catch (e: Throwable) {
            visitLabel(catchStart)
            visitVarInsn(Opcodes.ASTORE, 1)
            visitVarInsn(Opcodes.ALOAD, 0)
            visitMethodInsn(Opcodes.INVOKESTATIC, klass.name, RESOLVE_BUILD_TYPE_NAME_0, RESOLVE_BUILD_TYPE_DESC, false)
            visitInsn(Opcodes.ARETURN)
            visitLabel(catchEnd)
            // }
        }
    }

    private fun generateResolveBuildFlavor(context: TransformContext, klass: ClassNode) {
        val m = klass.methods.singleOrNull { RESOLVE_BUILD_FLAVOR == "${it.name}${it.desc}" }?.also {
            it.name = RESOLVE_BUILD_FLAVOR_NAME_0
        } ?: return

        klass.visitMethod(m.access, RESOLVE_BUILD_FLAVOR_NAME, m.desc, m.signature, m.exceptions.toTypedArray()).apply {
            val tryStart = Label()
            val tryEnd = Label()
            val catchStart = Label()
            val catchEnd = Label()

            // try {
            visitTryCatchBlock(tryStart, tryEnd, catchStart, Type.getInternalName(Throwable::class.java))
            visitLabel(tryStart)
            visitFieldInsn(Opcodes.GETSTATIC, "${context.originalApplicationId.replace('.', '/')}/BuildConfig", "FLAVOR", "L${FQ_JAVA_LANG_STRING};")
            visitInsn(Opcodes.ARETURN)
            visitLabel(tryEnd)
            // } catch (e: Throwable) {
            visitLabel(catchStart)
            visitVarInsn(Opcodes.ASTORE, 1)
            visitVarInsn(Opcodes.ALOAD, 0)
            visitMethodInsn(Opcodes.INVOKESTATIC, klass.name, RESOLVE_BUILD_FLAVOR_NAME_0, RESOLVE_BUILD_FLAVOR_DESC, false)
            visitInsn(Opcodes.ARETURN)
            visitLabel(catchEnd)
            // }
        }
    }

    private fun generateResolveInitGraph(context: TransformContext, klass: ClassNode) {
        val m = klass.methods.singleOrNull { RESOLVE_INIT_GRAPH == "${it.name}${it.desc}" }?.also {
            klass.methods.remove(it)
        } ?: return

        val configs = (config[context.name] ?: emptySet<InitConfig>()).sortedBy(InitConfig::initializer).toList()
        val nameToConfig = configs.associateBy(InitConfig::name)
        val graph = configs.fold(Graph.Builder<InitConfig>()) { builder, from ->
            from.dependencies.mapNotNull(nameToConfig::get).forEach { to ->
                builder.addEdge(from, to)
            }
            builder
        }.build()
        val lvtOffset = Type.getArgumentTypes(m.desc).size

        klass.visitMethod(m.access, m.name, m.desc, m.signature, m.exceptions.toTypedArray()).apply {
            val start = Label()
            visitLabel(start)

            val initializers = configs.map(InitConfig::initializer)
            val variables = initializers.withIndex().map { (index, initializer) ->
                val slot = index + lvtOffset
                val config = configs[index]
                // ..., wrapper
                visitTypeInsn(Opcodes.NEW, FQ_INITIALIZER_WRAPPER)
                // ..., wrapper, wrapper
                visitInsn(Opcodes.DUP)
                // ..., wrapper, wrapper, name
                visitLdcInsn(config.name)
                // ..., wrapper, wrapper, name, thread
                visitFieldInsn(Opcodes.GETSTATIC, FQ_THREAD_TYPE, config.thread.name, "L${FQ_THREAD_TYPE};")
                val desc = initializer.replace('.', '/')
                // ..., wrapper, wrapper, name, thread, initializer
                visitTypeInsn(Opcodes.NEW, desc)
                // ..., wrapper, wrapper, name, thread, initializer, initializer
                visitInsn(Opcodes.DUP)
                // ..., wrapper, wrapper, name, thread, initializer
                visitMethodInsn(Opcodes.INVOKESPECIAL, desc, "<init>", "()V", false)
                // ..., wrapper
                visitMethodInsn(Opcodes.INVOKESPECIAL, FQ_INITIALIZER_WRAPPER, "<init>", "(L${FQ_JAVA_LANG_STRING};L${FQ_THREAD_TYPE};L${FQ_INITIALIZER};)V", false)

                visitVarInsn(Opcodes.ASTORE, slot)
                Triple(initializer, slot, Label().apply(this::visitLabel))
            }.associateBy(Triple<String, Int, Label>::first)
            val lvt = { initializer: String ->
                variables[initializer]!!.second
            }

            // InitGraph.Builder builder = InitGraph.Builder()
            // ..., builder
            visitTypeInsn(Opcodes.NEW, FQ_INIT_GRAPH_BUILDER)
            // ..., builder, builder
            visitInsn(Opcodes.DUP)
            // ..., builder
            visitMethodInsn(Opcodes.INVOKESPECIAL, FQ_INIT_GRAPH_BUILDER, "<init>", "()V", false)

            // builder.addEdge(u, v)
            graph.forEach { (u, v) ->
                // ..., builder, u
                buildVertex(u, lvt)
                // ..., builder, u, v
                buildVertex(v, lvt)
                // ..., builder
                visitMethodInsn(Opcodes.INVOKEVIRTUAL, FQ_INIT_GRAPH_BUILDER, "addEdge", "(L${FQ_INIT_GRAPH_VERTEX};L${FQ_INIT_GRAPH_VERTEX};)L${FQ_INIT_GRAPH_BUILDER};", false)
            }

            // builder.build()
            // ..., graph
            visitMethodInsn(Opcodes.INVOKEVIRTUAL, FQ_INIT_GRAPH_BUILDER, "build", "()L${FQ_INIT_GRAPH};", false)
            visitInsn(Opcodes.ARETURN)

            // local variable table
            val end = Label()
            visitLabel(end)
            visitLocalVariable("var0", "L${FQ_ANDROID_APP_APPLICATION};", null, start, end, 0)
            initializers.withIndex().forEach { (i, v) ->
                val slot = i + lvtOffset
                visitLocalVariable("var${slot}", "L${FQ_INITIALIZER};", null, variables[v]!!.third, end, slot)
            }
        }
    }

    private fun MethodVisitor.buildVertex(cfg: InitConfig, variable: (String) -> Int) {
        // new Vertex
        // ..., vertex
        visitTypeInsn(Opcodes.NEW, FQ_INIT_GRAPH_VERTEX)
        // ..., vertex, vertex
        visitInsn(Opcodes.DUP)

        // arg0: name
        // ..., vertex, vertex, name
        visitLdcInsn(cfg.name)

        // arg1: initializer
        // ..., vertex, vertex, wrapper
        visitVarInsn(Opcodes.ALOAD, variable(cfg.initializer))

        // arg2: dependencies
        // ..., vertex, vertex, name, wrapper, dependencies
        when (cfg.dependencies.size) {
            0 -> {
                visitMethodInsn(Opcodes.INVOKESTATIC, FQ_JAVA_UTIL_COLLECTIONS, "emptySet", "()L${FQ_JAVA_UTIL_SET};", false)
            }
            1 -> {
                visitLdcInsn(cfg.dependencies.single())
                visitMethodInsn(Opcodes.INVOKESTATIC, FQ_JAVA_UTIL_COLLECTIONS, "singleton", "(L${FQ_JAVA_LANG_OBJECT};)L${FQ_JAVA_UTIL_SET};", false)
            }
            else -> buildDependencies(cfg)
        }

        // arg3: thread
        // ..., vertex, vertex, name, wrapper, dependencies thread
        visitFieldInsn(Opcodes.GETSTATIC, FQ_THREAD_TYPE, cfg.thread.name, "L${FQ_THREAD_TYPE};")

        // ..., vertex
        visitMethodInsn(Opcodes.INVOKESPECIAL, FQ_INIT_GRAPH_VERTEX, "<init>", "(L${FQ_JAVA_LANG_STRING};L${FQ_INITIALIZER_WRAPPER};L${FQ_JAVA_UTIL_SET};L${FQ_THREAD_TYPE};)V", false)
    }

    private fun MethodVisitor.buildDependencies(cfg: InitConfig) {
        // new HashSet<String>
        // ..., dependencies
        visitTypeInsn(Opcodes.NEW, FQ_JAVA_UTIL_HASH_SET)
        // ..., dependencies, dependencies
        visitInsn(Opcodes.DUP)

        // size
        // ..., dependencies, dependencies, size
        when (val size = cfg.dependencies.size) {
            0 -> visitInsn(Opcodes.ICONST_0)
            1 -> visitInsn(Opcodes.ICONST_1)
            2 -> visitInsn(Opcodes.ICONST_2)
            3 -> visitInsn(Opcodes.ICONST_3)
            4 -> visitInsn(Opcodes.ICONST_4)
            5 -> visitInsn(Opcodes.ICONST_5)
            else -> visitVarInsn(Opcodes.BIPUSH, size)
        }

        // new String[size]
        // ..., dependencies, dependencies, array
        visitTypeInsn(Opcodes.ANEWARRAY, FQ_JAVA_LANG_STRING)

        cfg.dependencies.withIndex().forEach { (i, dependency) ->
            // ..., dependencies, dependencies, array, array
            visitInsn(Opcodes.DUP)
            // [i] = xxx
            // ..., dependencies, dependencies, array, array, i
            when (i) {
                0 -> visitInsn(Opcodes.ICONST_0)
                1 -> visitInsn(Opcodes.ICONST_1)
                2 -> visitInsn(Opcodes.ICONST_2)
                3 -> visitInsn(Opcodes.ICONST_3)
                4 -> visitInsn(Opcodes.ICONST_4)
                5 -> visitInsn(Opcodes.ICONST_5)
                else -> visitVarInsn(Opcodes.BIPUSH, i)
            }
            // ..., dependencies, dependencies, array, array, i, dependency
            visitLdcInsn(dependency)
            // ..., dependencies, dependencies, array
            visitInsn(Opcodes.AASTORE)
        }

        // Arrays.asList(...)
        // ..., dependencies, dependencies, list
        visitMethodInsn(Opcodes.INVOKESTATIC, FQ_JAVA_UTIL_ARRAYS, "asList", "([L${FQ_JAVA_LANG_OBJECT};)L${FQ_JAVA_UTIL_LIST};", false)

        // HashSet(...)
        // ..., dependencies
        visitMethodInsn(Opcodes.INVOKESPECIAL, FQ_JAVA_UTIL_HASH_SET, "<init>", "(L${FQ_JAVA_UTIL_COLLECTION};)V", false)
    }

}

private const val PREFIX = "io/johnsonlee/initializr"
private const val INTERNAL = "${PREFIX}/internal"

private const val FQ_JAVA_LANG_OBJECT = "java/lang/Object"
private const val FQ_JAVA_LANG_STRING = "java/lang/String"
private const val FQ_JAVA_UTIL_ARRAYS = "java/util/Arrays"
private const val FQ_JAVA_UTIL_HASH_SET = "java/util/HashSet"
private const val FQ_JAVA_UTIL_COLLECTION = "java/util/Collection"
private const val FQ_JAVA_UTIL_COLLECTIONS = "java/util/Collections"
private const val FQ_JAVA_UTIL_SET = "java/util/Set"
private const val FQ_JAVA_UTIL_LIST = "java/util/List"

private const val FQ_ANDROID_APP_APPLICATION = "android/app/Application"

private const val FQ_INIT_RESOLVER = "${PREFIX}/InitResolver"
private const val FQ_INIT_GRAPH = "${INTERNAL}/InitGraph"
private const val FQ_INIT_GRAPH_BUILDER = "${FQ_INIT_GRAPH}\$Builder"
private const val FQ_INIT_GRAPH_VERTEX = "${FQ_INIT_GRAPH}\$Vertex"
private const val FQ_INITIALIZER_WRAPPER = "${INTERNAL}/InitializerWrapper"

private const val FQ_INITIALIZER = "${PREFIX}/Initializer"
private const val FQ_THREAD_TYPE = "${PREFIX}/annotation/ThreadType"

internal const val RESOLVE_BUILD_TYPE_NAME = "resolveBuildType"
internal const val RESOLVE_BUILD_TYPE_DESC = "(Landroid/app/Application;)Ljava/lang/String;"
internal const val RESOLVE_BUILD_TYPE = "$RESOLVE_BUILD_TYPE_NAME$RESOLVE_BUILD_TYPE_DESC"
internal const val RESOLVE_BUILD_TYPE_NAME_0 = "${RESOLVE_BUILD_TYPE_NAME}0"

internal const val RESOLVE_BUILD_FLAVOR_NAME = "resolveBuildFlavor"
internal const val RESOLVE_BUILD_FLAVOR_DESC = "(Landroid/app/Application;)Ljava/lang/String;"
internal const val RESOLVE_BUILD_FLAVOR = "$RESOLVE_BUILD_FLAVOR_NAME$RESOLVE_BUILD_FLAVOR_DESC"
internal const val RESOLVE_BUILD_FLAVOR_NAME_0 = "${RESOLVE_BUILD_FLAVOR_NAME}0"

internal const val RESOLVE_INIT_GRAPH_NAME = "resolveInitGraph"
internal const val RESOLVE_INIT_GRAPH_DESC = "(Landroid/app/Application;)L${FQ_INIT_GRAPH};"
internal const val RESOLVE_INIT_GRAPH = "${RESOLVE_INIT_GRAPH_NAME}${RESOLVE_INIT_GRAPH_DESC}"
