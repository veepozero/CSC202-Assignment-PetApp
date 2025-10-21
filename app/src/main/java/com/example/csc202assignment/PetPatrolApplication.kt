package com.example.csc202assignment

import android.app.Application


class PetPatrolApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PetRepository.initialize(this)
    }
}
