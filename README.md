
# Top 100 Music
Example application build on https://rss.applemarketingtools.com/ public API as an architecture test case.

The purpose of this application is to show usage of some basic libraries and architecture concepts:
* Clean architecture - Currently I resigned from UseCases (quite important part of clean architecture). All of them were 1:1 calls to repositories and do not provide any additional value. By doing so I would like to emphasize the need to rethink the elements of architecture in the context of a particular project. In bigger project use of them may provide additional value in with testing or code reusability. I would use cold-code solution like coroutine Flow in that case.  
* MVVM + BusinessFlow 
* Composer
* coroutines, flows
* Retrofit2
* Lottie
* Kotest, Mockk

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
* Support edge cases of Activity <--> System interaction
* Support for animations and transitions


