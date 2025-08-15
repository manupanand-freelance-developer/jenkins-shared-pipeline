def call(){
    node {
    def branch = env.BRANCH_NAME ? : " "
    def user   = currentBuild.getBuildCauses('hudson.model.Cause$UserIdCause')[0]?.getUserId()
    def app    = env.appType
 
    if (branch.startsWith("feature/")) {
       if(app == "nodejs"){
             stage('Checkout') {
            checkout scm
            echo "Branch: ${branch}"
            echo "Triggered by: ${user}"
            echo "trigeged okay"
            echo "trigeged okay 2"
        }

        stage('Unit test case') {
            echo "Running Unit test case"
            echo "npm test case"
        }
        stage('Integration Test') {
            echo "Running Integration test case"
            echo "npm integration test"
        }
        stage('Regression Test') {
            echo "Running regression test case."
            echo "npm regression test"
        }

        stage('Deploy to Test Environment') {
            echo "Deploying to test environment..."
            echo "fire base deploy to test env"
        }
       }

    } else if (branch == "main" || env.TAG_NAME) {
        if(app == "nodejs"){
            stage('Build') {
            
            echo "Building production code..."
            echo "test main branch run"
            echo "test main branch run 2"
            echo "testing pipeline jenkins in gcp"
            echo "testing new trigger pipeline jenkins in gcp to do"
        }

        stage('Approval') {
            // Only admindev or admininfra can approve
            input message: "Approve deployment to PROD?", 
                  submitter: 'admindev,admininfra'
        }

        stage('Deploy to Production') {
            echo "Deploying to production..."
            echo "firebase deploy production"
        }
        
        }
    } else {
        echo "No matching deployment rule for this branch."
    }
}




}