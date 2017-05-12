![Icon](https://raw.githubusercontent.com/sangrambankar/MusicBuster/master/app/src/main/res/mipmap-hdpi/ic_launcher.png)
# MusicBuster
A sound streaming application using sloundcloud API.
![Alt Text][[https://raw.githubusercontent.com/sangrambankar/MusicBuster/MusicBuster.gif | height = 100px]]
# Top Features
- Search your favourite songs
- Top 50 songs with various genres

# To Do Features
- New and Hot
- Recent Songs
- Playlists
- Radio Sations

## Used libraries:
- [RxJava](https://github.com/ReactiveX/RxAndroid) and [Retrofit](http://square.github.io/retrofit/) libraries to manage Rest Client
- [ButterKnife](http://jakewharton.github.io/butterknife/) library to bind views and avoid boilerplate views code
- [EventBus](https://github.com/greenrobot/EventBus) library to send data between components and makes code simpler
- [Picasso](http://square.github.io/picasso/) library to manage images
- [MusicCoverView](https://github.com/andremion/Music-Cover-View)
A Subclass of ImageView that 'morphs' into a circle shape and can rotates. Useful to be used as album cover in Music apps.

## Design pattern
MVP (Model View Presenter) pattern to keep it simple and make the code testable, robust and easier to maintain

## Build from the source:

In order to build the app you must provide your own API key from soundcloud.com
Open local.properties file and paste your key instead of ***YOUR_API_KEY*** text in this line:
```
soundcloudapi="YOUR_API_KEY"
```

## License:
```
Copyright 2017, Sangram Bankar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
