# This Makefile builds the API, Web, and DB components of the application
# It assumes that each component has its own Docker Compose file in the specified folders
# It uses the `docker compose build` command to build the images for each component
# It is designed to be run from the root directory of the project


# Set the paths for the application components
APP101-API-PY-FOLDER 	= ../app101-api-py
APP101-WEB-FOLDER    	= ../app101-web
APP101-DB-FOLDER     	= ../app101-db
APP101-MCP-FOLDER		= ../app101-mcp

# Define the default target
all: build-api build-web build-db build-mcp

# Define the build targets for each component
build-api:
	@echo "Building API..."
	cd $(APP101-API-PY-FOLDER) && docker compose build
build-web:
	@echo "Building Web..."
	cd $(APP101-WEB-FOLDER) && docker compose build
build-db:
	@echo "Building DB..."
	cd $(APP101-DB-FOLDER) && docker compose build
build-mcp:
	@echo "Building MCP..."
	cd $(APP101-MCP-FOLDER) && docker compose build

.PHONY: all build-api build-web build-db build-mcp