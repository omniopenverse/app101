import jenkins.model.*
import hudson.slaves.*
import hudson.model.*
import com.nirima.jenkins.plugins.docker.*
import com.nirima.jenkins.plugins.docker.strategy.*
import io.jenkins.docker.client.DockerAPI
import io.jenkins.docker.connector.DockerComputerAttachConnector

// Configure a Docker cloud with a template that uses the built agent image.
// Requires Docker plugin dependencies present (docker-workflow, docker-plugin).

try {
  def instance = Jenkins.getInstance()

  // Docker API points to dind over TLS using env vars
  // Use DockerServerEndpoint with DOCKER_HOST (e.g., tcp://dind:2376) and DOCKER_CERT_PATH (/certs/client)
  def dockerHost = System.getenv('DOCKER_HOST')
  def dockerCertPath = System.getenv('DOCKER_CERT_PATH')
  if (!dockerHost) {
    println "[init] WARNING: DOCKER_HOST is not set; docker cloud may not function."
  }
  if (!dockerCertPath) {
    println "[init] WARNING: DOCKER_CERT_PATH is not set; TLS may fail."
  }
  // Construct DockerAPI using host and cert path (plugin will derive TLS settings)
  def dockerApi = new DockerAPI(dockerHost, dockerCertPath, null)

  def connector = new DockerComputerAttachConnector()
  connector.setUser("jenkins")

  // Template 1: general-purpose agent with Docker CLI
  def templateDocker = new DockerTemplate(
    new DockerTemplateBase(
      "app101/jenkins-agent:latest", // image
      null, // pull credentials ID
      null, // DNS
      null, // network
      null, // volumes
      null, // volumes from
      null, // environments
      null, // lxcConfString
      null, // hostname
      null, // memory limit
      null, // memory swap
      null, // cpu shares
      null, // shm size
      null, // bind ports
      ""   // bind all ports flag
    ),
    connector,
    "docker-agent", // label
    "/home/jenkins/agent", // remote FS
    "1" // instance cap per template
  )
  templateDocker.setMode(Node.Mode.NORMAL)
  templateDocker.setRetentionStrategy(new DockerOnceRetentionStrategy(10))

  // Template 2: Python-focused agent image
  // NOTE: This container image must include a Java runtime for the Jenkins agent.
  def templatePython = new DockerTemplate(
    new DockerTemplateBase(
      "app101/jenkins-python-agent:latest", // image
      null, // pull credentials ID
      null, // DNS
      null, // network
      null, // volumes
      null, // volumes from
      null, // environments
      null, // lxcConfString
      null, // hostname
      null, // memory limit
      null, // memory swap
      null, // cpu shares
      null, // shm size
      null, // bind ports
      ""   // bind all ports flag
    ),
    connector,
    "python-agent", // label
    "/workspace/agent", // remote FS (writable path in the python image)
    "2" // instance cap per template
  )
  templatePython.setMode(Node.Mode.NORMAL)
  templatePython.setRetentionStrategy(new DockerOnceRetentionStrategy(10))

  def cloud = new DockerCloud(
    "docker", // name
    [templateDocker, templatePython],
    dockerApi.getDockerHost().toString(), // display only
    10, // container cap
    dockerApi
  )

  // Replace or add cloud
  def existing = instance.clouds.find { it instanceof DockerCloud && it.name == cloud.name }
  if (existing) {
    instance.clouds.remove(existing)
  }
  instance.clouds.add(cloud)
  instance.save()
  println "[init] Jenkins Docker cloud configured with templates: 'docker-agent' (app101/jenkins-agent:latest) and 'python-agent' (app101/jenkins-python-agent:latest)"
} catch (Throwable t) {
  println "[init] WARNING: Could not configure Docker cloud automatically: ${t.message}"
}
