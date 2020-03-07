package com.merricklabs.partymode.bots

import com.merricklabs.partymode.PartymodeIntegrationTestBase
import com.merricklabs.partymode.slack.EventPayload
import com.merricklabs.partymode.slack.SlackCallbackMessage
import io.kotlintest.matchers.string.contain
import io.kotlintest.shouldHave
import org.koin.test.inject
import org.testng.annotations.Test

class PartyBotTest : PartymodeIntegrationTestBase() {
    private val bot by inject<PartyBot>()

    @Test
    fun `Set partymode`() {
        val messageText = "partybot pm 1"
        val callbackMessage = constructMessage(messageText)
        val response = bot.handle(callbackMessage)
        response.text shouldHave contain("partymode enabled for 1 hour")
    }

    @Test
    fun `Disable partymode`() {
        val messageText = "partybot pm disable"
        val callbackMessage = constructMessage(messageText)
        val response = bot.handle(callbackMessage)
        response.text shouldHave contain("partymode disabled")
    }

    @Test
    fun `Disable partymode with space in command`() {
        val messageText = "partybot pm disable "
        val callbackMessage = constructMessage(messageText)
        val response = bot.handle(callbackMessage)
        response.text shouldHave contain("partymode disabled")
    }

    @Test
    fun `Should be case insensitive`() {
        val messageText = "partybot Pm 1"
        val callbackMessage = constructMessage(messageText)
        val response = bot.handle(callbackMessage)
        response.text shouldHave contain("partymode enabled for 1 hour")
    }

    @Test
    fun `Check status is enabled`() {
        val messageText = "partybot pm status"
        enablePartyMode(true)
        val callbackMessage = constructMessage(messageText)
        val response = bot.handle(callbackMessage)
        response.text shouldHave contain("partymode enabled")
    }

    @Test
    fun `Check status is disabled`() {
        val messageText = "partybot pm status"
        enablePartyMode(false)
        val callbackMessage = constructMessage(messageText)
        val response = bot.handle(callbackMessage)
        response.text shouldHave contain("partymode disabled")
    }

    private fun constructMessage(messageText: String): SlackCallbackMessage {
        val eventPayload = EventPayload(
                client_msg_id = null,
                type = "foo",
                subtype = null,
                text = messageText,
                user = null,
                ts = "foo",
                channel = "foo",
                event_ts = "foo",
                bot_id = null,
                channel_type = null
        )
        return SlackCallbackMessage(
                event = eventPayload,
                token = "foo"
        )
    }

}