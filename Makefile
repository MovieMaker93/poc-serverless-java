# Usage:
# make        # compile the java code
# make test  # compile and testing the java code
# make deploy # compile, testing and deploy to AWS
.PHONY: build test deploy

all: build

build:
	@echo "Building the application with maven"
	mvn -B clean package -DskipTests

test:
	@echo "Building and Testing the application with maven"
	mvn -B clean package

deploy:
	@echo "Building/testing the application with maven and deploy with Serverless"
	mvn -B clean package && sls deploy
