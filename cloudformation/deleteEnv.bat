set projectName=%1
aws cloudformation delete-stack --stack-name %projectName%ebs
aws s3 rm s3://%projectName%bucketcodedeploy --recursive
aws cloudformation delete-stack --stack-name %projectName%codebuild
aws cloudformation wait stack-delete-complete --stack-name %projectName%ebs
aws cloudformation wait stack-delete-complete --stack-name %projectName%codebuild