package com.alex.newstime.repository.api.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiModelSource(val name: String)