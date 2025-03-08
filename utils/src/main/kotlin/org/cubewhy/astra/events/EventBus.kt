package org.cubewhy.astra.events

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.valueParameters

object EventBus {
    private val listeners = mutableMapOf<KClass<*>, MutableList<Pair<Any, suspend (Event) -> Unit>>>()
    private val scope = CoroutineScope(Dispatchers.Default)

    fun register(listener: Any) {
        val clazz = listener::class
        for (method in clazz.declaredFunctions) {
            method.findAnnotation<EventHandler>() ?: continue
            val params = method.valueParameters

            if (params.size != 1) {
                throw IllegalArgumentException("Bad EventHandler method: ${method.name}")
            }

            val eventType = params[0].type.classifier as? KClass<*> ?: continue

            listeners.computeIfAbsent(eventType) { mutableListOf() }
                .add(listener to { event -> method.callSuspend(listener, event) })
        }
    }

    fun register(type: KClass<out Event>, handler: suspend (Event) -> Unit) {
        listeners.computeIfAbsent(type) { mutableListOf() }
            .add(UUID.randomUUID() to { event -> handler.invoke(event) })
    }

    fun unregister(listener: Any) {
        listeners.values.forEach { handlers -> handlers.removeIf { handler -> handler.first == listener } }
    }

    fun post(event: Event) {
        listeners[event::class]?.forEach { (_, handler) ->
            scope.launch { handler.invoke(event) }
        }
    }
}
