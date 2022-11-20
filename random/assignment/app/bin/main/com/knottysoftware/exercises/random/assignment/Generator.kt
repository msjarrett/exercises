package com.knottysoftware.exercises.random.assignment;

import kotlin.random.Random;

private fun <T> randomSet(fullList: List<T>, maxSize: Int, rng: Random): Set<T> {
    val inList = fullList.toMutableList()
    val outSubset = mutableSetOf<T>()
    repeat(rng.nextInt(1, maxSize)) {
      val index = rng.nextInt(0, inList.size)
      outSubset.add(inList[index])
      inList.removeAt(index)
    }
    return outSubset
}

fun generate(numTags: Int, numVolunteers: Int, numQuestions: Int, maxVolunteerTags: Int = numTags, maxQuestionTags: Int = numTags, rng: Random = Random(42)):  Pair<List<Volunteer>, List<Question>> {
    val tags = (1..numTags).toList().map() { "T" + it }
    val volunteers = (1..numVolunteers).toList().map() { Volunteer("V" + it, randomSet(tags, maxVolunteerTags, rng)) }
    val questions = (1..numQuestions).toList().map() { Question("Q" + it, randomSet(tags, maxQuestionTags, rng)) }
    return Pair(volunteers, questions);
}