package com.pocketherbarium.notes.domain.repository

interface CollectionRepository {

    fun names(): List<String>

    fun activeName(): String

    fun setActive(name: String)

    fun add(name: String): String

    fun reset()
}
