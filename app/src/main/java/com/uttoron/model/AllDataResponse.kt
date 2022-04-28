package com.uttoron.model

class AllDataResponse : ArrayList<AllDataResponseItem>()

data class AllDataResponseItem(
    val categories: List<Category>,
    val contents: List<Content>,
    val created_at: Any,
    val general_settings: List<GeneralSetting>,
    val id: Int,
    val sub_categories: List<SubCategory>,
    val track_no: Int,
    val updated_at: String
)

data class Category(
    val created_at: String,
    val icon: String,
    val id: Int,
    val name: String,
    val sort_order: Int,
    val updated_at: String,
    val video: String
)

data class Content(
    val category_id: Int,
    val category_name: String,
    val content: String,
    val created_at: String,
    val id: Int,
    val sub_category_id: Int,
    val sub_category: SubCatName,
    val sub_category_name: String,
    val updated_at: String,
    val video: String
)

data class GeneralSetting(
    val app_logo_icon: String,
    val created_at: String,
    val id: Int,
    val slogan: String,
    val about_uttoron: String,
    val updated_at: String
)

data class SubCategory(
    val category_id: Int,
    val category_name: String,
    val created_at: String,
    val icon: String,
    val id: Int,
    val name: String,
    val sort_order: Int,
    val updated_at: String
)