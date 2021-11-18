#!/bin/bash
readonly REGION=Your-AWS-region-here
readonly ECR_REPOSITORY=Your-ECR-repository-here
readonly IMAGE_TAG=Your-image-tag-here
readonly SECRET_NAME=Your-DB-password-secret-here

# Install Docker
amazon-linux-extras install docker
service docker start
usermod -a -G docker ec2-user
chkconfig docker on

# Setup ECR credentials helper
yum install amazon-ecr-credential-helper -y
cd ~
mkdir .docker
cd .docker
touch config.json
account_id=$(aws sts get-caller-identity --query Account --output text)
echo "{
	\"credHelpers\": {
		\"public.ecr.aws\": \"ecr-login\",
		\"${account_id}.dkr.ecr.${REGION}.amazonaws.com\": \"ecr-login\"
	}
}" >> config.json


# Pull Application Docker image from ECR
full_image_name="${account_id}.dkr.ecr.${REGION}.amazonaws.com/${ECR_REPOSITORY}:${IMAGE_TAG}"
docker pull $full_image_name

# Retrieve DB secret using AWS CLI
db_password=$(aws secretsmanager get-secret-value --secret-id ${SECRET_NAME} --region ${REGION} --query SecretString --output text | cut -d: -f2 | tr -d \"})
docker run -p 80:8080 -e "db_password=${db_password}" -d --name simple-java-rest-backend $full_image_name
