@file:Suppress("UNCHECKED_CAST")

package io.edanni.rndl.server.infrastructure.mapping

import java.beans.Introspector
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaType

/**
 * Turns a java bean into a Kotlin data class.
 */
fun <T> beanToData(source: Any, dest: KClass<*>): T {
    val parameters = dest.primaryConstructor!!.parameters
    val parametersMap = HashMap<KParameter, Any>()
    val sourceProperties = Introspector.getBeanInfo(source.javaClass).propertyDescriptors
    sourceProperties.forEach { property ->
        val matchingParameter = parameters.find { parameter -> parameter.type.javaType == property.propertyType && parameter.name == property.name }
        if (matchingParameter != null) {
            parametersMap.put(matchingParameter, property.readMethod.invoke(source))
        }
    }
    return dest.primaryConstructor!!.callBy(parametersMap) as T
}