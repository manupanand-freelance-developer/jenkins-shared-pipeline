def call(){
    pipeline {
    agent any
    stages{
        stage('Compile'){
            steps{
                sh 'echo build/compile'
            }
        }
        stage('Test Cases'){
            when{
                expression{ env.BRANCH_NAME != 'main'}
            }
            steps{
                sh 'echo test cases'
            }
        }
        stage('Docker build'){
            when{
                expression{ env.BRANCH_NAME != 'main'}
            }
            steps{
                sh 'echo docker build'
            }
        }
        stage('Docker push images'){
            when{
                expression{ env.BRANCH_NAME != 'main'}
            }
            steps{
                sh 'echo docker push images'
            }
        }
        stage('Deploy to dev env'){
            when{
                expression{ env.BRANCH_NAME != 'main'}
            }
            steps{
                sh 'echo deploy to dev env'
            }
        }
        
    }

    }

}