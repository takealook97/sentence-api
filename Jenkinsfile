pipeline {
    agent any

    environment {
        DOCKER_REPOSITORY = 'underthekey/sentence-api'
        REGISTRY_CREDENTIAL = 'underthekey-docker-hub'
        dockerImage = ''
        DEPLOY_URL = 'https://sentence.underthekey.com'
    }

    parameters {
        string(name: 'DOCKER_NETWORK', defaultValue: 'proxy-network', description: 'docker network name')
        string(name: 'IMAGE_NAME', defaultValue: 'sentence-api', description: 'docker image name')
        choice(name: 'ENV_TYPE', choices: ['dev', 'prod'], description: 'Choose the environment type')
    }

    stages {
        stage('environment setup') {
            steps {
                script {
                    env.BRANCH_NAME = params.ENV_TYPE == 'prod' ? 'main' : 'dev'
                }
            }
        }

        stage('git clone') {
            steps {
                git branch: "${env.BRANCH_NAME}",
                    credentialsId: 'github-token',
                    url: 'https://github.com/underthekey/sentence-api'
            }
        }

        stage('jar build') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew clean bootJar'
            }
        }

        stage('image build & docker-hub push') {
            steps {
                script {
                    docker.withRegistry('', REGISTRY_CREDENTIAL) {
                        sh 'docker buildx create --use --name mybuilder'
                        sh "docker buildx build --platform linux/amd64 -t $DOCKER_REPOSITORY:$BUILD_NUMBER --push ."
                        sh "docker buildx build --platform linux/amd64 -t $DOCKER_REPOSITORY:latest --push ."
                    }
                }
            }
        }
        
        stage('previous docker rm') {
            steps {
                sshagent(credentials: ['deepeet-ubuntu', 'udtk-ubuntu']) {
                    sh """
                        ssh -o ProxyCommand="ssh -W %h:%p -p ${PROXMOX_SSH_PORT} ${PROXMOX_SERVER_ACCOUNT}@${PROXMOX_SERVER_URI}" \
                        -o StrictHostKeyChecking=no ${UDTK_SERVER_ACCOUNT}@${UDTK_SERVER_IP} \
                        '
                        docker ps -q --filter "ancestor=${DOCKER_REPOSITORY}:${env.IMAGE_NAME}-latest" | xargs -r docker stop
                        docker ps -aq --filter "ancestor=${DOCKER_REPOSITORY}:${env.IMAGE_NAME}-latest" | xargs -r docker rm -f
                        docker images ${DOCKER_REPOSITORY}:${env.IMAGE_NAME}-latest -q | xargs -r docker rmi
                        '
                    """
                }
            }
        }

        stage('docker-hub pull') {
            steps {
                sshagent(credentials: ['deepeet-ubuntu', 'udtk-ubuntu']) {
                    sh """
                        ssh -o ProxyCommand="ssh -W %h:%p -p ${PROXMOX_SSH_PORT} ${PROXMOX_SERVER_ACCOUNT}@${PROXMOX_SERVER_URI}" \
                        -o StrictHostKeyChecking=no ${UDTK_SERVER_ACCOUNT}@${UDTK_SERVER_IP} 'docker pull ${DOCKER_REPOSITORY}:latest'
                    """
                }
            }
        }

        stage('service start') {
            steps {
                withCredentials([file(credentialsId: 'udtk-sentence-api-credentials', variable: 'ENV_CREDENTIALS')]) {
                    sshagent(credentials: ['deepeet-ubuntu', 'udtk-ubuntu']) {
                        sh """
                            scp -o ProxyCommand="ssh -W %h:%p -p ${PROXMOX_SSH_PORT} \
                            ${PROXMOX_SERVER_ACCOUNT}@${PROXMOX_SERVER_URI}" \
                            -o StrictHostKeyChecking=no $ENV_CREDENTIALS ${UDTK_SERVER_ACCOUNT}@${UDTK_SERVER_IP}:~/udtk-sentence-api-credentials
                        """

                        sh """
                            ssh -o ProxyCommand="ssh -W %h:%p -p ${PROXMOX_SSH_PORT} \
                            ${PROXMOX_SERVER_ACCOUNT}@${PROXMOX_SERVER_URI}" \
                            -o StrictHostKeyChecking=no ${UDTK_SERVER_ACCOUNT}@${UDTK_SERVER_IP} \
                            '
                            SERVER_PORT=\$(grep SERVER_PORT ~/udtk-sentence-api-credentials | cut -d "=" -f2)

                            docker run -i -e TZ=Asia/Seoul --env-file ~/udtk-sentence-api-credentials \\
                            --name ${params.IMAGE_NAME} --network ${params.DOCKER_NETWORK} \\
                            -p \${SERVER_PORT}:\${SERVER_PORT} \\
                            --restart unless-stopped \\
                            -d ${DOCKER_REPOSITORY}:latest

                            rm -f ~/udtk-sentence-api-credentials
                            '
                        """
                    }
                }
            }
        }

        stage('service test & alert send') {
            steps {
                sh """
                    #!/bin/bash

                    for retry_count in \$(seq 20)
                    do
                        if curl -s "${DEPLOY_URL}/ping" > /dev/null
                        then
                            echo "Build Success!"
                            exit 0
                        fi

                        if [ \$retry_count -eq 20 ]
                        then
                            exit 1
                        fi

                        echo "The server is not alive yet. Retry health check in 5 seconds..."
                        sleep 5
                    done
                """
            }
        }
    }
}
