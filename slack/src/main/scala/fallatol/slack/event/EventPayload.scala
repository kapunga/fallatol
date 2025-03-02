/*
 * Copyright (c) 2025 Paul (Thor) Thordarson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package fallatol.slack.event

import fallatol.slack.SlackId

object PayloadType {
  val AppRateLimited: String = "app_rate_limited"
  val Callback: String = "event_callback"
  val UrlVerification: String = "url_verification"
}

case class Authorization(
    enterpriseId: Option[SlackId.Enterprise],
    teamId: SlackId.Team,
    userId: SlackId.User,
    isBot: Boolean
)

sealed trait EventPayload {
  def token: String
}

object EventPayload {
  case class AppRateLimited(
      token: String,
      teamId: SlackId.Team,
      minuteRateLimited: String,
      apiAppId: SlackId.App
  ) extends EventPayload

  case class UrlVerification(token: String, challenge: String)
      extends EventPayload

  case class Callback(
      id: SlackId.Event,
      token: String,
      teamId: SlackId.Team,
      event: Event,
      apiAppId: SlackId.App,
      authedUsers: List[SlackId.User],
      authedTeams: List[SlackId.Team],
      authorizations: List[Authorization],
      eventContext: SlackId,
      eventTime: Long
  ) extends EventPayload
}
