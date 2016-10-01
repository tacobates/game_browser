### Root Install Directory
#sudo mkdir /usr/local/game_browser
#sudo chmod a+w /usr/local/game_browser

### Sub Directories
##Game Config Files synced from web (how to launch)
mkdir -m 777 /usr/local/game_browser/conf
##User config to override directories
mkdir -m 777 /usr/local/game_browser/conf/user
##Log Files - Install (overwrite), Usage (rolling), Error (rolling)
mkdir -m 777 /usr/local/game_browser/log
##Meta data - Quick, Descrips & Other non-searchables, icons, screens, & user ratings and usage
mkdir -m 777 /usr/local/game_browser/meta
mkdir -m 777 /usr/local/game_browser/meta/detail
mkdir -m 777 /usr/local/game_browser/meta/icon
mkdir -m 777 /usr/local/game_browser/meta/screen
mkdir -m 777 /usr/local/game_browser/meta/user


#TODO: make folder creation simple:
  # git download zip
  # extract to /usr/local/game_browser
  # run JAR as "pi" user or "root"
    # verify folder, or update Meta.DIR with actual location
    # still chmod a+w everything in the folder we exist in

