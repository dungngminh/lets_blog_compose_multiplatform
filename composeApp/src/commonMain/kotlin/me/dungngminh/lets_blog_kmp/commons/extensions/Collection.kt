package me.dungngminh.lets_blog_kmp.commons.extensions

fun <T> Iterable<T>.replaceFirst(
    predicate: (T) -> Boolean,
    transform: (T) -> T,
): List<T> {
    var found = false
    return map {
        if (!found && predicate(it)) {
            found = true
            transform(it)
        } else {
            it
        }
    }
}
