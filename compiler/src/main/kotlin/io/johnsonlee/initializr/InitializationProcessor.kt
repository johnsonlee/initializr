package io.johnsonlee.initializr

import com.github.mustachejava.util.DecoratedCollection
import com.google.auto.common.AnnotationMirrors
import com.google.auto.service.AutoService
import io.johnsonlee.codegen.compiler.*
import io.johnsonlee.codegen.model.Model
import io.johnsonlee.codegen.template.TemplateEngine
import io.johnsonlee.codegen.template.mustache.MustacheEngine
import io.johnsonlee.initializr.annotation.Initialization
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.TypeElement
import javax.lang.model.util.SimpleAnnotationValueVisitor8

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class InitializationProcessor : CodegenProcessor<Initialization>() {

    override val engine: TemplateEngine by lazy(::MustacheEngine)

    override fun onProcessing(processingEnv: ProcessingEnvironment, annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment) {
        roundEnv.onEachAnnotatedElement { element ->
            val implementation = element.asTypeElement()
            val mirror = element.getAnnotationMirror<Initialization>() ?: return@onEachAnnotatedElement
            val name = mirror.name
            val dependencies = DecoratedCollection(mirror.dependencies)

            if (checkImplementation(implementation, processingEnv.elementUtils.getTypeElement(Initializer::class.java.name))) {
                val model = InitModel(implementation.qualifiedName.toString(), name, dependencies)
                generate("${TEMPLATE_PREFIX}/${IDENTIFIER}", model, InitializrResource)
            } else {
                processingEnv.error("${implementation.qualifiedName} does not implement ${Initializer::class.java}", element, mirror)
            }
        }
    }

    private fun checkImplementation(implementation: TypeElement, api: TypeElement): Boolean {
        val verify: String? by processingEnv.options.withDefault { null }
        if (verify == null || !java.lang.Boolean.parseBoolean(verify)) {
            return true
        }
        return implementation.isSubtypeOf(api)
    }

}

object InitializrResource : Resource {
    override val extension: String = ""
    override val prefix: String = GENERATED_PREFIX
}

data class InitModel(
    override val qualifiedName: String,
    val name: String,
    val dependencies: DecoratedCollection<String>
) : Model {
    override val references: Set<String> = emptySet()
    override val packageName: String = qualifiedName.substringBeforeLast('/')
    override val simpleName: String = qualifiedName.substringAfterLast('/')
}

private val AnnotationMirror.name: String
    get() = AnnotationMirrors.getAnnotationValue(this, "name").accept(object : SimpleAnnotationValueVisitor8<String, Void>() {
        override fun visitString(value: String, p: Void?) = value
    }, null)

private val AnnotationMirror.dependencies: Set<String>
    get() = AnnotationMirrors.getAnnotationValue(this, "dependencies").accept(object : SimpleAnnotationValueVisitor8<Set<String>, Void>() {
        override fun visitString(value: String, p: Void?): Set<String> = setOf(value)
        override fun visitArray(values: MutableList<out AnnotationValue>, p: Void?): Set<String> {
            return values.mapNotNull {
                it.accept(this, null)
            }.flatten().toSet()
        }
    }, null)
