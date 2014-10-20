#!/bin/sh

DATE=$(date +"%Y-%m-%d_%H%M")
fswebcam -r 1280x720 --save /var/www/wp/wp-content/upload/MyPictures/$DATE.jpg

