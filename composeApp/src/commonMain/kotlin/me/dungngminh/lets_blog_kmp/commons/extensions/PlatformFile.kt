package me.dungngminh.lets_blog_kmp.commons.extensions

import io.github.vinceglb.filekit.core.PlatformFile

suspend fun PlatformFile.toByteArray(): ByteArray =
    if (this.supportsStreams()) {
        val size = this.getSize()
        if (size != null && size > 0L) {
            val buffer = ByteArray(size.toInt())
            val tmpBuffer = ByteArray(1000)
            var totalBytesRead = 0
            this.getStream().use {
                while (it.hasBytesAvailable()) {
                    val numRead = it.readInto(tmpBuffer, 1000)
                    tmpBuffer.copyInto(
                        buffer,
                        destinationOffset = totalBytesRead,
                        endIndex = numRead,
                    )
                    totalBytesRead += numRead
                }
            }
            buffer
        } else {
            this.readBytes()
        }
    } else {
        this.readBytes()
    }