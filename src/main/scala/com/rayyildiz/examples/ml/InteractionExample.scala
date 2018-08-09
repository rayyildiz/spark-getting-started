/*
 * Copyright (c) 2017 Ramazan AYYILDIZ
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.rayyildiz.examples.ml

import com.rayyildiz.SparkSupport
import org.apache.spark.ml.feature.{Interaction, VectorAssembler}

/**
 * Created by rayyildiz on 6/12/2017.
 */
object InteractionExample extends App with SparkSupport {

  val df = spark
    .createDataFrame(
      Seq(
        (1, 1, 2, 3, 8, 4, 5),
        (2, 4, 3, 8, 7, 9, 8),
        (3, 6, 1, 9, 2, 3, 6),
        (4, 10, 8, 6, 9, 4, 5),
        (5, 9, 2, 7, 10, 7, 3),
        (6, 1, 1, 4, 2, 8, 4)
      )
    )
    .toDF("id1", "id2", "id3", "id4", "id5", "id6", "id7")

  val assembler1 = new VectorAssembler().setInputCols(Array("id2", "id3", "id4")).setOutputCol("vec1")

  val assembled1 = assembler1.transform(df)

  val assembler2 = new VectorAssembler().setInputCols(Array("id5", "id6", "id7")).setOutputCol("vec2")

  val assembled2 = assembler2.transform(assembled1).select("id1", "vec1", "vec2")

  val interaction = new Interaction()
    .setInputCols(Array("id1", "vec1", "vec2"))
    .setOutputCol("interactedCol")

  val interactedDF = interaction.transform(assembled2)

  interactedDF.show(truncate = false)

  close()
}
