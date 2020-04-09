# serverless

1. Prerequisites for building and deploying your application locally.
    * aws cli 
    * jdk 1.8
    * mav installed
2. Build and Deploy instructions for web application.
    * circleCI
    * aws Cloudformation
    * aws CodeDeploy
    
3. Set up SSL Certificate for prod.mengchen-gao.me

    ```shell script
   
   1. generaet private key 
    openssl genrsa 2048 > private-key.pem
   
   2. get CRS
   openssl req -new -key private-key.pem -out csr.pem
   
   3. get Certificate on Namecheap
   
   4. set IAM for certificate
   aws iam upload-server-certificate \
       --server-certificate-name ch***ame \
       --certificate-body file://pr***e.pem \
       --private-key file://private-key.pem \
       --profile p***
   
   5. check 
   aws iam get-server-certificate \
     --server-certificate-name ch***me \
     --profile p***
    ```
