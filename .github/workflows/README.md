# GitHub Actions Workflows

## Build and Push Docker Image

The `build-and-push.yml` workflow automatically builds and pushes Docker images to GitHub Container Registry (ghcr.io).

### When it runs:
- On pushes to `main` branch
- On pull requests to `main` branch
- On tags starting with `v*` (e.g., `v1.0.0`)
- Manually via workflow_dispatch

### What it does:
1. Checks out the repository
2. Sets up JDK 21
3. Builds the application with Maven
4. Sets up Docker Buildx
5. Logs in to GitHub Container Registry
6. Builds and pushes the Docker image with appropriate tags

### Image Tags:
- `latest` - Latest build from main branch
- `main` - Builds from main branch
- `main-<sha>` - Specific commit SHA
- `v1.0.0` - Semantic version tags
- `1.0` - Major.minor version tags
- `pr-<number>` - Pull request builds

### Using the Images:

#### Pull the latest image:
```bash
docker pull ghcr.io/nichom01/business-rules-engine:latest
```

#### Run the container:
```bash
docker run -p 8080:8080 \
  -e AWS_REGION=us-east-1 \
  -e SQS_QUEUE_NAME=your-queue-name \
  ghcr.io/nichom01/business-rules-engine:latest
```

#### View images in GitHub:
Navigate to: `https://github.com/nichom01/business-rules-engine/pkgs/container/business-rules-engine`

### Permissions:
The workflow uses `GITHUB_TOKEN` which is automatically provided by GitHub Actions. No additional secrets are required.

### Cache:
The workflow uses GitHub Actions cache for:
- Maven dependencies
- Docker build layers

This significantly speeds up subsequent builds.
