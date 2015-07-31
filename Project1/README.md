Udacity NanoDegree Project One - Stage One

Jim Howard
jim@grayraven.com

This is an implementation of the Udacity Android NanoDegree, Stage one.

Basic features:

On start users are presented with a grid view of movie poster thumbnails obtained via Travis Bell's Movie Database API. The grid can be sorted by popularity or average user rating, selected by the options menu.

When a poster is clicked a details view is displayed, showing the following information about the selected movie:

- Original title
- Release date
- Average rating
- Plot synopsis

This detail information is overlayed over an image of the movie's poster.

Implementation details:

The Movie detail screen supports a left-to-right swipe gesture to return to the main grid view.

Movie data is downloaded in an IntentService, data is not re-downloaded after an orientation change.

Users downloading this code from GitHub must modify the file ApiKey.jave with their own valid Movie Database API key.

TODO's:

Improve user experience in the case of no or lost internet service.
Improve portrait layout.
Improve error handling in IntentService.

Acknowledgements:

Gesture support code based on an example provided by StackOverflow user Thomas Fankhauser, question #937313.

Images are loaded using the Picasso image library ( http://square.github.io/picasso/ ).

Movie data is obtained from the Movie Database using 'themovieapi' by GitHub user holgerbrandl ( https://github.com/holgerbrandl/themoviedbapi/ ).

Rights:

This application is strictly for non-commercial educational purposes.
Licensed under Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0).

