/*
 * Copyright (c) 2018 Symantec Corporation. All rights reserved.
 *
 * THIS SOFTWARE CONTAINS CONFIDENTIAL INFORMATION AND TRADE SECRETS OF SYMANTEC
 * CORPORATION.  USE, DISCLOSURE OR REPRODUCTION IS PROHIBITED WITHOUT THE PRIOR
 * EXPRESS WRITTEN PERMISSION OF SYMANTEC CORPORATION.
 *
 * The Licensed Software and Documentation are deemed to be commercial computer
 * software as defined in FAR 12.212 and subject to restricted rights as defined
 * in FAR Section 52.227-19 "Commercial Computer Software - Restricted Rights"
 * and DFARS 227.7202, Rights in "Commercial Computer Software or Commercial
 * Computer Software Documentation," as applicable, and any successor regulations,
 * whether delivered by Symantec as on premises or hosted services.  Any use,
 * modification, reproduction release, performance, display or disclosure of the
 * Licensed Software and Documentation by the U.S. Government shall be solely in
 * accordance with the terms of this Agreement.
 */

package com.loyalty.homework.gatling

import io.gatling.core.Predef.{constantUsersPerSec, _}
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._
import scala.language.postfixOps

class ApiSimulation extends Simulation {

  val httpProtocol: HttpProtocolBuilder = http
      //    .shareConnections
      //    .maxConnectionsPerHostLikeChrome
      .baseURL("http://localhost:8080")
      .disableFollowRedirect
      .acceptHeader("application/json")
      .acceptEncodingHeader("gzip, deflate")
      .acceptLanguageHeader("en-US,en;q=0.5")
      .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:59.0) Gecko/20100101 Firefox/59.0")

  val scn: ScenarioBuilder = scenario("Homework simulation")
      .exec(http("Add post")
          .post("/api/v1/users/test/post")
          .body(StringBody(
            _ =>
              s"""
                 |{"message":"test"}
                 |""".stripMargin)
          ).asJSON
          .notSilent
          .check(status.is(200)))


  setUp(scn.inject(
    rampUsers(20) over (30 seconds),
    constantUsersPerSec(20) during (30 seconds) randomized,
  )).protocols(httpProtocol)
}
