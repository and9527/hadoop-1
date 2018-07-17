/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. See accompanying LICENSE file.
 */

package org.apache.hadoop.yarn.submarine.client.cli.param;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.hadoop.yarn.api.records.Resource;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.submarine.client.cli.CliConstants;
import org.apache.hadoop.yarn.submarine.client.cli.CliUtils;
import org.apache.hadoop.yarn.submarine.common.ClientContext;

import java.io.IOException;

/**
 * Parameters used to run a job
 */
public class RunJobParameters extends RunParameters {
  private String input;
  private String checkpointPath;

  private int numWorkers;
  private int numPS;
  private Resource workerResource;
  private Resource psResource;
  private boolean tensorboardEnabled;
  private String workerLaunchCmd;
  private String psLaunchCmd;

  private boolean waitJobFinish = false;

  private String remoteConfigFolder;

  @Override
  public void updateParametersByParsedCommandline(CommandLine parsedCommandLine,
      Options options, ClientContext clientContext)
      throws ParseException, IOException, YarnException {

    String input = parsedCommandLine.getOptionValue(CliConstants.INPUT_PATH);
    String jobDir = parsedCommandLine.getOptionValue(CliConstants.CHECKPOINT_PATH);
    int nWorkers = 1;
    if (parsedCommandLine.getOptionValue(CliConstants.N_WORKERS) != null) {
      nWorkers = Integer.parseInt(
          parsedCommandLine.getOptionValue(CliConstants.N_WORKERS));
    }

    int nPS = 0;
    if (parsedCommandLine.getOptionValue(CliConstants.N_PS) != null) {
      nPS = Integer.parseInt(
          parsedCommandLine.getOptionValue(CliConstants.N_PS));
    }

    String workerResourceStr = parsedCommandLine.getOptionValue(
        CliConstants.WORKER_RES);
    if (workerResourceStr == null) {
      throw new ParseException("--" + CliConstants.WORKER_RES + " is absent.");
    }
    Resource workerResource = CliUtils.createResourceFromString(
        workerResourceStr,
        clientContext.getOrCreateYarnClient().getResourceTypeInfo());

    Resource psResource = null;
    if (nPS > 0) {
      String psResourceStr = parsedCommandLine.getOptionValue(CliConstants.PS_RES);
      if (psResourceStr == null) {
        throw new ParseException("--" + CliConstants.PS_RES + " is absent.");
      }
      psResource = CliUtils.createResourceFromString(psResourceStr,
          clientContext.getOrCreateYarnClient().getResourceTypeInfo());
    }

    boolean tensorboard = false;
    if (parsedCommandLine.getOptionValue(CliConstants.TENSORBOARD) != null) {
      tensorboard = Boolean.parseBoolean(
          parsedCommandLine.getOptionValue(CliConstants.TENSORBOARD));
    }

    if (parsedCommandLine.hasOption(CliConstants.WAIT_JOB_FINISH)) {
      this.waitJobFinish = true;
    }

    String workerLaunchCmd = parsedCommandLine.getOptionValue(
        CliConstants.WORKER_LAUNCH_CMD);
    String psLaunchCommand = parsedCommandLine.getOptionValue(
        CliConstants.PS_LAUNCH_CMD);

    if (parsedCommandLine.getOptionValue(CliConstants.REMOTE_CONFIG_FOLDER)
        != null) {
      this.remoteConfigFolder = parsedCommandLine.getOptionValue(
          CliConstants.REMOTE_CONFIG_FOLDER);
    }

    this.setInputPath(input).setCheckpointPath(jobDir).setNumPS(nPS).setNumWorkers(nWorkers)
        .setPSLaunchCmd(psLaunchCommand).setWorkerLaunchCmd(workerLaunchCmd)
        .setPsResource(psResource).setWorkerResource(workerResource)
        .setTensorboardEnabled(tensorboard);

    super.updateParametersByParsedCommandline(parsedCommandLine,
        options, clientContext);
  }

  public String getInputPath() {
    return input;
  }

  public RunJobParameters setInputPath(String input) {
    this.input = input;
    return this;
  }

  public String getCheckpointPath() {
    return checkpointPath;
  }

  public RunJobParameters setCheckpointPath(String checkpointPath) {
    this.checkpointPath = checkpointPath;
    return this;
  }

  public int getNumWorkers() {
    return numWorkers;
  }

  public RunJobParameters setNumWorkers(int numWorkers) {
    this.numWorkers = numWorkers;
    return this;
  }

  public int getNumPS() {
    return numPS;
  }

  public RunJobParameters setNumPS(int numPS) {
    this.numPS = numPS;
    return this;
  }

  public Resource getWorkerResource() {
    return workerResource;
  }

  public RunJobParameters setWorkerResource(Resource workerResource) {
    this.workerResource = workerResource;
    return this;
  }

  public Resource getPsResource() {
    return psResource;
  }

  public RunJobParameters setPsResource(Resource psResource) {
    this.psResource = psResource;
    return this;
  }

  public boolean isTensorboardEnabled() {
    return tensorboardEnabled;
  }

  public RunJobParameters setTensorboardEnabled(boolean tensorboardEnabled) {
    this.tensorboardEnabled = tensorboardEnabled;
    return this;
  }

  public String getWorkerLaunchCmd() {
    return workerLaunchCmd;
  }

  public RunJobParameters setWorkerLaunchCmd(String workerLaunchCmd) {
    this.workerLaunchCmd = workerLaunchCmd;
    return this;
  }

  public String getPSLaunchCmd() {
    return psLaunchCmd;
  }

  public RunJobParameters setPSLaunchCmd(String psLaunchCmd) {
    this.psLaunchCmd = psLaunchCmd;
    return this;
  }

  public boolean isWaitJobFinish() {
    return waitJobFinish;
  }

  public String getRemoteConfigFolder() {
    return remoteConfigFolder;
  }
}
