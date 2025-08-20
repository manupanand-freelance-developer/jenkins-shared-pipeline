def call(){
    node {
        def branch = env.BRANCH_NAME ?: "unknown"
        def userCause = currentBuild.getBuildCauses('hudson.model.Cause$UserIdCause')
        def user = userCause ? userCause[0]?.getUserId() : "system"
        def app = env.appType
        
        if(app == "nodejs"){
            stage('Checkout') {
                try {
                    checkout scm
                    echo "Branch: ${branch}"
                    echo "Triggered by: ${user}"
                    echo "Checkout completed successfully"
                } catch (Exception e) {
                    error "Checkout failed: ${e.getMessage()}"
                }
            }
            
            stage('Install Dependencies') {
                echo "Installing Dependencies"
                // Add actual npm install command here for real implementation
                // sh 'npm install'
            }
            
            stage('Build Angular App') {
                echo "Building angular application"
                // Add actual build command here for real implementation
                // sh 'npm run build'
            }
            
            stage('Approval to deploy') {
                timeout(time: 5, unit: 'MINUTES') {
                    input message: "Approve deployment to PROD?", 
                          submitter: 'admindev,admininfra',
                          ok: 'Deploy'
                }
            }
            
            stage('Deploy to Server') {
                echo "Deploying to firebase"
                // Add actual deployment commands here
                // sh 'firebase deploy'
            }
            stage('Email Notification'){
                echo "This is build log email"
            }
        } else {
            echo "Skipping pipeline - appType is not nodejs (current: ${app})"
        }
    }
}