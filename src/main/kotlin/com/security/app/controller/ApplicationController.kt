package com.security.app.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/application")
class ApplicationController {
    @GetMapping
    fun sayHello(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello World from secured endpoint!")
    }
}
