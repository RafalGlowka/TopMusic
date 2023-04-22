
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
|                    Splash                    |                Splash with connection error                |
|:--------------------------------------------:|:----------------------------------------------------------:|
| <img src="/screens/screen1.png" width="300"> | <img src="/screens/screen1_error.png" width="300"> |
|     List of albums - loading thumbnails      | List of albums - loading thumbnails error |
| <img src="/screens/screen2.png" width="300"> | <img src="/screens/screen2_error.png" width="300"> |
|                List of albums                | Album details |
|        <img src="/screens/screen3.png" width="300">        | <img src="screens/screen4.png" width="300"> |

## Screen shots in landscape orientation
|                    Splash                     |                Splash with connection error                |
|:---------------------------------------------:|:----------------------------------------------------------:|
| <img src="screens/screen1L.png" width="300">  | |
|                List of albums                 | List of albums - loading thumbnails error |
| <img src="/screens/screen3L.png" width="300"> | <img src="/screens/screen2_errorL.png" width="300"> |
|          Album details - top section          | Album details - bottom section |
|        <img src="screens/screen4L1.png" width="300">         | <img src="screens/screen4L2.png" width="300"> |

## Video
![Video from testing](video/Testing.mp4)

Further development should be focused on:
* androidTests - Testing fragments and emitting ViewEvents
* Support edge cases of Activity <--> System interaction
* Support for animations and transitions
* Testing business flows. 
* Move snackBars/toasts? to composer.


