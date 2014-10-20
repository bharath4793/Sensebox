#!/bin/bash
PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

echo $(date)
cd /home/pi/
date_pattern=$(date --date=yesterday +"%Y-%m-%d")
ls /var/www/wp/wp-content/upload/MyPictures/*$date_pattern* > stills.txt

DATE=$(date --date=yesterday +"%Y-%m-%d")
three_days_ago=$(date --date="3 days ago" "+%m-%d")
yesterday=$(date --date yesterday "+%m-%d")
video_name=$(date +"%m-%d")

#mencoder -nosound -mf fps=10 -o $yesterday.avi -ovc x264 vcodec=mpeg4:vbitrate=10000 mf://@stills.txt

max_retries=3
for i in $(seq ${max_retries});do
    mencoder -nosound -ovc lavc -lavcopts vcodec=mpeg4:aspect=16/9:vbitrate=8000000 -vf scale=1920:1080 -o $yesterday.avi -mf type=jpeg:fps=10 mf://@stills.txt
    sleep 10
    if [ $? == 0 ];then
        break
    fi
done


max_retries=3
for i in $(seq ${max_retries});do
    video_link=$(youtube-upload --email=emkontakis@gmail.com --password=sailor437 --title="Timelapse Video of "$yesterday --description="Heraklion Timelapse Video of "$yesterday  \
    --category=Tech --keywords="Raspberry PI, Timelapse" /home/pi/$yesterday.avi)
    sleep 10
    if [ $? == 0 ];then
	break
    fi
done

echo $video_link
youtube-upload --email=emkontakis@gmail.com --password=sailor437 --add-to-playlist http://gdata.youtube.com/feeds/api/playlists/PLbjjzh8UkLN2pQTZwXyQ4kP2bXb7Zwtmo $video_link

#youtube-upload --email=emkontakis@gmail.com --password=sailor437 --title=$yesterday --description="Timelapse Video of "$yesterday --category=Science & Technology --keywords="Raspberry PI, Timelapse" /home/pi/$yesterday.mp4
echo $(date)
/home/pi/Desktop/camera/Dropbox-Uploader/dropbox_uploader.sh upload /home/pi/Desktop/camera/video_maker.log /Public/pi_videos
rm $three_days_ago.avi
cd /var/www/wp/wp-content/upload/MyPictures
find . -type f -name $DATE\* -exec rm {} \;


