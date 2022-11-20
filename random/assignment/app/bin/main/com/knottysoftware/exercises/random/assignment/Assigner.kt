package com.knottysoftware.exercises.random.assignment;

const val VOLUNTEERS = 5000
const val QUESTIONS = 5000
const val TAGS = 100
const val MAX_VOLUNTEER_TAGS = 4
const val MAX_QUESTION_TAGS = 2

fun main() {
  val rng = kotlin.random.Random(42)

  println("Matching $VOLUNTEERS volunteers to $QUESTIONS questions with $TAGS tags")
  val (volunteers, questions) = generate(
      numTags = TAGS, 
      numVolunteers = VOLUNTEERS,
      maxVolunteerTags = MAX_VOLUNTEER_TAGS,
      numQuestions = QUESTIONS,
      maxQuestionTags = MAX_QUESTION_TAGS,
      rng = rng)

  var result = matchGreedyBruteForce(volunteers, questions)
  println("Greedy matches ${result.size} questions")
  result = matchGreedyVolunteerWeighted(volunteers, questions)
  println("Greedy volunteer-weighted matches ${result.size} questions")
}

fun matchGreedyBruteForce(volunteers: List<Volunteer>, questions: List<Question>): Set<Pair<Volunteer, Question>> {
    val matches = mutableSetOf<Pair<Volunteer, Question>>()
    val usedQuestions = mutableSetOf<String>()

    // Start with volunteers because IRL we assume they are always the smaller quantity.
    for (v in volunteers) {
        // Brute force enumeration of the question list until we find one that fits.
        for (q in questions) {
            if (usedQuestions.contains(q.id)) continue;
            if (v.tags.intersect(q.tags).isEmpty()) continue;

            matches.add(Pair(v, q))
            usedQuestions.add(q.id)
            break
        }
    }
    return matches
}

fun matchGreedyVolunteerWeighted(volunteers: List<Volunteer>, questions: List<Question>): Set<Pair<Volunteer, Question>> {
    val matches = mutableSetOf<Pair<Volunteer, Question>>()
    val usedQuestions = mutableSetOf<String>()

    // Build a map of tags to questions.
    val tagToQuestions = mutableMapOf<String, MutableList<Question>>()
    for (q in questions) {
        for (t in q.tags) {
            if (!tagToQuestions.containsKey(t)) tagToQuestions[t] = mutableListOf<Question>()
            tagToQuestions[t]!!.add(q)
        }
    }

    // Sort volunteers based on the number of questions they can answer.
    val sortedVolunteers = volunteers.sortedBy {
        var count: Int = 0
        for (t in it.tags)
            count += tagToQuestions[t]?.size ?: 0
        count
    }

    for (v in sortedVolunteers) {
        tag@ for (t in v.tags) {
            for (q in tagToQuestions[t]!!) {
                if (!usedQuestions.contains(q.id)) {
                    matches.add(Pair(v, q))
                    usedQuestions.add(q.id)
                    break@tag
                }
            }
        }
    }
    return matches
}
