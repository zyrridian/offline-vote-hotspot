package com.example.offlinevotehotspot.data.repository

import com.example.offlinevotehotspot.data.model.Vote
import com.example.offlinevotehotspot.domain.repository.VoteRepository

class VoteRepositoryImpl : VoteRepository {
    private val votes = mutableListOf<Vote>()

    override fun addVote(vote: Vote) {
        votes.add(vote)
    }

    override fun getVotes(): List<Vote> = votes
}
