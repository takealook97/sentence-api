pipeline {
    agent any

    environment {
        imageName = 'underthekey/underthekey'
        registryCredential = 'underthekey-docker-hub'
        dockerImage = ''

        releaseServerAccount = 'deepeet'
        releaseServerUri = 'sentence.udtk.site'
        proxmoxServerAccount = 'deepeet'
        proxmoxServerUri = 'ssh.deepeet.com'

        SSH_PORT  = credentials('deepeet-ssh-port')
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
                    env.BRANCH_NAME = params.ENV_TYPE == 'prod'
                            ? 'main'
                            : 'dev'
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
                    docker.withRegistry('', registryCredential) {
                        sh 'docker buildx create --use --name mybuilder'
                        sh "docker buildx build --platform linux/amd64 -t $imageName:$BUILD_NUMBER --push ."
                        sh "docker buildx build --platform linux/amd64 -t $imageName:latest --push ."
                    }
                }
            }
        }
        stage('previous docker rm') {
            steps {
                sshagent(credentials: ['deepeet-ubuntu', 'udtk-db-ubuntu']) {
                    sh """
                        ssh -p ${SSH_PORT} -J ${proxmoxServerAccount}@${proxmoxServerUri} -o StrictHostKeyChecking=no ${releaseServerAccount}@${releaseServerUri} '
                        sudo docker stop \$(sudo docker ps -aq --filter "ancestor=${imageName}:latest") || true &&
                        sudo docker rm -f \$(sudo docker ps -aq --filter "ancestor=${imageName}:latest") || true &&
                        sudo docker rmi ${imageName}:latest || true
                        '
                    """
                }
            }
        }
        stage('docker-hub pull') {
            steps {
                sshagent(credentials: ['deepeet-ubuntu', 'udtk-db-ubuntu']) {
                    sh "ssh -p ${SSH_PORT} -J ${proxmoxServerAccount}@${proxmoxServerUri} -o StrictHostKeyChecking=no ${releaseServerAccount}@${releaseServerUri} 'sudo docker pull $imageName:latest'"
                }
            }
        }
        stage('service start') {
            steps {
                sshagent(credentials: ['deepeet-ubuntu', 'udtk-db-ubuntu']) {
                    sh '''
                        echo DB_URL=$DB_URL > env.list
                        echo DB_USERNAME=$DB_USERNAME >> env.list
                        echo DB_PASSWORD=$DB_PASSWORD >> env.list
                        echo REDIS_HOST=$REDIS_HOST >> env.list
                        echo REDIS_PORT=$REDIS_PORT >> env.list
                        echo REDIS_PASSWORD=$REDIS_PASSWORD >> env.list
                        echo ALLOWED_IP_1=$ALLOWED_IP_1 >> env.list
                        echo ALLOWED_IP_2=$ALLOWED_IP_2>> env.list
                    '''

                    sh "scp -o StrictHostKeyChecking=no -J ${proxmoxServerAccount}@${proxmoxServerUri} env.list ${releaseServerAccount}@${releaseServerUri}:~"

                    sh """
                        ssh -p ${SSH_PORT} -J ${proxmoxServerAccount}@${proxmoxServerUri} -o StrictHostKeyChecking=no ${releaseServerAccount}@${releaseServerUri} \
                        "sudo docker run -i -e TZ=Asia/Seoul \
                        --env-file ~/env.list \
                        --name ${params.IMAGE_NAME} \
                        --network ${params.DOCKER_NETWORK} \
                        -p 4000:4000 \
                        -d ${env.imageName}:latest"
                    """
                }
            }
        }
    }
}
