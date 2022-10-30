package eu.ways4.trackex.home.domain

import eu.ways4.trackex.data.model.Tag

class SortTagsUseCase {
    operator fun invoke(tags: List<Tag>): List<Tag> {
        return tags.sortedBy { it.name }
    }
}