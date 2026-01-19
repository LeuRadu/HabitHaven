package com.raduleu.habithaven.core.data.mapper

import com.raduleu.habithaven.core.data.local.entity.FocusEntity
import com.raduleu.habithaven.core.model.Focus

fun FocusEntity.asDomain(): Focus {
    return Focus(
        id = id,
        name = name,
        iconName = iconName,
        colorIndex = colorIndex,
        sortOrder = sortOrder,
        isArchived = isArchived
    )
}

fun Focus.asEntity(): FocusEntity {
    return FocusEntity(
        id = id,
        name = name,
        iconName = iconName,
        colorIndex = colorIndex,
        sortOrder = sortOrder,
        isArchived = isArchived
    )
}