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

import java.nio.file.Path

import io.gatling.commons.util.PathHelper._

object IDEPathHelper {

  val gatlingConfUrl: Path = getClass.getClassLoader.getResource("gatling.conf")
  val projectRootDir: Path = gatlingConfUrl.ancestor(3)

  val mavenSourcesDirectory: Path = projectRootDir / "src" / "test" / "scala"
  val mavenResourcesDirectory: Path = projectRootDir / "src" / "test" / "resources"
  val mavenTargetDirectory: Path = projectRootDir / "target"
  val mavenBinariesDirectory: Path = mavenTargetDirectory / "test-classes"

  val dataDirectory: Path = mavenResourcesDirectory / "data"
  val bodiesDirectory: Path = mavenResourcesDirectory / "bodies"

  val recorderOutputDirectory: Path = mavenSourcesDirectory
  val resultsDirectory: Path = mavenTargetDirectory / "gatling"

  val recorderConfigFile: Path = mavenResourcesDirectory / "recorder.conf"

}
