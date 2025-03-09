package org.cubewhy.utils.ui

import java.util.*

/**
 * A mutable list implementation that supports observers (listeners) for changes.
 *
 * This class extends [MutableList] and allows listeners to be notified of changes made to
 * the list, such as additions, removals, updates, or clearing of the list. It supports
 * adding and removing listeners to track the list's state changes.
 *
 * @param T The type of elements in the list.
 */
class ObservableList<T> : MutableList<T> {
    private val innerList = LinkedList<T>()
    private val listeners = mutableListOf<(ChangeType, T?, Int) -> Unit>()

    /**
     * Enum class representing the types of changes that can occur to the list.
     */
    enum class ChangeType { ADD, REMOVE, UPDATE, CLEAR }

    /**
     * Adds a listener to the list of listeners. The listener will be notified of changes
     * to the list.
     *
     * @param listener The listener to be added, which receives the change type,
     *                 the element affected, and the index of the affected element.
     */
    fun addListener(listener: (ChangeType, T?, Int) -> Unit) {
        listeners.add(listener)
    }

    /**
     * Removes a previously added listener from the list of listeners.
     *
     * @param listener The listener to be removed.
     */
    fun removeListener(listener: (ChangeType, T?, Int) -> Unit) {
        listeners.remove(listener)
    }

    /**
     * Notifies all registered listeners of a change to the list.
     *
     * @param type The type of change (e.g., ADD, REMOVE, etc.)
     * @param element The element affected by the change (or null if not applicable).
     * @param index The index of the affected element (or -1 if not applicable).
     */
    private fun notifyChange(type: ChangeType, element: T?, index: Int) {
        listeners.forEach { it(type, element, index) }
    }

    /**
     * Returns the size of the list.
     */
    override val size: Int get() = innerList.size

    override fun contains(element: T) = innerList.contains(element)
    override fun containsAll(elements: Collection<T>) = innerList.containsAll(elements)
    override fun isEmpty() = innerList.isEmpty()
    override fun iterator() = innerList.iterator()
    override fun listIterator() = innerList.listIterator()
    override fun listIterator(index: Int) = innerList.listIterator(index)
    override fun subList(fromIndex: Int, toIndex: Int) = innerList.subList(fromIndex, toIndex)

    override fun get(index: Int) = innerList[index]
    override fun indexOf(element: T) = innerList.indexOf(element)
    override fun lastIndexOf(element: T) = innerList.lastIndexOf(element)

    /**
     * Adds an element to the list and notifies listeners of the addition.
     *
     * @param element The element to add to the list.
     * @return True if the element was successfully added, false otherwise.
     */
    override fun add(element: T): Boolean {
        val result = innerList.add(element)
        if (result) notifyChange(ChangeType.ADD, element, innerList.size - 1)
        return result
    }

    /**
     * Adds an element at the specified index and notifies listeners of the addition.
     *
     * @param index The index at which to add the element.
     * @param element The element to add.
     */
    override fun add(index: Int, element: T) {
        innerList.add(index, element)
        notifyChange(ChangeType.ADD, element, innerList.size - 1)
    }

    /**
     * Removes the element at the specified index and notifies listeners of the removal.
     *
     * @param index The index of the element to remove.
     * @return The element that was removed.
     */
    override fun removeAt(index: Int): T {
        val removed = innerList.removeAt(index)
        notifyChange(ChangeType.REMOVE, removed, index)
        return removed
    }

    /**
     * Removes the specified element from the list and notifies listeners of the removal.
     *
     * @param element The element to remove.
     * @return True if the element was successfully removed, false otherwise.
     */
    override fun remove(element: T): Boolean {
        val index = innerList.indexOf(element)
        val result = innerList.remove(element)
        if (result) notifyChange(ChangeType.REMOVE, element, index)
        return result
    }

    /**
     * Updates the element at the specified index and notifies listeners of the update.
     *
     * @param index The index of the element to replace.
     * @param element The element to set at the specified index.
     * @return The previous element at the specified index.
     */
    override fun set(index: Int, element: T): T {
        val old = innerList.set(index, element)
        notifyChange(ChangeType.UPDATE, element, index)
        return old
    }

    /**
     * Clears all elements from the list and notifies listeners of the clear operation.
     */
    override fun clear() {
        innerList.clear()
        notifyChange(ChangeType.CLEAR, null, -1)
    }

    /**
     * Adds all elements from the specified collection to the list and notifies listeners
     * of the additions.
     *
     * @param elements The collection of elements to add.
     * @return True if all elements were successfully added, false otherwise.
     */
    override fun addAll(elements: Collection<T>): Boolean {
        val result = innerList.addAll(elements)
        if (result) {
            elements.forEach {
                notifyChange(ChangeType.ADD, it, innerList.indexOf(it))
            }
        }
        return result
    }

    /**
     * Adds all elements from the specified collection at the specified index and notifies
     * listeners of the additions.
     *
     * @param index The index at which to add the elements.
     * @param elements The collection of elements to add.
     * @return True if all elements were successfully added, false otherwise.
     */
    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val result = innerList.addAll(index, elements)
        if (result) {
            elements.forEach {
                notifyChange(ChangeType.ADD, it, innerList.indexOf(it))
            }
        }
        return result
    }

    /**
     * Removes all elements in the specified collection from the list and notifies listeners
     * of the removals.
     *
     * @param elements The collection of elements to remove.
     * @return True if any elements were removed, false otherwise.
     */
    override fun removeAll(elements: Collection<T>): Boolean {
        val elementSet = elements.toSet()
        val result = innerList.removeAll(elementSet)
        if (result) {
            elementSet.forEach {
                notifyChange(ChangeType.REMOVE, it, innerList.indexOf(it))
            }
        }
        return result
    }

    /**
     * Retains only the elements in the specified collection and notifies listeners of the updates.
     *
     * @param elements The collection of elements to retain.
     * @return True if the list was modified, false otherwise.
     */
    override fun retainAll(elements: Collection<T>): Boolean {
        val result = innerList.retainAll(elements.toSet())
        notifyChange(ChangeType.UPDATE, null, -1)
        return result
    }
}
