FROM jenkins/jenkins:2.504.3-jdk21
USER root
RUN apt-get update && apt-get install -y lsb-release ca-certificates curl && \
    install -m 0755 -d /etc/apt/keyrings && \
    curl -fsSL https://download.docker.com/linux/debian/gpg -o /etc/apt/keyrings/docker.asc && \
    chmod a+r /etc/apt/keyrings/docker.asc && \
    echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] \
    https://download.docker.com/linux/debian $(. /etc/os-release && echo \"$VERSION_CODENAME\") stable" \
    | tee /etc/apt/sources.list.d/docker.list > /dev/null && \
    apt-get update && apt-get install -y docker-ce-cli && \
    apt-get clean && rm -rf /var/lib/apt/lists/*
    
USER jenkins
RUN jenkins-plugin-cli --plugins "blueocean docker-workflow json-path-api"
# Install Jenkins plugins
RUN jenkins-plugin-cli --plugins "slack:latest global-slack-notifier:latest \
kubernetes:latest \
configuration-as-code:latest \
credentials:latest \
git:latest git-client:latest github:latest \
github-oauth:latest gitlab-plugin:latest"

# Install Docker Pipeline plugin
RUN jenkins-plugin-cli --plugins "docker-workflow:latest"
# Install Blue Ocean plugins
RUN jenkins-plugin-cli --plugins "blueocean:latest blueocean-autofavorite:latest prometheus:819.v50953a_c560dd"
