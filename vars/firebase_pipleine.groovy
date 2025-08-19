def call(){
    node {
        def branch = env.BRANCH_NAME
        def user   = currentBuild.getBuildCauses('hudson.model.Cause$UserIdCause')[0]?.getUserId()
        def app    = env.appType
        if(app == "nodejs"){
            stage('Checkout') {
                checkout scm
                echo "Branch: ${branch}"
                echo "Triggered by: ${user}"
                echo "trigeged okay"
                
            }
            
        }   
    }
}