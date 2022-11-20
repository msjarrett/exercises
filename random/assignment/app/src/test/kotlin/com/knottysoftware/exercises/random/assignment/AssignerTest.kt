package com.knottysoftware.exercises.random.assignment;

import kotlin.test.BeforeTest;
import kotlin.test.Test;
import kotlin.test.assertFalse;
import kotlin.test.assertTrue;

class AssignerTest {
    private lateinit var questions: List<Question>
    private lateinit var volunteers: List<Volunteer>

    @BeforeTest fun setUp() {
        val (v, q) = generate(
            numTags = 50,
            numVolunteers = 100,
            numQuestions = 100,
            maxVolunteerTags = 10,
            maxQuestionTags = 10,
        )
        volunteers = v
        questions = q
    }
    @Test fun `greedy produces valid result`() {
        val matches = matchGreedyBruteForce(volunteers, questions)
        verifyUniqueMatches(matches)
        verifyTagOverlap(matches)
    }

    @Test fun `volunteerWeighted produces valid result`() {
        val matches = matchGreedyVolunteerWeighted(volunteers, questions)
        verifyUniqueMatches(matches)
        verifyTagOverlap(matches)
    }

    @Test fun `search produces valid result`() {
        val matches = matchSearch(volunteers, questions)
        verifyUniqueMatches(matches)
        verifyTagOverlap(matches)
    }

    private fun verifyUniqueMatches(matches: Set<Pair<Volunteer, Question>>) {
        val usedVolunteers = mutableSetOf<String>()
        val usedQuestions = mutableSetOf<String>()
        for ((v, q) in matches) {
            assertFalse(usedVolunteers.contains(v.id))
            assertFalse(usedQuestions.contains(q.id))
            usedVolunteers.add(v.id)
            usedQuestions.add(q.id)
        }
    }

    private fun verifyTagOverlap(matches: Set<Pair<Volunteer, Question>>) {
        for ((v, q) in matches) {
            assertTrue(v.tags.any {q.tags.contains(it)})
        }
    }
}
