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
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes


class HelloWorld {
    companion object : ClassLoader() {
        @JvmStatic
        fun main(args: Array<String>) {
            val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
            var methodVisitor: MethodVisitor

            // <init>
            run {
                classWriter.visit(
                        Opcodes.V1_8,
                        Opcodes.ACC_PUBLIC,
                        "com/github/patrick/learnasm/build/HelloWorld",
                        null,
                        "java/lang/Object",
                        null
                )

                methodVisitor = classWriter.visitMethod(
                        Opcodes.ACC_PUBLIC,
                        "<init>",
                        "()V",
                        null,
                        null
                )

                methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)

                methodVisitor.visitMethodInsn(
                        Opcodes.INVOKESPECIAL,
                        "java/lang/Object",
                        "<init>",
                        "()V",
                        false
                )

                methodVisitor.visitInsn(Opcodes.RETURN)
                methodVisitor.visitMaxs(0, 0)
                methodVisitor.visitEnd()
            }

            // main
            run {
                methodVisitor = classWriter.visitMethod(
                        Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
                        "main",
                        "([Ljava/lang/String;)V",
                        null,
                        null
                )

                methodVisitor.visitParameterAnnotation(
                        0,
                        "Lorg.jetbrains.annotations.NotNull;",
                        false
                )

                val start = Label()
                methodVisitor.visitLabel(start)

                methodVisitor.visitFieldInsn(
                        Opcodes.GETSTATIC,
                        "java/lang/System",
                        "out",
                        "Ljava/io/PrintStream;"
                )

                methodVisitor.visitLdcInsn("Hello World!")

                methodVisitor.visitMethodInsn(
                        Opcodes.INVOKEVIRTUAL,
                        "java/io/PrintStream",
                        "println",
                        "(Ljava/lang/String;)V",
                        false
                )

                methodVisitor.visitInsn(Opcodes.RETURN)

                val end = Label()
                methodVisitor.visitLabel(end)

                methodVisitor.visitLocalVariable(
                        "args",
                        "[Ljava/lang/String;",
                        null,
                        start,
                        end,
                        0
                )

                methodVisitor.visitMaxs(0, 0)
                methodVisitor.visitEnd()
            }

            val bytes = classWriter.toByteArray()
            val clazz = defineClass(
                    "com.github.patrick.learnasm.build.HelloWorld",
                    bytes,
                    0,
                    bytes.count()
            )

            clazz.getDeclaredMethod("main", Array<String>::class.java).invoke(null, emptyArray<String>())
        }
    }
}