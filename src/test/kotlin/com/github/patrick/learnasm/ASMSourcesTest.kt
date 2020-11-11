/*
 * Copyright (C) 2020 PatrickKR
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Contact me on <mailpatrickkr@gmail.com>
 */

package com.github.patrick.learnasm

import com.github.patrick.learnasm.sources.HelloWorld
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals


@Suppress("unused")
class ASMSourcesTest {
    private val testOutStream = ByteArrayOutputStream()
    private val testErrStream = ByteArrayOutputStream()
    private val outStream = System.out
    private val errStream = System.err

    @BeforeEach
    fun setUpStreams() {
        System.setOut(PrintStream(testOutStream))
        System.setErr(PrintStream(testErrStream))
    }

    @AfterEach
    fun restoreStreams() {
        System.setOut(outStream)
        System.setErr(errStream)
    }

    @Test
    fun testHelloWorld() {
        assertDoesNotThrow {
            HelloWorld.main(emptyArray())
        }

        assertEquals("Hello World!", testOutStream.toString().trim())
    }
}