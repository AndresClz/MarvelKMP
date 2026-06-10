package com.example.marvelkmp.data.crypto

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import platform.CoreCrypto.CC_MD5
import platform.CoreCrypto.CC_MD5_DIGEST_LENGTH

@OptIn(ExperimentalForeignApi::class)
actual fun md5(input: String): String {
    val data = input.encodeToByteArray()
    val digest = ByteArray(CC_MD5_DIGEST_LENGTH)
    // usePinned requires at least 1 element; use a dummy byte when input is empty
    val pinSource = if (data.isEmpty()) ByteArray(1) else data
    pinSource.usePinned { dataPin ->
        digest.usePinned { digestPin ->
            CC_MD5(dataPin.addressOf(0), data.size.toUInt(), digestPin.addressOf(0).reinterpret())
        }
    }
    return digest.joinToString("") { byte ->
        (byte.toInt() and 0xFF).toString(16).padStart(2, '0')
    }
}
