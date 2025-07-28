FROM omniopenverse/adminstack:1.0.1

USER root

# Install kind
RUN curl -Lo ./kind https://kind.sigs.k8s.io/dl/v0.29.0/kind-linux-amd64 \
    && chmod +x ./kind \
    && sudo mv ./kind /usr/local/bin/kind   

RUN pip3 install bash_kernel \
    && python3 -m bash_kernel.install

USER adminstack