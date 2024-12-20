package me.dungngminh.lets_blog_kmp.commons.extensions

fun String.isValidEmail(): Boolean = Regex("^[A-Za-z](.*)(@)(.+)(\\.)(.+)").matches(this)
