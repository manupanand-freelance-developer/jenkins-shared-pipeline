def call(){
    node{
        def branch = env.BRANCH_NAME ?: "unknown"
        def userCause = currentBuild.getBuildCauses('hudson.model.Cause$UserIdCause')
        def user = userCause ? userCause[0]?.getUserId() : "system"
        stage('Checkout'){
                try {
                    checkout scm
                    echo "Branch: ${branch}"
                    echo "Triggered by: ${user}"
                    echo "Checkout completed successfully"
                } catch (Exception e) {
                    error "Checkout failed: ${e.getMessage()}"
                }
        }
        stage('Terraform init'){
        }
        stage('Terraform Plan'){
        }
        stage('Terraform Apply'){
        }
    
    
    }
}