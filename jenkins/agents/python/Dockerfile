# Python + common build tools
FROM python:3.12-slim

RUN apt-get update -y && apt-get install -y --no-install-recommends \
      git make ca-certificates curl bash \
    && rm -rf /var/lib/apt/lists/*

# Create non-root user 1000:1000
RUN groupadd -g 1000 ciuser \
 && useradd -m -u 1000 -g 1000 -s /bin/bash ciuser

# Preinstall CI tooling in a global venv for speed
RUN python -m venv /opt/venv \
 && /opt/venv/bin/python -m pip install --upgrade pip setuptools wheel \
 && /opt/venv/bin/pip install pytest coverage ruff mypy bandit safety pip-audit

ENV PATH="/opt/venv/bin:${PATH}"
USER 1000:1000
WORKDIR /workspace