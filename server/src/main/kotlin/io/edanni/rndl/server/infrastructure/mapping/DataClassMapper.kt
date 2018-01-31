package io.edanni.rndl.server.infrastructure.mapping

import org.jooq.TableRecord
import java.beans.Introspector
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.jvmErasure

/**
 * Turns a JOOQ Record into a Kotlin data class.
 */
fun <T : Any> recordToData(source: TableRecord<*>, dest: KClass<T>): T {
    val parameters = dest.primaryConstructor!!.parameters
    val parametersMap = HashMap<KParameter, Any>()
    val sourceProperties = Introspector.getBeanInfo(source.javaClass).propertyDescriptors
    // same type properties
    sourceProperties.forEach { property ->
        val matchingParameter = parameters.find { parameter -> parameter.type.javaType == property.propertyType && parameter.name == property.name }
        if (matchingParameter != null) {
            val value = property.readMethod.invoke(source)
            if (value != null) {
                parametersMap[matchingParameter] = value
            }
        }
    }
    // string to enums
    sourceProperties.filter { it.propertyType == String::class.java }
            .forEach { property ->
                val matchingParameter = parameters.find { it.name == property.name && Enum::class.java.isAssignableFrom(it.type.jvmErasure.java) }
                if (matchingParameter != null) {
                    val sourceValue = property.readMethod.invoke(source)
                    if (sourceValue != null) {
                        parametersMap[matchingParameter] = matchingParameter.type.jvmErasure.java
                                .getMethod("valueOf", String::class.java)
                                .invoke(null, sourceValue)
                    }
                }
            }
    return dest.primaryConstructor!!.callBy(parametersMap)
}