package ua.motorny.objectbox.objectboxissue

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val boxStore = MyObjectBox.builder().androidContext(applicationContext).build()
        boxStore.startObjectBrowser()
        boxStore.boxFor(Teacher::class.java).put(Teacher())

        while (true) {
            val students = ArrayList<Student>()
            for (i in 0 until 1000) {
                students.add(Student())
            }

            val start = System.nanoTime()
            val teacher = boxStore.boxFor(Teacher::class.java)[777]
            teacher.students.addAll(students)
            Log.d("DBLog", "Students list size: ${teacher.students.size} ")
            boxStore.boxFor(Teacher::class.java).put(teacher)
            val tookTime = ((System.nanoTime() - start) / 1000000)
            Log.d("DBLog", "Updating students took: $tookTime ms")
        }
    }
}

@Entity
class Teacher(
        @Id(assignable = true)
        var id: Long = 777
) {
    lateinit var students: ToMany<Student>
}

@Entity
class Student(
        @Id
        var id: Long = 0
) {
    var field: String

    init {
        val stringBuilder = StringBuilder()
        for (i in 0 until 100) {
            stringBuilder.append(UUID.randomUUID().toString())
        }
        field = stringBuilder.toString()
    }
}