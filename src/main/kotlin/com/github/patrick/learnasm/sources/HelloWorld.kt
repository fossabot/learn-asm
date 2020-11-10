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

package com.github.patrick.learnasm.sources

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.*
import java.io.File

class HelloWorld {
    companion object : ClassLoader() {
        @JvmStatic
        fun run() {
            main(emptyArray())
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)

            classWriter.visit(
                    V1_8,
                    ACC_PUBLIC,
                    "com/github/patrick/learnasm/build/HelloWorld",
                    null,
                    "java/lang/Object",
                    null
            )

            val methodVisitor = classWriter.visitMethod(
                    ACC_PUBLIC + ACC_STATIC,
                    "main",
                    "([Ljava/lang/String;)V",
                    null,
                    null
            )

            methodVisitor.visitFieldInsn(
                    GETSTATIC,
                    "java/lang/System",
                    "out",
                    "Ljava/io/PrintStream;"
            )

            methodVisitor.visitLdcInsn(
                    "Hello World!"
            )

            methodVisitor.visitMethodInsn(
                    INVOKEVIRTUAL,
                    "java/io/PrintStream",
                    "println",
                    "(Ljava/lang/String;)V",
                    false
            )

            methodVisitor.visitInsn(
                    RETURN
            )

            methodVisitor.visitMaxs(
                    0,
                    0
            )

            methodVisitor.visitEnd()

            val bytes = classWriter.toByteArray()
            val clazz = defineClass(
                    "com.github.patrick.learnasm.build.HelloWorld",
                    bytes,
                    0,
                    bytes.count()
            )

            clazz.getDeclaredMethod("main", Array<String>::class.java).invoke(null, null)
        }
    }
}