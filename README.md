
# Top 100 Music
Example application build on https://rss.applemarketingtools.com/ public API as an architecture test case.

The purpose of this application is to show usage of some basic libraries and architecture concepts:
* Clean architecture
* MVVM + BusinessFlow 
* Composer
* coroutines, flows
* Retrofit2
* Lottie

## Screen shots
![Splash screen](/screens/screen1.png)
![Splash screen with network error and retry action](/screens/screen1_error.png)
![List of albums - loading thumbnails](/screens/screen2.png)
![List of albums - loading thumbnails error](/screens/screen2_error.png)
![List of albums](/screens/screen3.png)
![Album details](screens/screen4.png)

## Screen shots in landscape orientation
![Splash screen](/screens/screen1L.png)
![List of albums - loading thumbnails error](/screens/screen2_errorL.png)
![List of albums](/screens/screen3L.png)
![Album details - top section](screens/screen4L1.png)
![Album details - bottom section](screens/screen4L2.png)

## Video
![Video from testing](video/Testing.mp4)

Further development should be focused on:
* androidTests - Testing fragments and emitting ViewEvents
* Support edge cases of Activity <--> System interaction
* Support for animations and transitions
* Testing business flows. 
* Move snackBars/toasts? to composer.


