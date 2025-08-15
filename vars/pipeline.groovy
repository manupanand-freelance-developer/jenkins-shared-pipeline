def call(){
    node {
    def branch = env.BRANCH_NAME
    def user   = currentBuild.getBuildCauses('hudson.model.Cause$UserIdCause')[0]?.getUserId()
    def app    = env.appType
 
    if (branch.startsWith("feature/")) {
        stage('Checkout') {
            checkout scm
            echo "Branch: ${branch}"
            echo "Triggered by: ${user}"
            echo "trigeged okay"
            echo "trigeged okay 2"
        }

        stage('Build & Test') {
            echo "Building feature branch..."
        }

        stage('Deploy to Test') {
            echo "Deploying to test environment..."
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
        }
        
        }
    } else {
        echo "No matching deployment rule for this branch."
    }
}




}