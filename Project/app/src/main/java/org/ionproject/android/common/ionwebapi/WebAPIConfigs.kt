package org.ionproject.android.common.ionwebapi

import org.ionproject.android.userAPI.AUTH_REQ_ID

//const val WEB_API_HOST = "https://host1.dev.ionproject.org"

const val WEB_API_HOST = "http://10.0.2.2:10023" //this address is an alias setup to help in testing since localhost is used by the emulator

const val WEB_API_AUTHORIZATION_TOKEN = "l7kowOOkliu21oXxNpuCyM47u2omkysxb8lv3qEhm5U" //token to be used as a header for the read API, provided by the core docs

const val REMOTE_CONFIG_LINK =
    "https://raw.githubusercontent.com/i-on-project/isel/main/Remote_Config.json"

const val AUTH_METHODS_LINK = "$WEB_API_HOST/api/auth/methods"

const val REFRESH_TOKEN_LINK = "$WEB_API_HOST/api/auth/refreshToken"

const val CLIENT_ID = "22dd1551-db23-481b-acde-d286440388a5" //client ID provided by the core docs for testing

var CORE_POLL_LINK = "$WEB_API_HOST/api/auth/request/$AUTH_REQ_ID/poll"
