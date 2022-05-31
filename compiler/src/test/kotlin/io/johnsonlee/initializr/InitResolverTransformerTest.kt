package io.johnsonlee.initializr

import com.didiglobal.booster.kotlinx.file
import com.didiglobal.booster.kotlinx.touch
import com.didiglobal.booster.transform.AbstractTransformContext
import com.didiglobal.booster.transform.asm.asClassNode
import com.didiglobal.booster.transform.util.collect
import org.junit.Test
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.InputStream
import kotlin.test.assertNotNull

private val BUILD = File(System.getProperty("user.dir"), "build")

private const val FQ_INIT_RESOLVER = "io/johnsonlee/initializr/InitResolver.class"

class InitResolverTransformerTest {

    private val klassInitResolver = {
        javaClass.classLoader.getResourceAsStream(FQ_INIT_RESOLVER)!!.use(InputStream::asClassNode)
    }

    private val classpath = javaClass.classLoader.getResource(FQ_INIT_RESOLVER)!!.toURI().resolve("../../../../").path

    @Test
    fun `generate resolveBuildType`() {
        val context = object : AbstractTransformContext("io.johnsonlee.initializr.app", "debug", emptySet(), setOf(classpath).map(::File)) {}
        val klass = klassInitResolver()
        InitResolverTransformer().apply {
            onPreTransform(context)
            context.collect()
            transform(context, klass)
            onPostTransform(context)
        }
        val output = ClassWriter(ClassWriter.COMPUTE_MAXS or ClassWriter.COMPUTE_FRAMES)
            .also(klass::accept)
            .toByteArray().also {
                BUILD.file("tmp", FQ_INIT_RESOLVER).touch().writeBytes(it)
            }
        val transformed = output.asClassNode()
        assertNotNull(transformed.methods.find {
            it.name == RESOLVE_BUILD_TYPE_NAME_0
        })
        assertNotNull(transformed.methods.find {
            it.name == RESOLVE_BUILD_TYPE_NAME_0
        })
    }

}