package kz.peep.api.dto

data class PagedResponse <T> (
        val contents: List<T>,
        val page: Int,
        val total: Int
)