package com.it.restroexample.util
/**
 * created by Akash on 24/12/2020
 * Created for the purpose of giving const value to various parameter, webservice for eliminating the typo issues.
 */
interface BaseInterface {
    companion object {
        const val WS_BASE = "https://hacker-news.firebaseio.com/v0/"
        const val WS_TOP = "/v0/topstories.json?print=pretty"

        const val PN_FORM = "form"
    }
}
