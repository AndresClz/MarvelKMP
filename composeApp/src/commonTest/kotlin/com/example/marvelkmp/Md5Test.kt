package com.example.marvelkmp

import com.example.marvelkmp.data.crypto.md5
import kotlin.test.Test
import kotlin.test.assertEquals

class Md5Test {

    @Test
    fun md5_hash_conocido() {
        assertEquals("900150983cd24fb0d6963f7d28e17f72", md5("abc"))
    }

    @Test
    fun md5_string_vacio() {
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", md5(""))
    }
}
