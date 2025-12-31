import jenkins.model.*
import hudson.model.*

// Build a custom Jenkins agent image on first startup using the local Docker daemon.
// Requires DOCKER_HOST/DOCKER_CERT_PATH/DOCKER_TLS_VERIFY to be set (provided in docker-compose).

println "[init] Building Jenkins agent Docker image..."
try {
  def proc = new ProcessBuilder(["/usr/bin/env", "bash", "-lc", 
    "docker build -t app101/jenkins-agent:latest /var/jenkins_home/agents/docker"]).redirectErrorStream(true).start()
  proc.inputStream.eachLine { println "[init][docker] ${it}" }
  int code = proc.waitFor()
  if (code == 0) {
    println "[init] Jenkins agent image built: app101/jenkins-agent:latest"
  } else {
    println "[init] WARNING: Failed to build Jenkins agent image (exit ${code}). Check docker connectivity and Dockerfile."
  }
} catch (Throwable t) {
  println "[init] ERROR: Exception while building Jenkins agent image: ${t.message}"
}

println "[init] Building Jenkins python agent Docker image..."
try {
  def proc = new ProcessBuilder(["/usr/bin/env", "bash", "-lc", 
    "docker build -t app101/jenkins-python-agent:latest /var/jenkins_home/agents/python"]).redirectErrorStream(true).start()
  proc.inputStream.eachLine { println "[init][docker] ${it}" }
  int code = proc.waitFor()
  if (code == 0) {
    println "[init] Jenkins agent image built: app101/jenkins-python-agent:latest"
  } else {
    println "[init] WARNING: Failed to build Jenkins agent image (exit ${code}). Check docker connectivity and Dockerfile."
  }
} catch (Throwable t) {
  println "[init] ERROR: Exception while building Jenkins agent image: ${t.message}"
}