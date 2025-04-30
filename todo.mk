all the moves should ben done in remote server
1. login to remote server with this root@60.204.157.194
2. set up the website with index as home page, dashboard as dashboard page, and emergency as emergency page
3. detect all docker services and show them in dashboard page, update the items then user can redirect to the service page via clicking the item
4. update nginx config to allow the request to http://60.204.157.194 would see the index page
5. also I have setup the domain name mapping to 60.204.157.194, so the request to http://bigcat.tech would also see the index page


second section:
1. connect to remote repository https://github.com/alpacaThen/bigcat.git
2. create a new branch and overwrite all files with current project structure and files
3. use token.key file to get the github token for repo and workflow
3. push all files to the remote repository and generate README.md file based on your project understanding
4. create workflow to auto deploy changes to remote server with root@60.204.157.194, use current niginx config, don't change it