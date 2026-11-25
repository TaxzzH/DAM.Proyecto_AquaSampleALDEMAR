package com.duoc.proyectoaldemar.data

import dev.insforge.auth.Auth
import dev.insforge.createInsforgeClient
import dev.insforge.database.Database
import dev.insforge.storage.Storage

object InsforgeProvider {
    val client = createInsforgeClient(
        baseURL = "https://kgkzid4k.us-east.insforge.app/",
        anonKey = "ik_0bb553e3c67f3255abd68dc45976f14c"
    ) {
        install(Auth)
        install(Database)
        install(Storage)
    }
}
