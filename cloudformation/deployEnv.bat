set projectName=%1
aws cloudformation deploy --template codebuild.yml --stack-name %projectName%codebuild --capabilities CAPABILITY_IAM --parameter-overrides ProjectName=%projectName%
aws codebuild start-build --project-name %projectName%
aws s3api wait object-exists --bucket %projectName%bucketcodedeploy --key app.zip
aws cloudformation deploy --template ebs.yml --stack-name %projectName%ebs --parameter-overrides ProjectName=%projectName%
aws cloudformation list-exports