package com.example.offlinevotehotspot.domain.usecase

import com.example.offlinevotehotspot.data.model.Vote
import com.example.offlinevotehotspot.data.repository.VoteRepositoryImpl

class VoteHandler(private val repository: VoteRepositoryImpl) {
    fun handleVote(raw: String): Boolean {
        // simple split parser, could be JSON
        val parts = raw.split(":")
        if (parts.size != 2) return false

        val vote = Vote(parts[0], parts[1])
        repository.addVote(vote)
        return true
    }

    fun getResults(): List<Vote> = repository.getVotes()
}
