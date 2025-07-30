FROM omniopenverse/adminstack:1.0.1

USER root

# Install kind
RUN curl -Lo ./kind https://kind.sigs.k8s.io/dl/v0.29.0/kind-linux-amd64 \
    && chmod +x ./kind \
    && sudo mv ./kind /usr/local/bin/kind   

RUN pip3 install bash_kernel \
    && python3 -m bash_kernel.install

# Install helm
#   Ref: https://helm.sh/docs/intro/install/

# RUN curl https://baltocdn.com/helm/signing.asc | gpg --dearmor | sudo tee /usr/share/keyrings/helm.gpg > /dev/null \
#     && apt-get install apt-transport-https --yes \
#     && echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/helm.gpg] https://baltocdn.com/helm/stable/debian/ all main" | sudo tee /etc/apt/sources.list.d/helm-stable-debian.list \
#     && apt-get update \
#     && apt-get install helm

RUN curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 \
    && chmod 700 get_helm.sh \
    && ./get_helm.sh

USER adminstack