package com.example.offlinevotehotspot.domain.repository

import com.example.offlinevotehotspot.data.model.Vote

interface VoteRepository {
    fun addVote(vote: Vote)
    fun getVotes(): List<Vote>
}
