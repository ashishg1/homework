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

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

object Engine extends App {

  val props = new GatlingPropertiesBuilder()
      .dataDirectory(IDEPathHelper.dataDirectory.toString)
      .resultsDirectory(IDEPathHelper.resultsDirectory.toString)
      .bodiesDirectory(IDEPathHelper.bodiesDirectory.toString)
      .binariesDirectory(IDEPathHelper.mavenBinariesDirectory.toString)
      .simulationClass(classOf[ApiSimulation].getName)

  Gatling.fromMap(props.build)
}
